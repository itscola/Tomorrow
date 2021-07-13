package tomorrow.tomo.managers.config;

import tomorrow.tomo.Client;
import tomorrow.tomo.customgui.GuiObject;

import java.util.ArrayList;
import java.util.List;

public class GuiSettings {
    public List<GuiObject> settings = new ArrayList<>();


    public void init() {
        settings = Client.instance.customgui.objects;
    }
}
