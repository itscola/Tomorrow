package tomorrow.tomo.commands.commands;

import net.minecraft.util.HttpUtil;
import tomorrow.tomo.commands.Command;
import tomorrow.tomo.utils.cheats.player.Helper;

import java.io.IOException;
import java.net.URL;

public class CommandHuji extends Command {

    public static String qq = "";

    public CommandHuji() {
        super("huji", new String[]{}, "<qq>", "查询该用户的qq绑定手机号");
    }

    @Override
    public String execute(String[] args) {
        if (args.length < 1) {
            return null;
        }
        qq = args[0];
        new getThread().start();
        return null;
    }


    class getThread extends Thread {
        @Override
        public void run() {
            try {
                String s = HttpUtil.get(new URL("http://t.qb-api.com/qbtxt-api.php?qq=" + CommandHuji.qq));
                Helper.sendMessage("[AutoHuJi] " + s);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
