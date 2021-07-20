/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRenderCape;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class Capes
extends Module {
    public Capes(){
        super("Capes", ModuleType.Render);
    }
    @EventHandler
    public void onRender(EventRenderCape event) {
        if (this.mc.theWorld != null && FriendManager.isFriend(event.getPlayer().getName())) {
            // TODO: 2021/5/20 FIX CAPE 
//            event.setLocation(Tomorrow.CLIENT_CAPE);
            event.setCancelled(true);
        }
    }
}

