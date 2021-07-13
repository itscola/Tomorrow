/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.misc;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventTick;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import java.awt.Color;

import net.minecraft.entity.player.EnumPlayerModelParts;
@Mod(name = "SkinBugs",description = "." , type = ModuleType.Misc)
public class SkinFlash
extends Module {
    public SkinFlash(){
        super("SkinBugs", ModuleType.Misc);
    }
    @Override
    public void onDisable() {
        EnumPlayerModelParts[] parts;
        if (this.mc.thePlayer != null && (parts = EnumPlayerModelParts.values()) != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = arrayOfEnumPlayerModelParts1.length;
            int i = 0;
            while (i < j) {
                EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                this.mc.gameSettings.setModelPartEnabled(part, true);
                ++i;
            }
        }
    }

    @EventHandler
    private void onTick(EventTick e) {
        EnumPlayerModelParts[] parts;
        if (this.mc.thePlayer != null && (parts = EnumPlayerModelParts.values()) != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = arrayOfEnumPlayerModelParts1.length;
            int i = 0;
            while (i < j) {
                EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                boolean newState = this.isEnabled() ? random.nextBoolean() : true;
                this.mc.gameSettings.setModelPartEnabled(part, newState);
                ++i;
            }
        }
    }
}

