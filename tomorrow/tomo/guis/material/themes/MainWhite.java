package tomorrow.tomo.guis.material.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import tomorrow.tomo.Client;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.guis.material.Category;
import tomorrow.tomo.guis.material.Main;
import tomorrow.tomo.guis.material.Tabs.ModuleTab;
import tomorrow.tomo.guis.material.button.CButton;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.math.AnimationUtils;
import tomorrow.tomo.utils.render.RenderUtil;
import tomorrow.tomo.utils.render.renderManager.Rect;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainWhite extends Main {

    private CButton Blist, Btheme, Bsettings;
    float mouseX, mouseY;

    ArrayList<Category> categories = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
        Blist = new CButton("Modules", "client/clickgui/modules.png", 2, 4, 12, 8);
        Btheme = new CButton("Modules", "client/clickgui/theme.png", 3, 3, 11, 11);
        Bsettings = new CButton("Modules", "client/clickgui/settings.png", 3, 3, 10.5f, 10.5f);
        categories.clear();
        for (ModuleType mt : ModuleType.values()) {
            categories.add(new Category(mt, 0, false));
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (!isHovered(windowX + windowWidth - 5, windowY + windowHeight - 5, windowX + windowWidth + 5, windowY + windowHeight + 5, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            drag2 = false;
            mouseDX2 = 0;
            mouseDY2 = 0;
        }


        Blist.onMouseClicked(mouseX, mouseY);
        Btheme.onMouseClicked(mouseX, mouseY);
        Bsettings.onMouseClicked(mouseX, mouseY);

        float modsY = windowY + 35 + listRoll;

        for (Category mt : categories) {
            if (mt.show) {
                new Rect(windowX, modsY, animListX, 20, new Color(255, 255, 255), new Runnable() {
                    @Override
                    public void run() {
                        if (Mouse.isButtonDown(0)) {
                            if (!mt.show) {
                                mt.show = true;
                            } else {
                                mt.needRemove = !mt.needRemove;
                            }
                        }
                    }
                }).render(mouseX, mouseY);
                modsY += 25;
                for (Module m : Client.instance.getModuleManager().getModulesInType(mt.moduleType)) {
                    new Rect(windowX, modsY, animListX, 15, new Color(255, 255, 255), new Runnable() {
                        @Override
                        public void run() {
                            if (Mouse.isButtonDown(0)) {
                                m.setEnabled(!m.isEnabled());
                            } else if (Mouse.isButtonDown(1)) {
                                ModuleTab modT = new ModuleTab(m);
                                tabs.add(modT);
                                currentTab = modT;
                            }
                        }
                    }).render(mouseX, mouseY);
                    FontLoaders.arial18.drawString(m.getName(), windowX + animListX - 120, modsY + 5, new Color(50, 50, 50).getRGB());
                    modsY += 20;
                }

            } else {
                new Rect(windowX, modsY, animListX, 20, new Color(255, 255, 255), new Runnable() {
                    @Override
                    public void run() {
                        if (Mouse.isButtonDown(0)) {
                            mt.show = !mt.show;
                        }
                    }
                }).render(mouseX, mouseY);

            }

            modsY += 25;
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void drawBar(float mouseX, float mouseY) {
        Blist.onRender(windowX + 8, windowY + 8, mouseX, mouseY);
        Btheme.onRender(windowX + windowWidth - 20, windowY + 8, mouseX, mouseY);
        Bsettings.onRender(windowX + windowWidth - 40, windowY + 8, mouseX, mouseY);
    }

    static float animListX;
    float listRoll2 = 0;
    float listRoll = 0;

    static AnimationUtils listAnim = new AnimationUtils();
    static AnimationUtils rollAnim = new AnimationUtils();


    @Override
    public void drawList(float mouseX, float mouseY) {
        super.drawList(mouseX, mouseY);
        if (Blist.realized) {
            animListX = listAnim.animate(140, animListX, 0.1f);
        } else {
            animListX = listAnim.animate(0, animListX, 0.1f);
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor(windowX, windowY + 30, windowWidth, (windowHeight - 30));
//        GL11.glScissor((int)windowX, mc.displayHeight - (int)(windowY + windowHeight), (int)windowWidth, (int)windowHeight);
        if (animListX != 0) {
            RenderUtil.drawRect(windowX, windowY + 30, windowX + animListX, windowY + windowHeight, new Color(255, 255, 255));
            RenderUtil.drawGradientSideways(windowX + animListX, windowY + 30, windowX + animListX + 3, windowY + windowHeight, new Color(50, 50, 50, 100).getRGB(), new Color(255, 255, 255).getRGB());
            float dWheel = Mouse.getDWheel();

//            modsY = windowY + 35;

            if (dWheel > 0 && listRoll2 < 0) {
                listRoll2 += 32;
            } else if (dWheel < 0) {
                listRoll2 -= 32;
            }

            listRoll = rollAnim.animate(listRoll2, listRoll, 0.05f);

            float modsY = windowY + 35 + listRoll;


            for (Category mt : categories) {
                if (mt.show || mt.needRemove) {
                    new Rect(windowX, modsY, animListX, 20, new Color(255, 255, 255), new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).render(mouseX, mouseY);
                    FontLoaders.arial18.drawString(mt.moduleType.name(), windowX + animListX - 130, modsY + 5, new Color(0, 0, 0).getRGB());
                    modsY += 25;
                    mt.modsY2 = 0;
                    for (Module m : Client.instance.getModuleManager().getModulesInType(mt.moduleType)) {
                        new Rect(windowX, modsY + mt.modsY2, animListX, 15, new Color(255, 255, 255), new Runnable() {
                            @Override
                            public void run() {
                            }
                        }).render(mouseX, mouseY);
                        if (modsY + 5 + mt.modsY2 < modsY + mt.modsY3+25)
                            FontLoaders.arial18.drawString(m.getName(), windowX + animListX - 120, modsY + 5 + mt.modsY2, new Color(50, 50, 50).getRGB());
                        mt.modsY2 += 20;
                    }
                    if (mt.needRemove) {
                        mt.modsY3 = mt.rollAnim2.animate(-25, mt.modsY3, 0.02f);
                        if (mt.modsY3 == -25) {
                            mt.needRemove = false;
                            mt.show = false;
                        }
                    } else {
                        mt.modsY3 = mt.rollAnim2.animate(mt.modsY2, mt.modsY3, 0.02f);
                    }

                    modsY += mt.modsY3;
                } else {
                    new Rect(windowX, modsY, animListX, 20, new Color(255, 255, 255), new Runnable() {
                        @Override
                        public void run() {
                        }
                    }).render(mouseX, mouseY);
                    FontLoaders.arial18.drawString(mt.moduleType.name(), windowX + animListX - 130, modsY + 5, new Color(0, 0, 0).getRGB());

                }

                modsY += 25;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }

    @Override
    public void drawWindow(float mouseX, float mouseY) {

        if ((mouseDX != 0 && drag) && Mouse.isButtonDown(0)) {
            windowX = mouseX - mouseDX;
        } else {
            drag = false;
            mouseDX = 0;
            mouseDY = 0;
        }
        if ((mouseDY != 0 && drag) && Mouse.isButtonDown(0)) {
            windowY = mouseY - mouseDY;
        } else {
            drag = false;
            mouseDX = 0;
            mouseDY = 0;
        }
        RenderUtil.drawRoundedRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, 2, new Color(255, 255, 255).getRGB());

        new Rect(windowX, windowY, windowWidth, 30, clientColor, new Runnable() {
            @Override
            public void run() {
                if (!Mouse.isButtonDown(0))
                    return;
                drag = true;
                if (mouseDX == 0) {
                    mouseDX = mouseX - windowX;
                }
                if (mouseDY == 0) {
                    mouseDY = mouseY - windowY;
                }
//                mouseDX = 0;
//                mouseDY = 0;
            }
        }).render(mouseX, mouseY);
    }
}
