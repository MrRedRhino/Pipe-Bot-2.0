package org.pipeman.pipebot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.pipeman.pipebot.music.PlayerInstance;
import org.pipeman.pipebot.util.music.EmbedUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static JDA JDA;
    public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    public static final Map<Long, PlayerInstance> playerInstances = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            logger.error("Please pass a bot token as first programm argument!");
            System.exit(1);
        }

        JDA = JDABuilder.createDefault(args[0])
                .setActivity(Activity.listening("https://www.youtube.com/watch?v=Gleuqf10eB8"))
                .build();

//        JDA.upsertCommand("play", "Adds a song to the playlist")
//                .addOption(OptionType.STRING, "song", "search query or link", true)
//                .queue();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        AllCommands.addEventListeners(JDA);
        ShutdownManager.start();
        EmbedUpdater.start();
    }
}

