/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.world;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

@Mod(name = "DeathClip", description = ".", type = ModuleType.World)
public class Deathclip
        extends Module {
    private TimerUtil timer = new TimerUtil();

    public Deathclip() {
        super("DeathClip", ModuleType.Render);

    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (this.mc.thePlayer.getHealth() == 0.0f && this.mc.thePlayer.onGround) {
            this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.posX, -10.0, this.mc.thePlayer.posZ);
            if (this.timer.hasReached(500.0)) {
                this.mc.thePlayer.sendChatMessage("/home");
            }
        }
    }
}

