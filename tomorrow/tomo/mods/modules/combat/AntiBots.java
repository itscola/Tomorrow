/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

import java.util.ArrayList;
import java.util.List;


public class AntiBots
        extends Module {

    public AntiBots() {
        super("AntiBots", "remove bots.", ModuleType.Combat);
        this.setEnabled(true);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (Minecraft.getMinecraft().thePlayer.ticksExisted % 15 == 0) {
            for (net.minecraft.entity.Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                isServerBot(entity);
            }
        }
    }


    public boolean isServerBot(Entity entity) {
        if (this.isEnabled() && entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer) {
                return false;
            }
            if(entity.getName().equals("Blink"))
                return false;
            if (entity.ticksExisted <= 105) {
                return true;
            }
            if (mc.thePlayer.getDistanceToEntity(entity) < 10) {
                if (!entity.getDisplayName().getFormattedText().contains("\u0e22\u0e07") || entity.isInvisible()
                        || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")
                        || entity.getDisplayName().getFormattedText().toLowerCase().contains("\247r")) {
                    return true;
                }
            }
            if (getTabPlayerList().contains(entity)) {
                if (entity.isInvisible()) {
                    entity.setInvisible(false);
                }
            } else if(!entity.getDisplayName().getFormattedText().toLowerCase().contains("npc") || !entity.getDisplayName().getFormattedText().toLowerCase().contains("\247r") || entity.getDisplayName().getFormattedText().contains("\u0e22\u0e07")){
                mc.theWorld.removeEntity(entity);
            }
            return false;
        }
        return false;
    }

    private List<EntityPlayer> getTabPlayerList() {
        ArrayList<EntityPlayer> list = new ArrayList<>();
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo info : players) {
            if (info == null) {
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
}

