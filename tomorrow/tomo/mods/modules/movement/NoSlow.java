/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.movement;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.player.Helper;

public class NoSlow
        extends Module {
    public static Option noAttackSlow = new Option("NoAttackSlow", true);

    public NoSlow() {
        super("NoSlow", ModuleType.Movement);
        addValues(noAttackSlow);
    }


    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (!(!this.mc.thePlayer.isBlocking() || Helper.onServer("invaded") || Helper.onServer("hypixel") || Helper.onServer("faithful") || Helper.onServer("mineman"))) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}

