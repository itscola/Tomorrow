/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.world;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventTick;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class FastPlace
extends Module {

    public FastPlace(){
        super("FastPlace", ModuleType.Render);
    }

    @EventHandler
    private void onTick(EventTick e) {
        this.mc.rightClickDelayTimer = 0;
    }
}

