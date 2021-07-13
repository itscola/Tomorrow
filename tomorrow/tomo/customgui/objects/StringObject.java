package tomorrow.tomo.customgui.objects;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.GuiObject;

import java.awt.*;

public class StringObject extends GuiObject {
    public String content = "";
    public int red;
    public int green;
    public int blue;


    public StringObject(String name, String content, float x, float y, int red, int green, int blue) {
        super(name, x, y);
        this.content = content;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    @Override
    public void drawObject() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float x1 = sr.getScaledWidth() * x / 100, y1 = sr.getScaledHeight() * y / 100;

        Client.fontLoaders.msFont18.drawStringWithShadow(content, x1, y1, new Color(red, green, blue).getRGB());

        super.width = Client.fontLoaders.msFont18.getStringWidth(content);
        super.height = Client.fontLoaders.msFont18.getStringHeight(content);

    }

}
