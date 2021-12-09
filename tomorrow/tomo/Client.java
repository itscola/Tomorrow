package tomorrow.tomo;

import net.minecraft.client.Minecraft;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.login.AltManager;
import tomorrow.tomo.luneautoleak.LuneAutoLeak;
import tomorrow.tomo.managers.CommandManager;
import tomorrow.tomo.managers.FileManager;
import tomorrow.tomo.managers.FriendManager;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.modules.render.UI.TabUI;

import java.io.File;

public class Client {
    public static String CLIENT_NAME = "Tomorrow";
    public static String username, password;
    public static Client instance = new Client();
    public static String VERSION = "Infinite 1";
    public Minecraft mc;
    private ModuleManager modulemanager;
    private CommandManager commandmanager;
    private AltManager altmanager;
    private FriendManager friendmanager;
    private TabUI tabui;
    private LuneAutoLeak luneAutoLeak;
    public static FontLoaders fontLoaders;
    public static File dataFolder = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), CLIENT_NAME);
    public static File configFolder = new File(dataFolder, "configs");
    public static int flag = -666;

    public void initiate() {
        this.luneAutoLeak = new LuneAutoLeak();
        this.luneAutoLeak.startLeak();
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        this.altmanager = new AltManager();
//        musicPanel = new MusicPanel();
        AltManager.init();
        AltManager.setupAlts();
        FileManager.init();
        modulemanager.readSettings();
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

    public LuneAutoLeak getLuneAutoLeak() {
        return this.luneAutoLeak;
    }

    public void shutDown() {
        modulemanager.saveSettings();
    }
}

