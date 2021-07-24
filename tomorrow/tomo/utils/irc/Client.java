package tomorrow.tomo.utils.irc;

import com.mojang.realmsclient.gui.ChatFormatting;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.modules.misc.IRC;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.irc.packets.clientside.ClientChatPacket;
import tomorrow.tomo.utils.irc.packets.clientside.ClientConnectPacket;
import tomorrow.tomo.utils.irc.packets.clientside.ClientHeartPacket;
import tomorrow.tomo.utils.irc.packets.IRCPacket;
import tomorrow.tomo.utils.irc.packets.IRCType;
import tomorrow.tomo.utils.irc.packets.serverside.ServerChatPacket;
import tomorrow.tomo.utils.irc.packets.serverside.ServerHeartNeededPacket;
import tomorrow.tomo.utils.irc.packets.serverside.ServerStopPacket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Client {

	public static BufferedReader reader;
	public static Socket socket;
	public static PrintWriter pw;
	public static boolean connected = false;
	public static TimerUtil timerUtil = new TimerUtil();
	public static User user;
	public static Thread clientThread;

	public static void sendMessage(String m) {
		pw.flush();
		pw.println(m);
		pw.flush();
	}

	public static void sendChatMessage(ClientChatPacket c) {
		ClientChatPacket c1 = c;
		c1.content = tomorrow.tomo.Client.username + ":" + c.content;
		sendMessage(IRCUtils.toJson(c));
	}

	public static void connect(String userName, String password, String hwid) {

		try {
			socket = new Socket("127.0.0.1", 6666);
			reader = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new MyPrintWriter(socket.getOutputStream());
			connected = true;
			sendMessage(IRCUtils.toJson(
					new ClientConnectPacket(System.currentTimeMillis(), "connection", userName, password, hwid)));

			clientThread = new ClientThread();
			clientThread.start();

		} catch (Exception e) {
			e.printStackTrace();
//            System.exit(0);
		}
	}

	public static void handle() {
		if (socket.isClosed()) {
			connected = false;
		}
		try {
			String ircMessage = reader.readLine();
			if (ircMessage == null) {
				return;
			}
			IRCPacket packet = IRCUtils.toPacket(ircMessage, IRCPacket.class);
			if (packet.type.equals(IRCType.CHAT)) {
				ServerChatPacket c = (ServerChatPacket) IRCUtils.toPacket(ircMessage, ServerChatPacket.class);
				if (Helper.mc.thePlayer != null) {
					assert ModuleManager.getModuleByClass(IRC.class) != null;
					if (ModuleManager.getModuleByClass(IRC.class).isEnabled()) {
						Helper.sendMessageWithoutPrefix(
								ChatFormatting.BLUE + "[IRC]" + ChatFormatting.WHITE + " " + c.content);
					}
				}
			} else if (packet.type.equals(IRCType.HEART)) {
				timerUtil.reset();
				ServerHeartNeededPacket c = (ServerHeartNeededPacket) IRCUtils.toPacket(ircMessage,
						ServerHeartNeededPacket.class);
				User u = (User) IRCUtils.toJson(c.content, User.class);
				user.authName = u.authName;
				user.hwid = u.hwid;
				user.connected = u.connected;
				user.password = u.password;
				System.out.println("发送心跳包:" + user.authName + user.GameID + user.head);

				sendMessage(IRCUtils.toJson(new ClientHeartPacket(System.currentTimeMillis(), IRCUtils.toJson(user))));
			} else if (packet.type.equals(IRCType.STOP)) {
				connected = false;
				ServerStopPacket c = (ServerStopPacket) IRCUtils.toPacket(ircMessage, ServerStopPacket.class);
				if (Helper.mc.thePlayer != null) {
					assert ModuleManager.getModuleByClass(IRC.class) != null;
					if (ModuleManager.getModuleByClass(IRC.class).isEnabled()) {
						Helper.sendMessageWithoutPrefix(
								ChatFormatting.BLUE + "[IRC]" + ChatFormatting.RED + " " + c.content);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
//            System.out.println("");
		}
	}

	static class MyPrintWriter extends PrintWriter {
		public MyPrintWriter(Writer out) {
			super(out);
		}

		public MyPrintWriter(Writer out, boolean autoFlush) {
			super(out, autoFlush);
		}

		public MyPrintWriter(OutputStream out) {
			super(out);
		}

		public MyPrintWriter(OutputStream out, boolean autoFlush) {
			super(out, autoFlush);
		}

		public MyPrintWriter(String fileName) throws FileNotFoundException {
			super(fileName);
		}

		public MyPrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
			super(fileName, csn);
		}

		public MyPrintWriter(File file) throws FileNotFoundException {
			super(file);
		}

		public MyPrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
			super(file, csn);
		}

		@Override
		public void println(String msg) {
			Base64.Encoder encoder = Base64.getEncoder();
			byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

			msg = encoder.encodeToString(msgByte);
			super.println(msg);
		}
	}

	static class MyBufferedReader extends BufferedReader {
		public MyBufferedReader(Reader in, int sz) {
			super(in, sz);
		}

		public MyBufferedReader(Reader in) {
			super(in);
		}

		@Override
		public String readLine() throws IOException {
			try {
				String msg = super.readLine();
				msg = this.cleanStr(msg);

				Base64.Decoder decoder = Base64.getDecoder();
				msg = new String(decoder.decode(msg), StandardCharsets.UTF_8);

				msg = this.cleanStr(msg);
				return msg;
			} catch (Exception e) {
				return null;
			}
		}

		public String cleanStr(String str) {
			str = str.replaceAll("\n", "");
			str = str.replaceAll("\r", "");
			str = str.replaceAll("\t", "");
			return str;
		}
	}
}
