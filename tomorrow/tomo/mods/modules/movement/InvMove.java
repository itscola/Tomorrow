package tomorrow.tomo.mods.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.guis.material.themes.Classic;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class InvMove extends Module{

	public InvMove(){
		super("InvMove", ModuleType.Movement);
	}

	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		if (this.mc.currentScreen instanceof Classic) {
			KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
			KeyBinding[] array;
			for (int length = (array = key).length, i = 0; i < length; ++i) {
				KeyBinding b = array[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
		}
	}
}
