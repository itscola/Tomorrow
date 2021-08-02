package tomorrow.tomo.managers.IRC;

import me.superskidder.server.data.User;
import me.superskidder.server.packets.ClientPacket;
import me.superskidder.server.packets.PacketType;
import me.superskidder.server.packets.PacketUtil;
import me.superskidder.server.packets.ServerPacket;
import me.superskidder.server.packets.client.C00PacketConnect;
import me.superskidder.server.util.MyBufferedReader;
import me.superskidder.server.util.MyPrintWriter;
import me.superskidder.server.util.encryption.EncryptionUtils;
import net.minecraft.client.Minecraft;
import tomorrow.tomo.Client;
import tomorrow.tomo.login.GuiTomoLogin;
import tomorrow.tomo.luneautoleak.LuneAutoLeak;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.modules.misc.IRC;
import tomorrow.tomo.utils.cheats.player.Helper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class IRCClient {
    public static User user;
    static Socket socket;
    private static InputStream input;
    public static BufferedReader reader;
    public static PrintWriter writer;
    private static final char[] DIGITS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHex(String str) {
        byte[] data = str.getBytes();
        int outLength = data.length;
        char[] out = new char[outLength << 1];
        for (int i = 0, j = 0; i < outLength; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & data[i]];
        }
        return new String(out);
    }

    public static String genKey() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd:HH:mm");
        String date = sdf.format(new Date());
        return toHex(date);
    }

    public static String decode(String content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return EncryptionUtils.decryptByHexString(content, genKey()).trim();
    }

    public static void Start(String username, String password, String hwid, String ip, int port) {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = null;
        try {
            writer = new MyPrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addPacket(writer, new C00PacketConnect(new User(username, password, hwid)));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        handle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void addPacket(PrintWriter writer, ClientPacket sp) {
        String s = PacketUtil.getClientJson(sp);
        String encode = EncryptionUtils.encryptIntoHexString(s, genKey());
        writer.println(encode);
        writer.flush();
    }

    public static void handle() throws IOException {
        String msg = reader.readLine();
        if (msg != null) {
            ServerPacket sp = null;
            try {
                sp = PacketUtil.parsePacketServer(decode(msg), ServerPacket.class);
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            if (sp.packetType.equals(PacketType.S03))
                if (ModuleManager.getModuleByClass(IRC.class).isEnabled()) {
                    Helper.sendMessageWithoutPrefix(sp.content);
                }
            if (sp.packetType.name().equals("S02")) {
                System.exit(0);
                JOptionPane.showMessageDialog(null, sp.content);
            } else if (sp.packetType.equals(PacketType.S01)) {
                user = new User(Client.username, Client.password, LuneAutoLeak.getHWID());
                if (Minecraft.getMinecraft().currentScreen instanceof GuiTomoLogin) {
                    Minecraft.should = true;
                }
            }
        }
    }
}
