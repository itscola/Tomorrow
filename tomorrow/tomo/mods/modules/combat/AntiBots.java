/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPostUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.player.Helper;


public class AntiBots
extends Module {
    Mode mode = new Mode<>("Mode","Mode",mods.values(),mods.Basic);

    enum mods{
        Basic,
        Hypixel,
        CubeCraft
    }

    public AntiBots() {
        super("AntiBots","remove bots.",ModuleType.Combat);
        this.addValues(mode);
        this.setEnabled(true);
    }

    @EventHandler
    public void onUpdate(EventPostUpdate e){
        this.setSuffix(mode.getModeAsString());

    }

    public boolean isServerBot(Entity entity) {
        if(mode.getValue() == mods.Basic){
            if (this.isEnabled()) {
                if (Helper.onServer("hypixel")) {
                    if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                        return false;
                    }
                    return true;
                }
                if (Helper.onServer("mineplex")) {
                    for (Object object : this.mc.theWorld.playerEntities) {
                        EntityPlayer entityPlayer = (EntityPlayer)object;
                        if (entityPlayer == null || entityPlayer == this.mc.thePlayer || !entityPlayer.getName().startsWith("Body #") && entityPlayer.getMaxHealth() != 20.0f) continue;
                        return true;
                    }
                }
            }
        }else if(mode.getValue() == mods.Hypixel){
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                return false;
            }
            return true;
        }else if(mode.getValue() == mods.CubeCraft){
            if(!this.isEnabled()) return false;
            if(entity.getDisplayName().getUnformattedText().startsWith("\247")) {
                return false;
            }
            return false;
        }
        return false;
    }
}

