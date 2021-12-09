package tomorrow.tomo.mods.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

public class Criticals extends Module {
    public Mode mode = new Mode("Mode", "Mode", new String[]{"HypixelPacket", "Packet", "Jump", "LowJump"}, "HypixelPacket");
    public Numbers<Number> delay = new Numbers<>("Delay", 300, 100, 1500, 10);
    public Numbers<Number> hurtTime = new Numbers<>("HurtTime", 15, 1, 20, 1);

    private TimerUtil timer = new TimerUtil();

    public Criticals() {
        super("Criticals", ModuleType.Combat);
        addValues(delay, mode, hurtTime);
    }

    private boolean canCrit(Entity target) {
        return this.isEnabled() && mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWeb && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && mc.thePlayer.ridingEntity == null && target.hurtResistantTime <= this.hurtTime.getValue().intValue();
    }

    public void doCrit(Entity entity) {
        if (!this.isEnabled())
            return;
        switch (mode.getModeAsString()) {
            case "HypixelPacket":
//                if (canCrit(entity))
                    onHypixelCrit();
                break;
            case "Packet":
                if (mc.thePlayer.onGround) {
                    mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15f, mc.thePlayer.posZ, false));
                    mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                } else {
                    mc.getNetHandler().addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.01f, mc.thePlayer.posZ, false));
                }
                break;
            case "Jump":
                if (canCrit(entity))
                    mc.thePlayer.jump();
                break;
            case "LowJump":
                if (canCrit(entity))
                    mc.thePlayer.motionY = 0.014;
                break;
        }
        mc.thePlayer.onCriticalHit(entity);
        timer.reset();
    }

    public void onHypixelCrit() {
        for (double d : new double[]{0.04, 0.00079999923706, 0.04079999923707, 0.00159999847412}) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ, false));
        }

    }
}
