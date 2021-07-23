/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat;

import net.minecraft.entity.Entity;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;


public class AntiBots
        extends Module {

    public AntiBots() {
        super("AntiBots", "remove bots.", ModuleType.Combat);
        this.setEnabled(true);
    }


    public boolean isServerBot(Entity entity) {
        if (this.isEnabled()) {
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                return false;
            }
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                return false;
            }
            return false;
        }
        return false;
    }
}

