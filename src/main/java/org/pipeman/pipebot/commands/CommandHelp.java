package org.pipeman.pipebot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class CommandHelp extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("help") || event.getMember() == null) return;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(114, 137, 218));
        eb.setTitle("Pipe-Bot Help");
        eb.addField("Music",
                """
                
                """, true);

        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
