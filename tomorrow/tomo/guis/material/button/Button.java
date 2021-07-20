package tomorrow.tomo.guis.material.button;

import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.utils.math.AnimationUtils;

public class Button {
    public float x, y;
    public Value v;
    public float animation;
    public Runnable event;
    public boolean drag;

    public AnimationUtils animationUtils = new AnimationUtils();

    public Button(float x, float y, Value v) {
        this.x = x;
        this.y = y;
        this.v = v;
    }


    public void drawButton(float mouseX, float mouseY) {

    }

    public void draw(float mouseX, float mouseY) {
        drawButton(mouseX, mouseY);
    }

    public void mouseClicked(float mouseX, float mouseY) {

    }

}
