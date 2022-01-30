package org.pipeman.pipebot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class CommandPurgeAmount extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (!event.getName().equals("purge") || event.getMember() == null) return;

        if (!event.getMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_MANAGE)) {
            event.reply("You dont have permission to use this command.").queue();
            return;
        }

        ArrayList<Message> messagesToDelete = new ArrayList<>();

        for (Message msg : event.getChannel().getIterableHistory()) {
            messagesToDelete.add(msg);

            if (messagesToDelete.size() >= event.getOption("amount").getAsLong()) {
                event.getChannel().purgeMessages(messagesToDelete);
                event.reply("Deleted " + event.getOption("amount").getAsLong() + " messages!").queue();
                return;
            }
        }
    }
}