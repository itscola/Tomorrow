/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventPostRenderPlayer;
import tomorrow.tomo.event.events.rendering.EventPreRenderPlayer;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
@Mod(name = "Chams",description = "." , type = ModuleType.Render)

public class Chams
extends Module {
    public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])ChamsMode.values(), (Enum)ChamsMode.Textured);

    public Chams() {
        super("Chams", ModuleType.Render);

        this.addValues(this.mode);
    }

    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e) {
        GL11.glEnable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
    }

    @EventHandler
    private void postRenderPlayer(EventPostRenderPlayer e) {
        GL11.glDisable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);
    }

    public static enum ChamsMode {
        Textured,
        Normal;
    }

}

