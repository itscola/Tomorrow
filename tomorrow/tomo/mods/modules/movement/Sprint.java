/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.movement;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class Sprint
extends Module {
    private Option<Boolean> omni = new Option<Boolean>("Omni-Directional", "omni", true);

    public Sprint() {
        super("Sprint", ModuleType.Movement);
        this.addValues(this.omni);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValue() != false ? this.mc.thePlayer.moving() : this.mc.thePlayer.moveForward > 0.0f) {
            mc.gameSettings.keyBindSprint.pressed = true;
//            this.mc.thePlayer.setSprinting(true);
        }
    }
}

