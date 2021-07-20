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
        FontLoaders.arial24.drawString("Welcome!", Main.windowX + Main.animListX + 10, Main.windowY + 60, new Color(150, 150, 150).getRGB());
        float width = FontLoaders.arial24.getStringWidth("Welcome!");

        FontLoaders.arial16B.drawString(Client.username, Main.windowX + Main.animListX + 12 + width, Main.windowY + 60, new Color(200, 200, 200).getRGB());


    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
}
