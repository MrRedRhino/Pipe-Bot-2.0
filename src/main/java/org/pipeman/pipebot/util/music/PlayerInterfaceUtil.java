package org.pipeman.pipebot.util.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class PlayerInterfaceUtil {
    public static MessageEmbed genSearchEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Interface");
        eb.setColor(new Color(114, 137, 218));
        eb.addField("Song", "Searching...", false); // [meddl](https://a.b)

        return eb.build();
    }

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

    public static MessageEmbed genNothingPlayingEmbed(boolean addLine) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Player Interface");
        eb.setColor(new Color(114, 137, 218));
        eb.setThumbnail("https://mineblocks.com/1/wiki/images/archive/c/cc/20220102011405%21Barrier.png");
        eb.addField("Song", "[Nothing playing](https://a.com)\n-:-- ┈┈┈┈┈┈┈┈┈┈┈ -:--"
                + (addLine ? "\n==========================" : ""), false);

        return eb.build();
    }

    public static String formatTitle(String in, String url) {
        return "[" + (in.length() < 40 ? in : in.substring(0, 40) + "...") + "](" + url + ")";
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
