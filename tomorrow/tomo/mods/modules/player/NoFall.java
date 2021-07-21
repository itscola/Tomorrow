package tomorrow.tomo.mods.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

import java.util.ArrayList;

public class NoFall
        extends Module {
    private Mode mod = new Mode("Mode", "Mode", MOD.values(), MOD.Hypixel);
    private ArrayList<Packet> packets = new ArrayList<>();

    enum MOD {
        Hypixel,
        Onground
    }
    
    public NoFall() {
        super("NoFall", ModuleType.Player);
        this.addValues(mod);
    }

    @EventHandler
    private void onUpdate(EventPacketSend e) {
        if (mod.getValue().equals(MOD.Onground)) {
            if (this.mc.thePlayer.fallDistance > 2.5f) {
//                e.setCancelled(true);
//             }else {
                if (e.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer c = (C03PacketPlayer) e.getPacket();
                    c.onGround = mc.thePlayer.ticksExisted % 2 == 0;
//                        e.setCancelled(true);
                    e.setPacket(c);
                }
            }

        }

    }
    float dis = 0;
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (this.mc.thePlayer.fallDistance > 2.75f) {
            if (mod.getValue().equals(MOD.Hypixel)) {
            	if(mc.thePlayer.fallDistance - dis > 2.75f) {
            		dis = mc.thePlayer.fallDistance;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            	}
                
            }
        }
    }
}

