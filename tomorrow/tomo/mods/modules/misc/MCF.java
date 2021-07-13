/*
 * Decompiled with CFR 0_132.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package tomorrow.tomo.mods.modules.misc;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

import java.awt.Color;

import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@Mod(name = "MidClickFriend", description = ".", type = ModuleType.Misc)
public class MCF
        extends Module {
    private boolean down;

    public MCF() {
        super("MCF", ModuleType.Misc);
    }

    @EventHandler
    private void onClick(EventPreUpdate e) {
        if (Mouse.isButtonDown((int) 2) && !this.down) {
            if (this.mc.objectMouseOver.entityHit != null) {
                EntityPlayer player = (EntityPlayer) this.mc.objectMouseOver.entityHit;
                String playername = player.getName();
                if (!FriendManager.isFriend(playername)) {
                    this.mc.thePlayer.sendChatMessage(".f add " + playername);
                } else {
                    this.mc.thePlayer.sendChatMessage(".f del " + playername);
                }
            }
            this.down = true;
        }
        if (!Mouse.isButtonDown((int) 2)) {
            this.down = false;
        }
    }
}

