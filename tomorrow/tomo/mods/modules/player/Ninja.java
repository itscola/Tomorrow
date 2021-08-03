package tomorrow.tomo.mods.modules.player;

import net.minecraft.item.ItemBow;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.math.MathUtil;

public class Ninja extends Module {
    public Ninja() {
        super("Ninja", ModuleType.Misc);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.keyBindSneak.pressed = false;
        mc.gameSettings.keyBindUseItem.pressed = false;

    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        mc.gameSettings.keyBindSneak.pressed = true;
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            mc.gameSettings.keyBindUseItem.pressed = true;
            e.setPitch(190);
            mc.thePlayer.setSpeed(MathUtil.getBaseMovementSpeed());
        }

    }
}
