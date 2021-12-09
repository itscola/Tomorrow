package tomorrow.tomo.guis.material.Tabs;

import tomorrow.tomo.Client;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.Tab;

import java.awt.*;

public class MainTab extends Tab {
    public MainTab() {
        name = "Main Menu";
    }

    @Override
    public void render(float mouseX, float mouseY) {
        super.render(mouseX, mouseY);
        FontLoaders.arial24.drawString("Welcome!", Main.windowX + Main.animListX + 50, Main.windowY + 100, new Color(50, 50, 50).getRGB());
        float width = FontLoaders.arial24.getStringWidth("Welcome!");
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
}
