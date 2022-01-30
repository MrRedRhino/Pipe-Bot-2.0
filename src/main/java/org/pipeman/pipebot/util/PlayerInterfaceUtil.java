package org.pipeman.pipebot.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class PlayerInterfaceUtil {
    private static final Logger logger = LoggerFactory.getLogger(PlayerInterfaceUtil.class);

    public static MessageEmbed genSearchEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Interface");
        eb.setColor(new Color(114, 137, 218));
        eb.addField("Song", "Searching...", false); // [meddl](https://a.b)

        return eb.build();
    }

//    public static EmbedBuilder genNormalEmbed(String thumbnailUrl, AudioTrack track) {
//        EmbedBuilder eb = new EmbedBuilder();
//        eb.setTitle("Player Interface");
//        eb.setColor(new Color(114, 137, 218));
//        eb.setThumbnail(thumbnailUrl);
//        eb.addField("Song", formatTitle(track.getInfo().title, track.getInfo().uri)
//                + "\n" + genPlaytime((int) track.getPosition(), (int) track.getDuration()), false);
//
//        return eb;
//    }

    public static EmbedBuilder genNormalEmbed(String thumbnailUrl, AudioTrack track, boolean addLine) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Interface");
        eb.setColor(new Color(114, 137, 218));
        eb.setThumbnail(thumbnailUrl);
        eb.addField("Song", formatTitle(track.getInfo().title, track.getInfo().uri)
                + "\n" + genPlaytime((int) track.getPosition(), (int) track.getDuration())
                + (addLine ? "\n==========================" : ""), false);

        return eb;
    }

    public static MessageEmbed genNothingPlayingEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Interface");
        eb.setColor(new Color(114, 137, 218));
        eb.addField("Song", "Nothing playing", false);

        return eb.build();
    }

    public static String formatTitle(String in, String url) {
        return "[" + (in.length() < 40 ? in : in.substring(0, 40) + "...") + "](" + url + ")";
        // 280
    }

    private static String genPlaytime(int position, int duration) {
        position /= 1000;
        duration /= 1000;

        StringBuilder output = new StringBuilder(String.format("%d:%02d ", position / 60, position % 60));
        for (int i = 0; i < (float) position / (float) duration * 10; i++) {
            output.append("┈");
        }
        output.append("◉");
        for (int i = 0; i < 10 - (float) position / (float) duration * 10; i++) {
            output.append("┈");
        }
        output.append(String.format(" %d:%02d", duration / 60, duration % 60));

        return output.toString();
    }
}
