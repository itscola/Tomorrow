/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class Bobbing
extends Module {
    private Numbers<Number> boob = new Numbers<Number>("Amount", "Amount", 1.0, 0.1, 5.0, 0.5);

    public Bobbing() {
        super("Bobbing", ModuleType.Render);
        this.addValues(this.boob);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * this.boob.getValue().floatValue());
        }
    }
}

