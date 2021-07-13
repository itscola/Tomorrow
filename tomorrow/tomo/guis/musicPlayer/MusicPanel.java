package tomorrow.tomo.guis.musicPlayer;

import com.mojang.realmsclient.gui.ChatFormatting;

import libraries.superskidder.ctrl.Ctrl;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.lwjgl.input.Mouse;
import tomorrow.tomo.Client;
import tomorrow.tomo.guis.font.FontLoaders;
import tomorrow.tomo.mods.modules.render.MusicPlayer;
import tomorrow.tomo.utils.cheats.player.Helper;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.render.RenderUtil;
import tomorrow.tomo.utils.render.renderManager.Rect;
import tomorrow.tomo.utils.render.renderManager.RoundRect;

import javax.media.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MusicPanel {

    public static String playing;
    public float realX, realY;

    public ArrayList<Music> musics = new ArrayList<>();

    public RoundRect title;

    public static EmptyInputBox inputBox = new EmptyInputBox(1, Helper.mc.fontRendererObj, 0, 0, 140, 10);
    private float dragX;
    private float dragY;
    private boolean drag;
    public static Music isPlaying;
    private double percent = 0;

    public Player player;

    public double totalTime;
    public float time;
    private TimerUtil timer = new TimerUtil();

    public static void initMusic(File file){
        try {
            URL url = file.toURI().toURL();
            MediaLocator locator = new MediaLocator(url);
            Client.musicPanel.player = Manager.createPlayer(locator);
            Client.musicPanel.player.addControllerListener(new Ctrl());
            Client.musicPanel.player.realize();

        } catch (IOException | NoPlayerException e) {
            e.printStackTrace();
        }
    }

    public void render(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        realX = MusicPlayer.x.getValue().floatValue() / 100f * sr.getScaledWidth();
        realY = MusicPlayer.y.getValue().floatValue() / 100f * sr.getScaledHeight();
        inputBox.xPosition = realX + 5;
        inputBox.yPosition = realY + 16;
        RenderUtil.drawRoundedRect(realX, realY, realX + 160, realY + 190, 3, new Color(59, 59, 59).getRGB());

//        if (timer.delay(1000) && isPlaying != null) {
//            time++;
//            timer.reset();
//        }

        title = new RoundRect(realX, realY, 160, 15, 4, new Color(19, 19, 19), new Runnable() {
            @Override
            public void run() {
                if (dragX == 0 && dragY == 0) {
                    dragX = mouseX - realX;
                    dragY = mouseY - realY;
                } else {
                    realX = mouseX - dragX;
                    realY = mouseY - dragY;
                }
                drag = true;
            }
        });
        if (Mouse.isButtonDown(0) && drag) {
            realX = mouseX - dragX;
            realY = mouseY - dragY;
        } else {
            dragX = 0;
            dragY = 0;
            drag = false;
        }
        title.render(mouseX, mouseY);
        MusicPlayer.x.setValue(realX / sr.getScaledWidth() * 100);
        MusicPlayer.y.setValue(realY / sr.getScaledHeight() * 100);
        FontLoaders.arial16.drawCenteredStringWithShadow("Music Player", realX + 80, realY + 5, -1);
        inputBox.drawTextBox();
        RenderUtil.drawRect(realX + 5, realY + 26, realX + 155, realY + 26.5f, new Color(0, 124, 255).getRGB());



        float y = realY + 30;
        if(musics.size() < 10){
            return;
        }
        for (Music m : musics) {
            new RoundRect(realX + 5, y, 150, 12, 1, new Color(108, 108, 108), new Runnable() {
                @Override
                public void run() {
                    isPlaying = m;

                    File file = new File("C:/musicdata");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String filePath = file.getAbsolutePath();

                    FileOutputStream fileOut = null;
                    HttpURLConnection conn = null;
                    InputStream inputStream = null;
                    try {
                        // 建立链接
                        URL httpUrl = new URL(m.url);
                        conn = (HttpURLConnection) httpUrl.openConnection();
                        //以Post方式提交表单，默认get方式
//                        conn.setRequestMethod("get");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        // post方式不能使用缓存
                        conn.setUseCaches(false);
                        //连接指定的资源
                        conn.connect();
                        //获取网络输入流
                        inputStream = conn.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        //判断文件的保存路径后面是否以/结尾
                        if (!filePath.endsWith("/")) {

                            filePath += "/";

                        }
                        //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
                        fileOut = new FileOutputStream(filePath + "music.mp3");
                        BufferedOutputStream bos = new BufferedOutputStream(fileOut);

                        byte[] buf = new byte[4096];
                        int length = bis.read(buf);
                        //保存文件
                        while (length != -1) {
                            bos.write(buf, 0, length);
                            length = bis.read(buf);
                        }
                        bos.close();
                        bis.close();
                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("抛出异常！！");
                    }
                    File musicFile = new File("C:/musicdata/music.mp3");
                    initMusic(musicFile);
                }
            }).render(mouseX, mouseY);
//            RenderUtil.drawCustomImage(realX+5,realY+20,10,10,new ResourceLocation());
            Client.fontLoaders.msFont18.drawStringWithShadow(m.name + " " + ChatFormatting.GRAY + m.description, realX + 15, y + 2, -1);

            y += 15;
        }
        if (isPlaying != null) {
//            System.out.println(isPlaying.name);
//            Client.fontLoaders.msFont18.drawCenteredString("Playing:" + ChatFormatting.BLUE + isPlaying.name, realX + 80, realY + 168, -1);
            percent = time / totalTime;
            RenderUtil.drawRect(realX, realY + 185, realX + (160 * percent / 100f), realY + 186, new Color(71, 162, 255));
        }

        new Rect(realX, realY + 185, 160, 1, new Color(144, 144, 144), new Runnable() {
            @Override
            public void run() {

            }
        }).render(mouseX, mouseY);

    }

    public void onMouse(float mouseX, float mouseY, int click) {
        inputBox.mouseClicked((int) mouseX, (int) mouseY, click);
    }

    public void getMusics() {
        musics.clear();
        String s = null, n = "null";
        try {
            s = HttpUtil.get(new URL("http://music.eleuu.com/playlist/detail?id=" + inputBox.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject o = JSONObject.fromObject(s);
        JSONObject o1 = (JSONObject) o.get("playlist");

        JSONArray o2 = o1.getJSONArray("tracks");
        int size = o2.size();
        for (int i = 0; i < size; i++) {
            JSONObject jo1 = o2.getJSONObject(i);
//            System.out.println(jo1.getString(jo1.getString("name"))); //循环返回网址

            String s1 = null;
            try {
                s1 = HttpUtil.get(new URL("http://music.eleuu.com/song/url?id=" + jo1.get("id")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject oa = JSONObject.fromObject(s1);
            System.out.println(oa);

            JSONArray ob = oa.getJSONArray("data");
//            System.out.println(ob);

            JSONObject joo = JSONObject.fromObject(ob.getString(0));

            if (joo.get("url").toString() == "null") {
                continue;
            }
//            JSONObject oa1 = oa.get("data");
            JSONArray jooo = jo1.getJSONArray("ar");
            JSONObject jooo2 = jo1.getJSONObject("al");

            musics.add(new Music(jo1.getString("name"), jooo.getJSONObject(0).getString("name"), jooo2.getString("picUrl"), jo1.getString("id"), joo.getString("url")));

        }

    }

    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
}
