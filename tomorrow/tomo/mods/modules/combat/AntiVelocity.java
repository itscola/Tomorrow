package tomorrow.tomo.mods.modules.combat;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPacketRecieve;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import java.awt.Color;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;


@Mod(name = "Velocity",description = "Anti knock back." , type = ModuleType.Combat)
public class AntiVelocity
extends Module {
    private Numbers<Double> percentage = new Numbers<Double>("Percentage", "percentage", 0.0, 0.0, 100.0, 5.0);

    public AntiVelocity() {
        super("Velocity", "Anti knock back.", ModuleType.Combat);
        this.addValues(this.percentage);
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if (this.percentage.getValue().equals(0.0)) {
                e.setCancelled(true);
            } else {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                packet.motionX = (int)(this.percentage.getValue() / 100.0);
                packet.motionY = (int)(this.percentage.getValue() / 100.0);
                packet.motionZ = (int)(this.percentage.getValue() / 100.0);
            }
        }
    }
}

