package org.pipeman.pipebot;

import net.dv8tion.jda.api.JDA;
import org.pipeman.pipebot.commands.CommandPlay;
import org.pipeman.pipebot.commands.CommandPurgeAmount;
import org.pipeman.pipebot.commands.CommandPurgeUntilID;
import org.pipeman.pipebot.music.InterfaceButtonHandler;

public class AllCommands {
    public static void addEventListeners(JDA jda) {
        jda.addEventListener(new CommandPurgeAmount());
        jda.addEventListener(new CommandPurgeUntilID());
        jda.addEventListener(new CommandPlay());

        jda.addEventListener(new InterfaceButtonHandler());
    }
}
