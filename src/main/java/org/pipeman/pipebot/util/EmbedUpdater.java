package org.pipeman.pipebot.util;

import org.pipeman.pipebot.Main;
import org.pipeman.pipebot.music.PlayerInstance;

import java.util.Timer;
import java.util.TimerTask;

public class EmbedUpdater {
    private static final Timer t = new Timer();

    public static void start() {
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (PlayerInstance pi : Main.playerInstances.values()) {
                    if (!pi.player.isPaused() && pi.player.getPlayingTrack() != null) {
                        pi.updateEmbed();
                    }
                }
            }
        }, 0L, 5000L);
    }
}
