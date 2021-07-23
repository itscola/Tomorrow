package tomorrow.tomo.guis.material.button;

import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.Tab;
import tomorrow.tomo.utils.math.AnimationUtils;

public class Button {
    public float x, y;
    public Value v;
    public float animation;
    public Runnable event;
    public boolean drag;

    public AnimationUtils animationUtils = new AnimationUtils();
    public Tab tab;

    public Button(float x, float y, Value v, Tab moduleTab) {
        this.x = x;
        this.y = y;
        this.v = v;
        this.tab = moduleTab;
    }


    public void drawButton(float mouseX, float mouseY) {

    }

    public void draw(float mouseX, float mouseY) {
        drawButton(mouseX, mouseY);
    }

    public void mouseClicked(float mouseX, float mouseY) {

    }

    public void mouseClick(float mouseX, float mouseY) {
        if (Main.currentTab != this.tab) {
            return;
        }

        mouseClicked(mouseX, mouseY);
    }
}
