package tomorrow.tomo.mods.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

import java.util.ArrayList;
public class AntiVoid
        extends Module {
	private Mode mode = new Mode("Mode","Mode",new String[] {"Hypixel1","Hypixel2"},"Hypixel1");
    public AntiVoid() {
        super("AntiVoid", ModuleType.Movement);
    }

    public ArrayList<Packet> packets = new ArrayList();
    public TimerUtil timerUtil = new TimerUtil();

    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (this.mc.thePlayer.fallDistance < 3.0f) {
            return;
        }
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
        if(mode.getValue().equals("Hypixel2"))
        	mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) e.getPacket()).getPositionX(), ((C03PacketPlayer) e.getPacket()).getPositionY(), ((C03PacketPlayer) e.getPacket()).getPositionZ(), ((C03PacketPlayer) e.getPacket()).isOnGround()));
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
        if(!mode.getValue().equals("Hypixel1"))
        	return;
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

        if (timerUtil.delay(10)) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 9, mc.thePlayer.posZ, false));
            if (timerUtil.delay(200)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.2, mc.thePlayer.posZ, false));
                if (timerUtil.delay(500)) {
                    mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    timerUtil.reset();
                }
            }
        }


//        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {
//            this.mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 100, mc.thePlayer.posZ);
//        }
    }
}

