package tomorrow.tomo.luneautoleak.md5check;

import net.minecraft.client.Minecraft;
import tomorrow.tomo.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Random;

public class Md5Check {
    public static void check() {
        try {
            String siteString2 = "wo*&*.aP&".replace("caonima", "http").replaceAll("-X-", "/").replaceAll("wo", "gaoyu")
                    .replace("*&*", "sense").replace("aP&", "buzz");
            InetAddress addresses = InetAddress.getByName(siteString2);
            String ip = "tomorrow.version.92.247".replace("tomorrow", "103").replace("version", "17");
            if (!contains_(ip, addresses.getHostAddress()) || !(contains_(ip, "1") && contains_(ip, "103")
                    && contains_(ip, "17") && contains_(ip, "92.2") && contains_(ip, "47") && contains_(ip, "3.17"))) {
                System.out.println("hosts被修改。");
                Client.flag = -new Random().nextInt(555);
                Minecraft.getMinecraft().fontRendererObj = null;
                Minecraft.getMinecraft().currentScreen = null;
                System.exit(new Random().nextInt(555));
            } else {
                System.out.println("hosts通过。");
            }
            Client.md5flag = 0;

        } catch (Exception e) {
            Client.flag = -new Random().nextInt(555);
            Minecraft.getMinecraft().fontRendererObj = null;
            Minecraft.getMinecraft().currentScreen = null;
            System.exit(new Random().nextInt(555));
        }
    }


    public static boolean equals(String s, String t) {
        byte[] temp1 = s.getBytes();
        byte[] temp2 = t.getBytes();
        String md51 = DigestUtilsa.md5Hex(temp1);
        String md52 = DigestUtilsa.md5Hex(temp2);
        return md51 == md52;

    }

    public static boolean contains_(String src, String dst) {
        char[] srcArray = src.toCharArray();
        char[] dstArray = dst.toCharArray();
        int srcLen = srcArray.length;
        int dstLen = dstArray.length;
        for (int i = 0; i < srcLen; i++) {
            boolean find = false;
            if (srcArray[i] == dstArray[0] && (i + dstLen <= srcLen)) {
                int equalCount = 0;
                for (int j = 0; j < dstLen; j++) {
                    if (srcArray[i + j] == dstArray[j]) {
                        equalCount++;
                    }
                }
                if (equalCount == dstLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String get(URL url) throws IOException {
        HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();

        httpurlconnection.setRequestMethod("GET");
        BufferedReader bufferedreader = new BufferedReader(
                new InputStreamReader(httpurlconnection.getInputStream(), "utf-8"));
//        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream(), "GBK"));
        StringBuilder stringbuilder = new StringBuilder();
        String s;

        while ((s = bufferedreader.readLine()) != null) {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }

        bufferedreader.close();
        return stringbuilder.toString();
    }

}
