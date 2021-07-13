package tomorrow.tomo.mods.modules.render;

import tomorrow.tomo.Client;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.rendering.EventRender2D;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.guis.musicPlayer.MusicPanel;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;

public class MusicPlayer extends Module {
    public static Numbers<Number> x = new Numbers<>("X", "X", 5, 0, 100, 0.1);
    public static Numbers<Number> y = new Numbers<>("Y", "Y", 50, 0, 100, 0.1);

    public MusicPlayer() {
        super("MusicPlayer", ModuleType.Render);
        addValues(x, y);
    }

    @EventHandler
    public void onRender(EventRender2D e) {
        mc.fontRendererObj.drawStringWithShadow("ÕýÔÚ²¥·Å£º" + Client.musicPanel.playing, 100, 100, -1);
    }


}
