package tomorrow.tomo.mods.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.player.Teams;
import tomorrow.tomo.mods.modules.world.Scaffold;
import tomorrow.tomo.utils.cheats.player.PlayerUtils;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class Killaura extends Module {

    public static EntityLivingBase target;
    private Option mob = new Option("Mob", false);
    private Option animals = new Option("Animals", false);
    private Option player = new Option("Player", true);
    private Option wither = new Option("Wither", false);
    private Option invisible = new Option("Invisible", false);
    private Option block = new Option("Block", false);
    private Option smart = new Option("SmartSwitch", false);
    private Option noSwing = new Option("NoSwing", false);


    private Mode mode = new Mode("Mode", "Mode", new String[]{"Switch", "Single"}, "Switch");
    private Mode rot = new Mode("RotationMode", "RotationMode", new String[]{"Instant", "Animate"}, "Animate");
    private Mode priority = new Mode("Priority", "Priority", new String[]{"Distance", "Health", "Direction"}, "Distance");
    private Mode esp = new Mode("ESP", "ESP", new String[]{"NONE", "Box", "HeadBox", "RainbowBox", "VapeBox"}, "NONE");

    private Numbers<Number> switchDelay = new Numbers<>("SwitchDelay", 150, 10, 2000, 10);
    private Numbers<Number> rotSpeed = new Numbers<>("RotationSpeed", 80, 1, 100, 1);
    private Numbers<Number> cps = new Numbers<>("CPS", 11, 1, 18, 0.1);
    private Numbers<Number> range = new Numbers<>("Range", 3.8, 1, 6, 0.1);
    private Numbers<Number> targets = new Numbers<>("Targets", 3, 1, 6, 1);

    static ArrayList<EntityLivingBase> curTargets = new ArrayList<>();

    private TimerUtil timer = new TimerUtil();
    private TimerUtil cpsTimer = new TimerUtil();
    private AnimationUtils animationUtils1 = new AnimationUtils();
    private AnimationUtils animationUtils2 = new AnimationUtils();


    private int cur = 0;
    private Criticals crit;

    public Killaura() {
        super("KillAura", ModuleType.Combat);
        addValues(priority, esp, cps, range, targets, mob, animals, player, wither, invisible, rot, switchDelay, rotSpeed, block, mode, smart, noSwing);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        crit = (Criticals) ModuleManager.getModuleByClass(Criticals.class);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
        curTargets.clear();
        mc.gameSettings.keyBindUseItem.pressed = false;
    }

    public void filter(List<? extends Entity> entities) {
        curTargets.clear();
        target = null;
        for (Entity entity : entities) {
            if (entity == mc.thePlayer) continue;
            if (!entity.isEntityAlive()) continue;
            if(Teams.isOnSameTeam(entity)) continue;
            if (((AntiBots) ModuleManager.getModuleByClass(AntiBots.class)).isServerBot(entity)) continue;
            if (curTargets.size() > targets.getValue().intValue()) continue;
            if (mc.thePlayer.getDistanceToEntity(entity) > range.getValue().doubleValue()) continue;

            if (entity instanceof EntityMob && ((boolean) mob.getValue())) curTargets.add((EntityLivingBase) entity);

            if (entity instanceof EntityAnimal && ((boolean) animals.getValue()))
                curTargets.add((EntityLivingBase) entity);

            if (entity instanceof EntityPlayer && ((boolean) player.getValue())) {
                if (entity.isInvisible() && ((boolean) invisible.getValue()))
                    curTargets.add((EntityLivingBase) entity);
                else if (!entity.isInvisible())
                    curTargets.add((EntityLivingBase) entity);
            }

            if (entity instanceof EntityWither && ((boolean) wither.getValue()))
                curTargets.add((EntityLivingBase) entity);
        }
    }

    float cYaw, cPitch;

    @EventHandler
    public void onPre(EventPreUpdate e) {
        this.setSuffix(this.mode.getModeAsString());
        if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
            return;
        filter(mc.theWorld.getLoadedEntityList());
        switchTarget();
        if (curTargets.size() != 0) {
            target = curTargets.get(cur);
            float[] rotations = PlayerUtils.getRotations(target);
            rotate(rotations, e);
        }
        if (mc.thePlayer.isBlocking() && target == null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        }
    }

    private void stopBlock() {
        mc.playerController.onStoppedUsingItem(mc.thePlayer);
    }

    private void startBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
    }

    @EventHandler
    public void onPost(EventPostUpdate e) {
        if (ModuleManager.getModuleByClass(Scaffold.class).isEnabled())
            return;
        if (cpsTimer.delay(1000 / cps.getValue().floatValue()) && target != null) {
            if (mc.thePlayer.isBlocking())
                this.stopBlock();
            crit.doCrit(target);
            if (!(boolean) noSwing.getValue()) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            } else {
                mc.thePlayer.swingItem();
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            if (this.canBlock())
                this.startBlock();
            cpsTimer.reset();
        }
    }

    private boolean canBlock() {
        return ((boolean) block.getValue()) && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && target != null;
    }

    private void rotate(float[] rotations, EventPreUpdate e) {
        switch (rot.getModeAsString()) {
            case "Instant":
                cYaw = rotations[0];
                cPitch = rotations[1];
                PlayerUtils.rotate(cYaw, cPitch);
                e.setYaw(cYaw);
                e.setPitch(cPitch);
                break;
            case "Animate":
                if (cYaw == 0 || cPitch == 0) {
                    cYaw = mc.thePlayer.rotationYaw;
                    cPitch = mc.thePlayer.rotationPitch;
                }
                cYaw = animationUtils1.animate(rotations[0], cYaw, rotSpeed.getValue().intValue() / 100f);
                cPitch = animationUtils2.animate(rotations[1], cPitch, rotSpeed.getValue().intValue() / 100f);
                PlayerUtils.rotate(cYaw, cPitch);
                e.setYaw(cYaw);
                e.setPitch(cPitch);
        }
    }

    public float getRotationDis(EntityLivingBase entity) {
        float pitch = PlayerUtils.getRotations(entity)[1];
        return mc.thePlayer.rotationPitch - pitch;
    }

    private void switchTarget() {
        if (mode.isValid("Switch")) {
            if (timer.delay(switchDelay.getValue().intValue())) {
                if (cur < curTargets.size() - 1) {
                    if (((boolean) smart.getValue() && target != null && target.getHealth() < 5)) {
                        timer.reset();
                        return;
                    }
                    cur++;
                } else {
                    cur = 0;
                }
                timer.reset();
            }
        } else {
            switch (priority.getValue().toString()) {
                case "Distance":
                    curTargets.sort(((o1, o2) -> (int) (o2.getDistanceToEntity(mc.thePlayer) - o1.getDistanceToEntity(mc.thePlayer))));
                    break;
                case "Health":
                    curTargets.sort(((o1, o2) -> (int) (o2.getHealth() - o1.getHealth())));
                    break;
                case "Direction":
                    curTargets.sort(((o1, o2) -> (int) (getRotationDis(o2) - getRotationDis(o1))));
                    break;
            }
            cur = 0;
        }
    }
}
