/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.combat;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import tomorrow.tomo.Client;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketSend;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.movement.Speed;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;

public class Criticals
extends Module {
    private Mode mode = new Mode("Mode", "mode", (Enum[])CritMode.values(), (Enum)CritMode.Packet);
    private TimerUtil timer = new TimerUtil();

    public Criticals() {
        super("Criticals", "Always make crits", ModuleType.Combat);
        this.addValues(this.mode);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
    }

    private boolean canCrit() {
        if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof C02PacketUseEntity && this.canCrit() && this.mode.getValue() == CritMode.Minijumps) {
            this.mc.thePlayer.motionY = 0.2;
        }
    }

    void packetCrit() {
        if (this.timer.hasReached(Helper.onServer("hypixel") ? 500 : 10) && this.mode.getValue() == CritMode.Packet && this.canCrit()) {
            double[] offsets = new double[]{0.0625, 0.0, 1.0E-4, 0.0};
            int i = 0;
            while (i < offsets.length) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offsets[i], this.mc.thePlayer.posZ, false));
                ++i;
            }
            this.timer.reset();
        }
    }

    void offsetCrit() {
        if (this.canCrit() && !this.mc.getCurrentServerData().serverIP.contains("hypixel")) {
            double[] offsets = new double[]{0.0624, 0.0, 1.0E-4, 0.0};
            int i = 0;
            while (i < offsets.length) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offsets[i], this.mc.thePlayer.posZ, false));
                ++i;
            }
        }
    }

    public void hypixelCrit() {
        if (this.mode.getValue() == CritMode.Hypixel && this.canCrit()) {
            double[] arrd = new double[]{0.06142999976873398, 0.0, 0.012511000037193298, 0.0};
            int n = arrd.length;
            int n2 = 0;
            while (n2 < n) {
                double offset = arrd[n2];
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, true));
                ++n2;
            }
        }
    }

    static enum CritMode {
        Packet,
        Hypixel,
        Minijumps;
    }

}

