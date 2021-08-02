/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.movement;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class NoSlow
        extends Module {
    public static Option noAttackSlow = new Option("NoAttackSlow", true);

    public NoSlow() {
        super("NoSlow", ModuleType.Movement);
        addValues(noAttackSlow);
    }


    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    }
}

