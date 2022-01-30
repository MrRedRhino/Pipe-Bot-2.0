package org.pipeman.pipebot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CommandPurgeUntilID extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("purge-until-id") || event.getMember() == null) return;

        if (!event.getMember().hasPermission(event.getGuildChannel(), Permission.MESSAGE_MANAGE)) {
            event.reply("You dont have permission to use this command.").queue();
            return;
        }
        String msgId = event.getOption("id").getAsString();

        ArrayList<Message> messagesToDelete = new ArrayList<>();
        int searchedMessages = 0;

        for (Message msg : event.getChannel().getIterableHistory()) {
            messagesToDelete.add(msg);
            searchedMessages++;
            if (msg.getId().equals(msgId)) {
                event.getChannel().purgeMessages(messagesToDelete);
                event.reply("Deleted " + searchedMessages + " messages!").queue();
                return;
            }

            if (searchedMessages > 200) {
                break;
            }
        }

        event.reply("Message not found in the last " + searchedMessages + " messages.").setEphemeral(true).queue();

//        event.reply("yes").queue(response -> response.deleteOriginal().queueAfter(5L, TimeUnit.SECONDS));
    }
}