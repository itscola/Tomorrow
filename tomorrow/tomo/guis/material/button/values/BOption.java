package tomorrow.tomo.guis.material.button.values;

import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.button.Button;
import tomorrow.tomo.utils.math.AnimationUtils;
import tomorrow.tomo.utils.render.ColorUtils;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;

public class BOption extends Button {
    AnimationUtils au = new AnimationUtils();

    public BOption(float x, float y, Value v) {
        super(x, y, v);
    }

    @Override
    public void draw(float mouseX, float mouseY) {
        super.draw(mouseX, mouseY);
        FontLoaders.arial18.drawString(v.getName(),x+30,y+2,new Color(50,50,50).getRGB());
        if (((boolean) v.getValue())) {
            RenderUtil.drawRoundedRect(x, y + 1, x + 20, y + 9, 2, ColorUtils.lighter(Main.clientColor, 0.3F));
            animation = au.animate(20, animation, 0.05f);
            if (Main.isHovered(x, y + 1, x + 20, y + 9, mouseX, mouseY)) {
                RenderUtil.drawCircle(x + animation - 3.5f, y + 5, 9, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.1f));
            }
//            if(animation != 20) {
//                RenderUtil.drawCircle(x + animation - 3.5f, y + 5, animation / 2, ColorUtils.reAlpha(Main.clientColor.getRGB(), 0.5f));
//            }
            RenderUtil.drawCircle(x + animation - 3, y + 5, 6, new Color(240, 240, 240).getRGB());

        } else {
            RenderUtil.drawRoundedRect(x, y + 1, x + 20, y + 9, 2, new Color(180, 180, 180));
            animation = au.animate(0, animation, 0.05f);
            if (Main.isHovered(x, y, x + 20, y + 10, mouseX, mouseY)) {
                RenderUtil.drawCircle(x + animation - 1.5f, y + 5, 9, new Color(0, 0, 0, 30).getRGB());
            }
            RenderUtil.drawCircle(x + animation - 1, y + 5, 6, new Color(240, 240, 240).getRGB());
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        if (Main.isHovered(x, y, x + 20, y + 10, mouseX, mouseY)) {
            v.setValue(!((boolean) v.getValue()));
        }
    }
}
