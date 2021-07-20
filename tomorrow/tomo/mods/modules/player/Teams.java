package tomorrow.tomo.mods.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import tomorrow.tomo.Client;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class Teams extends Module{

    public Teams(){
        super("Teams", ModuleType.Player);
    }
	public static boolean isOnSameTeam(Entity entity) {
		if(!Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled()) return false;
		if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
            if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
                    || entity.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }
            if(Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
                return true;
            }
        }
		return false;
	}

}
