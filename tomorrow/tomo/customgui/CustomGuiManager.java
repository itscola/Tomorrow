package tomorrow.tomo.customgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.objects.ArrayListObject;
import tomorrow.tomo.customgui.objects.StringObject;
import tomorrow.tomo.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class CustomGuiManager extends Gui {

    public static ArrayList<GuiObject> objects = new ArrayList<>();

    public static ArrayList<GuiObject> getObjects() {
        return objects;
    }

    public static void setObjects(ArrayList<GuiObject> objects) {
        CustomGuiManager.objects = objects;
    }

    public static void addObject(GuiObject o) {
        objects.add(o);
    }

    public static void removeObject(GuiObject o) {
        objects.remove(o);
    }


    public static void drawGuiPre() {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        for (GuiObject guiObject : objects) {
            float x1 = sr.getScaledWidth() * guiObject.x / 100, y1 = sr.getScaledHeight() * guiObject.y / 100f;
            if (guiObject.pre) {

                if (Client.instance.mc.currentScreen != null) {
                    guiObject.handleMouse();
                    if (GuiObject.isHovered(x1, y1, x1 + guiObject.width, y1 + guiObject.height, Mouse.getX() / 2, sr.getScaledHeight() - (Mouse.getY() / 2))) {

                        if (guiObject.dragX != 0 && guiObject.dragY != 0) {
                            RenderUtil.drawBorderedRect(x1 - 1, y1 - 1, x1 - 1 + guiObject.width + 2f, y1 - 1 + guiObject.height + 2, 1, new Color(0, 0, 0, 173).getRGB(), new Color(255, 255, 255, 100).getRGB());
                        } else {
                            RenderUtil.drawBorderedRect(x1 - 1, y1 - 1, x1 - 1 + guiObject.width + 2f, y1 - 1 + guiObject.height + 2, 1, new Color(0, 0, 0, 173).getRGB(), new Color(255, 255, 255, 17).getRGB());
                        }
                    }
                }
                if (guiObject instanceof ArrayListObject) {
                    ArrayListObject obj = (ArrayListObject) guiObject;
                    obj.drawObject();
                } else if (guiObject instanceof StringObject) {
                    StringObject obj = (StringObject) guiObject;
                    obj.drawObject();
                }

                if (Client.instance.mc.currentScreen != null) {
                    guiObject.handleMouse();
                    if (guiObject.dragX != 0 && guiObject.dragY != 0) {
                        float mouseX = Mouse.getX() / 2;
                        float mouseY = sr.getScaledHeight() - (Mouse.getY() / 2);
                        RenderUtil.drawBorderedRect(mouseX, mouseY, mouseX + 4 + Client.fontLoaders.msFont18.getStringWidth("x:" + (int) (guiObject.x * 10) / 10f + " y:" + (int) (guiObject.y * 10) / 10f) + 2, mouseY + Client.fontLoaders.msFont18.getStringHeight("AbcD"), 1, new Color(200, 200, 200, 255).getRGB(), new Color(23, 23, 23, 255).getRGB());
                        Client.fontLoaders.msFont18.drawStringWithShadow("x:" + (int) (guiObject.x * 10) / 10f + " y:" + (int) (guiObject.y * 10) / 10f, mouseX + 1, mouseY + 2, -1);

                    }
                }
            }
        }
    }


    public static void drawGuiPost() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        for (GuiObject guiObject : objects) {
            float x1 = sr.getScaledWidth() * guiObject.x / 100, y1 = sr.getScaledHeight() * guiObject.y / 100f;
            if (!guiObject.pre) {

                if (Client.instance.mc.currentScreen != null) {
                    guiObject.handleMouse();
                    if (GuiObject.isHovered(x1, y1, x1 + guiObject.width, y1 + guiObject.height, Mouse.getX() / 2, sr.getScaledHeight() - (Mouse.getY() / 2))) {

                        if (guiObject.dragX != 0 && guiObject.dragY != 0) {
                            RenderUtil.drawBorderedRect(x1 - 1, y1 - 1, x1 - 1 + guiObject.width + 2f, y1 - 1 + guiObject.height + 2, 1, new Color(0, 0, 0, 173).getRGB(), new Color(255, 255, 255, 100).getRGB());
                        } else {
                            RenderUtil.drawBorderedRect(x1 - 1, y1 - 1, x1 - 1 + guiObject.width + 2f, y1 - 1 + guiObject.height + 2, 1, new Color(0, 0, 0, 173).getRGB(), new Color(255, 255, 255, 17).getRGB());
                        }
                    }
                }
                if (guiObject instanceof ArrayListObject) {
                    ArrayListObject obj = (ArrayListObject) guiObject;
                    obj.drawObject();
                } else if (guiObject instanceof StringObject) {
                    StringObject obj = (StringObject) guiObject;
                    obj.drawObject();
                }

                if (Client.instance.mc.currentScreen != null) {
                    guiObject.handleMouse();
                    if (guiObject.dragX != 0 && guiObject.dragY != 0) {
                        float mouseX = Mouse.getX() / 2;
                        float mouseY = sr.getScaledHeight() - (Mouse.getY() / 2);
                        RenderUtil.drawBorderedRect(mouseX, mouseY, mouseX + 4 + Client.fontLoaders.msFont18.getStringWidth("x:" + (int) (guiObject.x * 10) / 10f + " y:" + (int) (guiObject.y * 10) / 10f) + 2, mouseY + Client.fontLoaders.msFont18.getStringHeight("AbcD"), 1, new Color(200, 200, 200, 255).getRGB(), new Color(23, 23, 23, 255).getRGB());
                        Client.fontLoaders.msFont18.drawStringWithShadow("x:" + (int) (guiObject.x * 10) / 10f + " y:" + (int) (guiObject.y * 10) / 10f, mouseX + 1, mouseY + 2, -1);

                    }
                }
            }
        }
    }

    public GuiObject getByName(String name) {
        for (GuiObject m : objects) {
            if (!m.name.equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }
}
