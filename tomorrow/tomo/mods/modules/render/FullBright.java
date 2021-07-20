/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventTick;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class FullBright
        extends Module {
    private float old;

    public FullBright() {
        super("FullBright", ModuleType.Render);

    }

    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
    }

    @EventHandler
    private void onTick(EventTick e) {
        this.mc.gameSettings.gammaSetting = 1.5999999E7f;
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.old;
    }
}

