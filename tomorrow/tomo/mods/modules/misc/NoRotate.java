/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.misc;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import java.awt.Color;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
@Mod(name = "NoRotate",description = "." , type = ModuleType.Misc)
public class NoRotate
extends Module {
    public NoRotate(){
        super("NoRotate", ModuleType.Misc);
    }
    @EventHandler
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}

