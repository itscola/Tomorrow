package tomorrow.tomo.utils.cheats.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.GuiObject;
import tomorrow.tomo.customgui.objects.ArrayListObject;
import tomorrow.tomo.customgui.objects.StringObject;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.managers.config.ModuleConfig;
import tomorrow.tomo.managers.config.GuiSettings;
import tomorrow.tomo.managers.config.Settings;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.utils.misc.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JsonUtil {
    public static Settings modules = new Settings();
    public static GuiSettings guiSettings = new GuiSettings();

    public static void saveConfig() {
        saveConfig("");
    }

    public static void saveConfig(String prefix) {
        modules.settings = new ArrayList<>();
        guiSettings.settings = new ArrayList<>();

        modules.init();
        guiSettings.init();
        Gson gson = new GsonBuilder().setVersion(1.1).setPrettyPrinting().create();
        String f = gson.toJson(modules);
        String gui = gson.toJson(guiSettings);


        String configFiles = Client.configFolder.getAbsolutePath();
        File configFile = new File(configFiles + "\\" + prefix);

        String dir = configFile.getAbsolutePath();

        if (!Client.dataFolder.exists()) {
            if (!configFile.mkdir()) {
            }
        }

        if (!configFile.exists()) {
            if (!configFile.mkdir()) {
            }
        }
        File file = new File(dir, "config.json");
        File guiFile = new File(dir, "gui.json");

        try {
            if (!guiFile.exists()) {
                guiFile.createNewFile();
            }
            FileWriter fw = new FileWriter(guiFile, false);
            BufferedWriter bf = new BufferedWriter(fw);
            bf.write(gui);
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bf = new BufferedWriter(fw);
            bf.write(f);
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved Config");
    }

    public static void load() {
        load("");
    }

    public static void load(String prefix) {
        Gson gson = new Gson();

        File dir = new File(Client.configFolder.getAbsolutePath(),prefix);

        if (!dir.exists()) {
            dir.mkdir();
            return;
        }
        File file = new File(dir + prefix, "config.json");

        Settings mods = new Settings();
        mods.init();
//        System.out.println(FileUtil.readFile(dir.getAbsolutePath(), "config.json"));
        if (!FileUtil.readFile(dir.getAbsolutePath(), "config.json").isEmpty()) {
            mods = gson.fromJson(FileUtil.readFile(dir.getAbsolutePath(), "config.json"), Settings.class);
//        System.out.println(mods.settings.size());
            for (ModuleConfig modConfig : mods.settings) {
                Module m = Client.instance.getModuleManager().getModuleByName(modConfig.name);
                if(m == null)
                    continue;
                if (modConfig.enable != m.isEnabled()) {
                    m.setEnabledWithoutInfo(!m.isEnabled());
                }
                m.setKey(modConfig.bind);

//                modConfig.values.forEach(value -> {
//                    System.out.println(value.getName());
//                    System.out.println(value.getValue());
//
//                });

                for (Value value : m.values) {
                    if (value instanceof Option) {
                        for (Value v : modConfig.values) {
                            if (v.getName().equals(value.getName())) {
                                value.setValue(v.getValue());
                            }
                        }
                    }
                    if (value instanceof Numbers) {
                        for (Value v : modConfig.values) {
                            if (v.getName().equals(value.getName())) {
                                value.setValue(v.getValue());
                            }
                        }
                    }
                    if (value instanceof Mode) {
                        for (Value v : modConfig.values) {
                            if (v.getName().equals(value.getName())) {
                                ((Mode<?>) value).setMode((String) v.getValue());
                            }
                        }
                    }
                }
            }
        }

        if (!FileUtil.readFile(dir.getAbsolutePath(), "gui.json").isEmpty()) {
            guiSettings = gson.fromJson(FileUtil.readFile(dir.getAbsolutePath(), "gui.json"), GuiSettings.class);
            Client.instance.customgui.objects.clear();
            for (GuiObject go : guiSettings.settings) {
                if (go.name.toLowerCase().contains("arraylist")) {
                    Client.instance.customgui.objects.add(new ArrayListObject(go.name, go.x, go.y));
                } else if (go.name.toLowerCase().contains("string")) {
                    Client.instance.customgui.objects.add(new StringObject(go.name, ((StringObject) go).content, go.x, go.y, ((StringObject) go).red, ((StringObject) go).green, ((StringObject) go).blue));
                }
            }
        }


        System.out.println("Loaded Config");

    }

}
