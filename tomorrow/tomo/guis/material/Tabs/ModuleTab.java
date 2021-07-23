package tomorrow.tomo.guis.material.Tabs;

import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.event.value.Numbers;
import tomorrow.tomo.event.value.Option;
import tomorrow.tomo.event.value.Value;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.Tab;
import tomorrow.tomo.guis.material.button.Button;
import tomorrow.tomo.guis.material.button.values.BMode;
import tomorrow.tomo.guis.material.button.values.BNumbers;
import tomorrow.tomo.guis.material.button.values.BOption;
import tomorrow.tomo.mods.Module;

import java.util.ArrayList;

public class ModuleTab extends Tab {
    public Module module;
    private ArrayList<Button> btns = new ArrayList<>();

    public ModuleTab(Module m) {
        this.module = m;
        name = m.getName();
        for (Value v : module.values) {
            if (v instanceof Option) {
                Button value = new BOption(startX, startY, v,this);
                btns.add(value);

            } else if (v instanceof Numbers) {
                Button value = new BNumbers(startX, startY, v,this);
                btns.add(value);

            } else if (v instanceof Mode) {
                Button value = new BMode(startX, startY, v,this);
                btns.add(value);

            }
        }
    }


    float startX = Main.windowX + 20;
    float startY = Main.windowY + 70;

    public void render(float mouseX, float mouseY) {
        startX = Main.windowX + 20 + Main.animListX;
        startY = Main.windowY + 70;
        for (Button v : btns) {
            if(!v.v.visible)
                continue;
            v.x = startX;
            v.y = startY;
            v.draw(mouseX, mouseY);
            if (startX + 100 + FontLoaders.arial18.getStringWidth(v.v.getName()) < Main.windowX + Main.windowWidth) {
                if (v instanceof BOption) {
                    startX += 40 + FontLoaders.arial18.getStringWidth(v.v.getName());
                } else {
                    startX += 80;
                }
            } else {
                startX = Main.windowX + 20 + Main.animListX;
                startY += 30;
            }
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
        startX = Main.windowX + 20 + Main.animListX;
        startY = Main.windowY + 70;
        for (Button v : btns) {
            if(!v.v.visible)
                continue;
            v.mouseClick(mouseX, mouseY);
        }
    }
}
