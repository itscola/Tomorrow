package libraries.superskidder.ctrl;

import tomorrow.tomo.Client;

import javax.media.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Ctrl extends ControllerAdapter {

    @Override
    public void realizeComplete(RealizeCompleteEvent e) {
        super.realizeComplete(e);

        Client.musicPanel.player.prefetch();
    }


    @Override
    public void prefetchComplete(PrefetchCompleteEvent e) {
        super.prefetchComplete(e);
        // TODO: 2021/7/13 level
        Client.musicPanel.player.getGainControl().setLevel(0.4F);

        Client.musicPanel.totalTime = (int) Client.musicPanel.player.getDuration().getSeconds();

        Client.musicPanel.player.start();
    }

    @Override
    public void endOfMedia(EndOfMediaEvent e) {
        super.endOfMedia(e);
    }

}
