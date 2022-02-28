package org.pipeman.pipebot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;

import static org.pipeman.pipebot.Main.*;

public class AudioUtil {
    private static final Logger logger = LoggerFactory.getLogger(AudioUtil.class);

    private static synchronized PlayerInstance getOrCreateGuildAudioPlayer(Guild guild) {
        long guildId = guild.getIdLong();
        PlayerInstance playerInstance = playerInstances.get(guildId);

        if (playerInstance == null) {
            playerInstance = new PlayerInstance(playerManager);
            playerInstances.put(guildId, playerInstance);
        }
        guild.getAudioManager().setSendingHandler(playerInstance.getSendHandler());

        return playerInstance;
    }

    public static void loadAndPlay(Member member, SlashCommandEvent event, String trackUrl) {
        PlayerInstance player = getOrCreateGuildAudioPlayer(Objects.requireNonNull(event.getGuild()));

        boolean isNewPlayer = false;
        if (player.playerGUIMessage == null) {
            player.sendEmbed(event);
            isNewPlayer = true;
        }

        final boolean finalIsNewPlayer = isNewPlayer;
        playerManager.loadItemOrdered(player, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(member, player, track);

                updateAndSendMsg(track, player, event);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack t : playlist.getTracks()) {
                    player.queue(t);
                }
                AudioTrack firstTrack = playlist.getSelectedTrack() ==
                        null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
                play(member, player, playlist.getSelectedTrack());

                updateAndSendMsg(firstTrack, player, event);
            }

            @Override
            public void noMatches() {
                sendErrorEmbed("No matches", "Nothing was found by " + trackUrl, finalIsNewPlayer, event, player);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendErrorEmbed("Something weird happened", "Please try again or choose a different song. "
                        + "\nWe are sorry for the inconvenience", finalIsNewPlayer, event, player);
                logger.error("An error occurred when loading \"" + trackUrl + "\". " + exception.getMessage());
            }
        });
    }

    private static void updateAndSendMsg(AudioTrack track, PlayerInstance player, SlashCommandEvent event) {
        if (player.player.getPlayingTrack() == null) {
            player.updateEmbed(track);
        } else {
            player.updateEmbed();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(114, 137, 218));
        eb.setDescription("Queued [" + track.getInfo().title + "](" + track.getInfo().uri + ")");
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }

    private static void sendErrorEmbed(String title, String desc, boolean newPlayer, SlashCommandEvent e, PlayerInstance p) {
        if (newPlayer) {
            if (p.playerGUIMessage == null) {
                p.setRunnableToExecuteWhenEmbedWasSent(() -> p.playerGUIMessage.delete().queue());
            } else {
                p.playerGUIMessage.delete().queue();
            }
            playerInstances.remove(e.getGuild().getIdLong());
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("ERROR");
        eb.setColor(new Color(231, 76, 60));
        eb.addField(title, desc, false);

        e.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }

    public static void play(Member member, PlayerInstance playerInstance, AudioTrack track) {
        if (member.getVoiceState() != null) {
            member.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
            playerInstance.queue(track);
        }
    }

    public static void shutdown() {
        for (PlayerInstance p : playerInstances.values()) {
            p.disconnect();
        }
    }
}
