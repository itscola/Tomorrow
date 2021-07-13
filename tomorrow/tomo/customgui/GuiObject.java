package tomorrow.tomo.customgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class GuiObject {
    public float x, y;
    public float width, height;
    public boolean pre = true;
    public float dragX, dragY;
    public String name;
    private boolean drag = false;

    public GuiObject(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void drawObject() {
    }

    public void handleMouse() {

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int mouseX = Mouse.getX() / 2;
        int mouseY = sr.getScaledHeight() - (Mouse.getY() / 2);

        float x1 = sr.getScaledWidth() * x / 100f, y1 = sr.getScaledHeight() * y / 100f;

        if (isHovered(x1, y1, x1 + width, y1 + height, Mouse.getX() / 2, sr.getScaledHeight() - (Mouse.getY() / 2)) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0 && !drag) {
                dragX = (mouseX - x1);
                dragY = (mouseY - y1);
                drag = true;
            }
        }

        if (drag) {
            x = ((float) (mouseX - dragX) / sr.getScaledWidth() * 100f);
            y = ((float) (mouseY - dragY) / sr.getScaledHeight() * 100f);
        }

        if ((dragX != 0 || dragY != 0) && (!Mouse.isButtonDown(0))) {
            dragX = 0;
            dragY = 0;
            drag = false;
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= (x< x2 ? x : x2) && mouseX <= (x< x2 ? x2 : x) && mouseY >= (y< y2 ? y : y2) && mouseY <= (y< y2 ? y2 : y);
    }
}
