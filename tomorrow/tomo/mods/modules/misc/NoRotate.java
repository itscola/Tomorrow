/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.misc;

import me.superskidder.server.util.TimerUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketRecieve;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.player.Helper;

public class NoRotate
        extends Module {
    public NoRotate() {
        super("NoStuck", ModuleType.Misc);
    }

    TimerUtil timerUtil = new TimerUtil();
    int stuck = 0;

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (timerUtil.delay(10000)) {
            stuck = 0;
            timerUtil.reset();
        }
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && stuck < 3) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook) e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
            Helper.sendMessage("(NoStuck!):" + stuck + " " + look.yaw + "  " + look.pitch);
        }
    }
}

