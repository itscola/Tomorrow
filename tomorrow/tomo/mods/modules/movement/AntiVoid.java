package tomorrow.tomo.mods.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

import java.util.ArrayList;

public class AntiVoid
        extends Module {
    public AntiVoid(){
        super("AntiVoid", ModuleType.Movement);
    }
    public ArrayList<Packet> packets = new ArrayList();
    public TimerUtil timerUtil = new TimerUtil();

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (this.mc.thePlayer.fallDistance < 3.0f) {
            return;
        }
//        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {
//            if (packets.size() < 10 && (e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook || e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition)) {
//                packets.add(e.getPacket());
//                e.setCancelled(true);
//            } else {
//                for (Packet p : packets) {
//                    mc.getNetHandler().addToSendQueueWithoutEvent(p);
//                }
//                packets.clear();
//            }
//        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        boolean blockUnderneath = false;
        int i = 0;
        while ((double) i < this.mc.thePlayer.posY + 2.0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double) i, this.mc.thePlayer.posZ);
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
            }
            ++i;
        }
        if (blockUnderneath) {
            return;
        }
        if (this.mc.thePlayer.fallDistance < 2.0f) {
            return;
        }

        if(timerUtil.delay(400)) {
//            mc.thePlayer.setPosition(mc.thePlayer.motionX, mc.thePlayer.motionY + 3, mc.thePlayer.motionZ);
//            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
//            mc.thePlayer.onGround = true;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+5, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+4, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+3, mc.thePlayer.posZ, false));
            timerUtil.reset();
        }


//        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {
//            this.mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 100, mc.thePlayer.posZ);
//        }
    }
}

