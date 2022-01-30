package org.pipeman.pipebot;

import net.dv8tion.jda.api.JDA;
import org.pipeman.pipebot.commands.CommandPlay;
import org.pipeman.pipebot.commands.CommandPurgeAmount;
import org.pipeman.pipebot.commands.CommandPurgeUntilID;
import org.pipeman.pipebot.music.InterfaceButtonHandler;

public class AllCommands {
    public static void upsert(JDA jda) {
//        jda.upsertCommand("play", "Adds a song to the playlist and plays it if nothing is playing; (video name)").queue();
//        jda.addEventListener(new CommandPlay());
//
//        jda.upsertCommand("leave", "Makes the bot leave your VC").queue();

//        jda.upsertCommand("purge", "Deletes messages.")
//                .addOption(OptionType.INTEGER, "amount", "Amount of messages to delete", true).queue();

//        jda.upsertCommand("purge-until-id", "Deletes all messages sent after the message with the given ID")
//                .addOptions(new OptionData(OptionType.STRING, "id", "Message-ID", true))
//                .queue();

//        jda.upsertCommand("lang", "Changes the bot's language; [language: Leave empty to see available languages]").queue();
//        jda.upsertCommand("mc-server", "Shows information about a minecraft server; (ip/domain: can be 'IP:PORT') (port)").queue();
//        jda.upsertCommand("clear", "Empties the playlist").queue();
//        jda.upsertCommand("seek", "Jumps to the given time in the song").queue();
//        jda.upsertCommand("volume", "Sets the bot's volume; (volume: 1 is normal, 2 is intense, 10 is not enjoyable, there is no limit)").queue();
//        jda.upsertCommand("player", "Shows an embed with controls about the bot and the song that is playing").queue();
//        jda.upsertCommand("equalizer", "Lets you edit the bot's EQ").queue();
    }

    public static void addEventListeners(JDA jda) {
        jda.addEventListener(new CommandPurgeAmount());
        jda.addEventListener(new CommandPurgeUntilID());
        jda.addEventListener(new CommandPlay());

        jda.addEventListener(new InterfaceButtonHandler());
    }

    public static void deleteCommands(JDA jda) {
//        jda.deleteCommandById("back").queue();
    }
}
