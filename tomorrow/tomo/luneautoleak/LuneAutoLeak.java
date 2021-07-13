package tomorrow.tomo.luneautoleak;

import net.minecraft.client.Minecraft;
import net.minecraft.util.HttpUtil;
import tomorrow.tomo.Client;
import tomorrow.tomo.luneautoleak.checks.AntiPatch;
import tomorrow.tomo.luneautoleak.md5check.Md5Util;
import tomorrow.tomo.luneautoleak.othercheck.ReVerify;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LuneAutoLeak {
    public AntiPatch antiPatch;

    public void startLeak() {
        String username = JOptionPane.showInputDialog(null, "输入您的用户名");
        String password = JOptionPane.showInputDialog(null, "输入您的密码");

        try {
            if(AntiPatch.contains_(HttpUtil.get(new URL("http://gaoyusense.buzz/Tomo/Login/login.php?UserName=" + username + "&PassWord=" + password)), "Successfully")){
                Client.username = username;
                JOptionPane.showMessageDialog(null,"验证成功");
            }else{
                JOptionPane.showMessageDialog(null,"验证失败");
                System.exit(234);
                Client.flag = 7641;
                Minecraft.getMinecraft().thePlayer = null;
                Minecraft.getMinecraft().fontRendererObj = null;
                Minecraft.getMinecraft().currentScreen = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        antiPatch = new AntiPatch();
        new Md5Util();
        new ReVerify();

    }
}
