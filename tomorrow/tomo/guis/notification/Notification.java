package tomorrow.tomo.guis.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.AnimationUtils;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;

public class Notification {
    public float x;
    public float width, height;
    public String name;
    public float lastTime;
    public TimerUtil timer;
    public Type type;
    public boolean setBack;
    private float fy, cy = 0;
    private TimerUtil anitimer = new TimerUtil();

    public Notification(String name, Type type) {
        this.name = name;
        this.type = type;
        this.lastTime = 1.5f;
        this.width = FontLoaders.arial16.getStringWidth(name) + 25;
        this.height = 20;
    }

    public Notification(String name, Type type, float lastTime) {
        this.name = name;
        this.type = type;
        this.lastTime = lastTime;
        this.width = FontLoaders.arial16.getStringWidth(name) + 25;
        this.height = 20;

    }

    public void render(float y) {
        fy = y;
        if (cy == 0) {
            cy = fy + 25;
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x), cy, (float) ((sr.getScaledWidth_double() - x) + width), cy + height, new Color(10, 10, 10, 220));
        RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x), cy + height - 1, (float) (sr.getScaledWidth_double() - x) + width, cy + height, new Color(180, 180, 180));
        if (timer != null) {
            RenderUtil.drawRect((float) (sr.getScaledWidth_double() - x), cy + height - 1, (float) ((sr.getScaledWidth_double() - x) + (this.timer.getTime() - timer.lastMS) / (lastTime * 1000) * width), cy + height, new Color(0, 110, 255));
        }
//        RenderUtil.drawCustomImageAlpha(sr.getScaledWidth() - x + 3, cy + 4, 12, 12, new ResourceLocation("client/" + type.name() + ".png"),-1,255);

        FontLoaders.arial16.drawString(name, (sr.getScaledWidth() - x) + 18, cy + 7, -1);
    }

    public void update() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (timer == null && Math.abs(x - width) < 0.1f) {
            timer = new TimerUtil();
            timer.reset();
        }
        if (anitimer.delay(10)) {
            cy = AnimationUtils.animate(fy, cy, 0.1f);

            if (!setBack) {
                x = AnimationUtils.animate(width, x, 0.1f);
            } else {
                x = AnimationUtils.animate(0, x, 0.1f);
            }
            anitimer.reset();
        }
    }

    public enum Type {
        Info,
        Alert,
        Error
    }

}
