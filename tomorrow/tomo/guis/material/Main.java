package tomorrow.tomo.guis.material;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import tomorrow.tomo.guis.material.Tabs.MainTab;
import tomorrow.tomo.guis.material.themes.MainWhite;
import tomorrow.tomo.mods.modules.ClientSettings;
import tomorrow.tomo.utils.render.RenderUtil;
import tomorrow.tomo.utils.render.renderManager.Rect;

import java.awt.*;
import java.util.ArrayList;

public class Main extends GuiScreen {

    public static float windowX, windowY, windowWidth, windowHeight;

    ScaledResolution sr;

    public ArrayList<Tab> tabs = new ArrayList<>();

    public Tab currentTab;

    public Color clientColor;

    public boolean showList;


    @Override
    public void initGui() {
        clientColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
        sr = new ScaledResolution(mc);

        if (sr.getScaledWidth() < 550 && sr.getScaledHeight() < 300) {
            windowWidth = sr.getScaledWidth() - 10;
            windowHeight = sr.getScaledHeight() - 10;
            windowX = (sr.getScaledWidth() - windowWidth) / 2;
            windowY = (sr.getScaledHeight() - windowHeight) / 2;
        }
        if (windowWidth == 0)
            windowWidth = 550;
        if (windowHeight == 0)
            windowHeight = 300;
        if (windowX == 0)
            windowX = (sr.getScaledWidth() - windowWidth) / 2;
        if (windowY == 0)
            windowY = (sr.getScaledHeight() - windowHeight) / 2;
        if (tabs.size() == 0) {
            tabs.add(new MainTab());
        }

        super.initGui();
    }

    //Drag verb
    public float mouseDX, mouseDY;
    public boolean drag;

    public void drawWindow(float mouseX, float mouseY) {

    }

    public void drawBar(float mouseX, float mouseY) {

    }

    //Drag verb
    public float mouseDX2, mouseDY2;
    public boolean drag2;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawWindow(mouseX, mouseY);

        drawBar(mouseX, mouseY);

        if ((isHovered(windowX + windowWidth - 5, windowY + windowHeight - 5, windowX + windowWidth + 5, windowY + windowHeight + 5, mouseX, mouseY) && Mouse.isButtonDown(0)) || drag2) {
            if (windowWidth > 560) {
                windowWidth = mouseX - windowX - mouseDX2;
            } else if ((mouseX - windowX > windowWidth)) {
                windowWidth = mouseX - windowX - mouseDX2;
            }
            if (windowHeight > 310) {
                windowHeight = mouseY - windowY - mouseDY2;
            } else if (mouseY - windowY > windowHeight) {
                windowHeight = mouseY - windowY - mouseDY2;
            }
        }
        if (isHovered(windowX + windowWidth - 5, windowY + windowHeight - 5, windowX + windowWidth + 5, windowY + windowHeight + 5, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            drag2 = true;
            mouseDX2 = mouseX - (windowX + windowWidth);
            mouseDY2 = mouseY - (windowY + windowHeight);
        }
        if (!Mouse.isButtonDown(0)) {
            drag2 = false;
            mouseDX2 = 0;
            mouseDY2 = 0;
        }
        if (drag2 || isHovered(windowX + windowWidth - 5, windowY + windowHeight - 5, windowX + windowWidth + 5, windowY + windowHeight + 5, mouseX, mouseY)) {
            Mouse.setGrabbed(true);
            RenderUtil.drawCustomImage(mouseX - 4, mouseY - 4, 16, 16, new ResourceLocation("client/clickgui/cur.png"));
        } else {
            if (Mouse.isGrabbed()) {
                Mouse.setCursorPosition(mouseX * 2, (sr.getScaledHeight() - mouseY) * 2);
                Mouse.setGrabbed(false);
            }
        }

        drawList(mouseX, mouseY);
    }

    public void drawList(float mouseX, float mouseY) {

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public static boolean isHovered(float x, float y, float x2, float y2, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
