package tomorrow.tomo.luneautoleak;

import net.minecraft.client.Minecraft;
import net.minecraft.util.HttpUtil;
import tomorrow.tomo.Client;
import tomorrow.tomo.luneautoleak.checks.AntiPatch;
import tomorrow.tomo.luneautoleak.md5check.Md5Check;
import tomorrow.tomo.luneautoleak.othercheck.ReVerify;
import tomorrow.tomo.utils.irc.User;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LuneAutoLeak {
    public AntiPatch antiPatch;

    public void startLeak() {
    	JOptionPane.showInputDialog("This is your HWID:",getHWID());
        String username = JOptionPane.showInputDialog(null, "UserName");
        String password = JOptionPane.showInputDialog(null, "PassWord");
        
        tomorrow.tomo.utils.irc.Client.user = new User(username,password,getHWID(),"");
        System.out.println("Connecting irc server");
        tomorrow.tomo.utils.irc.Client.connect(username,password, getHWID());
        Client.username = username;
        Client.password = password;
        antiPatch = new AntiPatch();
        new ReVerify();
    }

	public static String getHWID() {
		try {
			StringBuilder sb = new StringBuilder();
			String computerName = System.getenv("COMPUTERNAME");
			String processIdentifier = System.getenv("PROCESS_IDENTIFIER");
			String main =  processIdentifier + computerName;
			byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(bytes);
			for (byte b : md5) {
				sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 2);
			}
			char[] wow = sb.toString().toCharArray();
			for (char c : wow) {
				try {
					sb.insert(computerName.length(), c ^ 555 & 114 & 514 ^ 233);
				}catch(Exception e){
					// oh shit
				}
			}
			String lastNumber = sb.substring(sb.toString().length() - 1);
			try {
				int num = Integer.parseInt(lastNumber);
				sb.append(getShitString(num));
			}catch (Exception e){
				//System.out.println("唉");
				return sb.toString();
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException ignored) {
		}
		return null;
	}
	
	public static String getShitString(int length){
		String str = "456g4fdgh98637156df4g69874dfgf44gfd56g4f5d6g";
		return str.substring(0, length);
	}
}
