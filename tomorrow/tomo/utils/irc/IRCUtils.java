package tomorrow.tomo.utils.irc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tomorrow.tomo.utils.irc.packets.IRCPacket;

import java.lang.reflect.Type;

public class IRCUtils {
    static Gson gson = new GsonBuilder().setVersion(1.1).setPrettyPrinting().create();

    public static String toJson(IRCPacket packet) {
        String j = gson.toJson(packet);
        return j;
    }

    public static String toJson(Object packet) {
        String j = gson.toJson(packet);
        return j;
    }

    public static IRCPacket toPacket(String packet,Type ctype) {
        return gson.fromJson(packet, ctype);
    }
    public static Object toJson(String packet,Type ctype) {
        return gson.fromJson(packet, ctype);
    }
}
