package tomorrow.tomo.mods.modules.player;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

import java.util.ArrayList;

public class NoFall
        extends Module {
    private Mode mod = new Mode("Mode", "Mode", new String[]{"Hypixel", "Onground"}, "Hypixel");
    private ArrayList<Packet> packets = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();

    public NoFall() {
        super("NoFall", ModuleType.Player);
        this.addValues(mod);
    }

    @EventHandler
    private void onUpdate(EventPacketSend e) {
        if (mod.getValue().equals("Onground")) {
            if (this.mc.thePlayer.fallDistance > 2.5f) {
                if (e.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
                    c.onGround = mc.thePlayer.ticksExisted % 2 == 0;
                    e.setPacket(c);
                }
            }

        }
//
//        if (this.mod.getValue().equals("Hypixel") && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage) {
//            if (e.getPacket() instanceof C03PacketPlayer && ((C03PacketPlayer) e.getPacket()).isMoving()) {
//                if (mc.thePlayer.fallDistance > 2.6f) {
//                    e.setCancelled(true);
//                }
//            }
//        }

    }

    float dis = 0;

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (this.mod.getValue().equals("Hypixel") && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.capabilities.disableDamage) {
            if (mc.thePlayer.fallDistance > 2.75f + getActivePotionEffect() * 0.2f && timer.delay(100)) {
                mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer(true));
                timer.reset();
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

    private int getActivePotionEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
}

