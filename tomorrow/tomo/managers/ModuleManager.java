package tomorrow.tomo.managers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import tomorrow.tomo.event.EventBus;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.misc.EventKey;
import tomorrow.tomo.event.events.rendering.EventRender2D;
import tomorrow.tomo.event.events.rendering.EventRender3D;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.mods.modules.ClientSettings;
import tomorrow.tomo.mods.modules.combat.*;
import tomorrow.tomo.mods.modules.misc.IRC;
import tomorrow.tomo.mods.modules.player.*;
import tomorrow.tomo.mods.modules.world.*;
import tomorrow.tomo.mods.modules.render.*;
import tomorrow.tomo.mods.modules.movement.*;
import tomorrow.tomo.mods.modules.misc.*;


import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.irc.Client;
import tomorrow.tomo.utils.misc.ClassUtils;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.render.gl.GLUtils;

public class ModuleManager
        implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    public static List<Module> enabledModules = new ArrayList<Module>();


    private boolean enabledNeededMod = true;

    @Override
    public void init() {

//        for (Class c : ClassUtils.getClassSet("")) {
//            if (c.getAnnotation(Mod.class) != null) {
//                try {
//                    Module m = (Module) c.newInstance();
//                    m.modName = ((Mod) c.getAnnotation(Mod.class)).name();
//                    m.description = (((Mod) c.getAnnotation(Mod.class)).description());
//                    m.type = ((Mod) c.getAnnotation(Mod.class)).type();
//                    modules.add(m);
//
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        //Combat
        modules.add(new Killaura());
        modules.add(new Criticals());
        modules.add(new AntiVelocity());
        modules.add(new Regen());
        modules.add(new AutoArmor());
        modules.add(new AutoHeal());
        modules.add(new FastBow());
        modules.add(new BowAimBot());
        modules.add(new AntiBots());
        modules.add(new TpAura());

        //Render
        modules.add(new HUD());
        modules.add(new ESP());
        modules.add(new ClickGui());
        modules.add(new FullBright());
        modules.add(new Nametags());
        modules.add(new Tracers());
        modules.add(new NoRender());
        modules.add(new Chams());
        modules.add(new TargetHud());
        modules.add(new ItemPhysic());
        modules.add(new ScreenRader());
        modules.add(new DamageParticle());
        modules.add(new ChunkAnimator());
        modules.add(new MusicPlayer());


        //Player
        modules.add(new FastUse());
        modules.add(new InvCleaner());
        modules.add(new Teams());
        modules.add(new AutoTools());


        //Movement
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Blink());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new NoFall());
        modules.add(new InvMove());
        modules.add(new Jesus());
        modules.add(new AntiVoid());
        modules.add(new Longjump());
        modules.add(new SafeWalk());
        modules.add(new NoSlow());


        //World
        modules.add(new ChestStealer());
        modules.add(new Scaffold());
        modules.add(new Phase());
        modules.add(new FastPlace());

        //Ghost
        modules.add(new AutoAccept());
        modules.add(new Freecam());

        //Misc
        modules.add(new MCF());
        modules.add(new PingSpoof());
        modules.add(new AntiAim());
        modules.add(new IRC());
        modules.add(new NoRotate());
        modules.add(new ClientSettings());
        modules.add(new AutoGG());



//		this.readSettings();ss
        EventBus.getInstance().register(this);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls)
                continue;
            return m;
        }
        System.out.println("一个功能没有获取到:" + cls.getName());
        return modules.get(0);
    }


    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t) continue;
            output.add(m);
        }
        return output;
    }

    @EventHandler
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    @EventHandler
    private void onGLHack(EventRender3D e) {
        GlStateManager.getFloat(2982, (FloatBuffer) GLUtils.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer) GLUtils.PROJECTION.clear());
        GlStateManager.glGetInteger(2978, (IntBuffer) GLUtils.VIEWPORT.clear());
    }

    TimerUtil timerUtil = new TimerUtil();

    @EventHandler
    private void on2DRender(EventRender2D e) {
//        if(timerUtil.delay(15000)) {
//            try {
//                Client.sendMessage("E");
//            } catch (Exception ex) {
//                System.out.println("Connect irc server");
//                Client.connect(tomorrow.tomo.Client.username, Helper.mc.thePlayer.getName());
//            }
//            timerUtil.reset();
//        }

//        if(Helper.mc.thePlayer != null && (Client.timerUtil.delay(10000))){
//            Helper.sendMessage("\2474[IRC] Connecting...");
//            Client.connect(Tomorrow.username, Helper.mc.thePlayer.getName());
//            Client.timerUtil.reset();
//        }
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabled(true);
            }
        }
    }

    private void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String) bind.toUpperCase()));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) continue;
            m.enabledOnStartup = true;
        }
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode) value).setMode(v.split(":")[2]);
            }
        }
    }
}


