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
        boolean needsExtraReply = true;
        if (player.playerGUIMessage == null) {
            player.sendEmbed(event);
            needsExtraReply = false;
        }

        boolean finalNeedsExtraReply = needsExtraReply;
        playerManager.loadItemOrdered(player, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(member, player, track);

                if (player.player.getPlayingTrack() == null) {
                    player.updateEmbed(track);
                } else {
                    player.updateEmbed();
                }

                if (finalNeedsExtraReply) {
                    event.reply("Loaded track" + track.getInfo().title).setEphemeral(true).queue();
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                play(member, player, firstTrack);

                if (player.player.getPlayingTrack() == null) {
                    player.updateEmbed(firstTrack);
                } else {
                    player.updateEmbed();
                }
                if (finalNeedsExtraReply) {
                    event.reply("Loaded track " + firstTrack.getInfo().title).setEphemeral(true).queue();
                    // TODO Send in an Embed since that looks terrible
                }
            }

            @Override
            public void noMatches() {
                playerInstances.remove(event.getGuild().getIdLong());
                sendErrorEmbed("No matches", "Nothing was found by " + trackUrl,
                        finalNeedsExtraReply, event, player);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                playerInstances.remove(event.getGuild().getIdLong());
                sendErrorEmbed("Something weird happened", "Please try again or choose a different song. "
                        + "\nWe are sorry for the inconvenience", finalNeedsExtraReply, event, player);
                logger.error("An error occurred when loading \"" + trackUrl + "\". " + exception.getMessage());
            } // TODO remove errorEmbed thing in PlayerInstance
        });
    }

    private static void sendErrorEmbed(String title, String desc, boolean needsExtraReply, SlashCommandEvent e, PlayerInstance p) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("ERROR");
        eb.setColor(new Color(231, 76, 60));
        eb.addField(title, desc, false);

        if (needsExtraReply) {
            e.replyEmbeds(eb.build()).setEphemeral(true).queue();
        } else {
            p.setEmbedToUpdateWhenMessageHasBeenSent(eb.build());
        }
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
