/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.misc;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketRecieve;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
public class NoRotate
extends Module {
    public NoRotate(){
        super("NoStuck", ModuleType.Misc);
    }
    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}

