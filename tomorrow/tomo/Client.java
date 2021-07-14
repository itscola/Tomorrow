/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo;

import net.minecraft.client.Minecraft;
import tomorrow.tomo.customgui.CustomGuiManager;
import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.musicPlayer.MusicPanel;
import tomorrow.tomo.luneautoleak.LuneAutoLeak;
import tomorrow.tomo.managers.CommandManager;
import tomorrow.tomo.managers.FileManager;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.modules.render.UI.TabUI;
import tomorrow.tomo.guis.login.AltManager;
import tomorrow.tomo.utils.cheats.misc.JsonUtil;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.irc.packets.clientside.ClientConnectPacket;
import tomorrow.tomo.utils.math.MathUtil;

import java.io.File;

public class Client {
    public static String CLIENT_NAME = "Tomo";
    public static String username ;
    public static Client instance = new Client();
    public static String VERSION = "r 1.1";
    public static boolean publicMode = false;
    public Minecraft mc;
    private ModuleManager modulemanager;
    private CommandManager commandmanager;
    private AltManager altmanager;
    private FriendManager friendmanager;
    private TabUI tabui;
    public static FontLoaders fontLoaders;
    public static File dataFolder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), CLIENT_NAME);
    public static File configFolder = new File(dataFolder, "configs");
    public CustomGuiManager customgui;
//    public static MusicPanel musicPanel;
    public static int flag = -666;

    public void initiate() {
        new LuneAutoLeak().startLeak();
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        this.altmanager = new AltManager();
        this.customgui = new CustomGuiManager();
//        musicPanel = new MusicPanel();
        AltManager.init();
        AltManager.setupAlts();
        FileManager.init();
        JsonUtil.load();
        mc = Minecraft.getMinecraft();
//        try {
//            new ViaFabric().onInitialize();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public ModuleManager getModuleManager() {
        return this.modulemanager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public AltManager getAltManager() {
        return this.altmanager;
    }

    public void shutDown() {
//        tomorrow.tomo.utils.irc.Client.sendMessage(new ClientConnectPacket());
        String values = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }
}

