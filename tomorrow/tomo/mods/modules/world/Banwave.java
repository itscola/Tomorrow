/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.world;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

import java.util.ArrayList;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
@Mod(name = "BanEveryone",description = "." , type = ModuleType.World)
public class Banwave
extends Module {
    private TimerUtil timer = new TimerUtil();
    public ArrayList<Entity> banned;
    private String banMessage = " bannnnnn!";
    private Option<Boolean> tempBan = new Option<Boolean>("Temp Ban", "temp", false);
    private Numbers<Double> banDelay = new Numbers<Double>("Delay", "delay", 10.0, 1.0, 20.0, 1.0);

    public Banwave() {
        super("Ban everyone", ModuleType.Render);
        this.banned = new ArrayList();
        this.addValues(this.tempBan, this.banDelay);
    }

    @Override
    public void onEnable() {
        this.banned.clear();
        super.onEnable();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        for (Object o : this.mc.theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityOtherPlayerMP)) continue;
            EntityOtherPlayerMP e = (EntityOtherPlayerMP)o;
            if (!this.timer.hasReached(this.banDelay.getValue() * 100.0) || FriendManager.isFriend(e.getName()) || e.getName() == this.mc.thePlayer.getName() || this.banned.contains(e)) continue;
            if (this.tempBan.getValue().booleanValue()) {
                this.mc.thePlayer.sendChatMessage("/tempban " + e.getName() + " 7d" + " " + this.banMessage);
                System.out.println("/tempban " + e.getName() + " 7d" + " " + this.banMessage);
            } else {
                this.mc.thePlayer.sendChatMessage("/ban " + e.getName() + " " + this.banMessage);
                System.out.println("/ban " + e.getName() + " " + this.banMessage);
            }
            this.banned.add(e);
            this.timer.reset();
        }
    }
}

