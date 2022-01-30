package org.pipeman.pipebot.music;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.pipeman.pipebot.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterfaceButtonHandler extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(InterfaceButtonHandler.class);

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        event.deferEdit().queue(); // TODO only defer if user has permission otherwise send angry message
        PlayerInstance pi = Main.playerInstances.get(event.getGuild().getIdLong());
        if (pi == null
                || event.getMember() == null
                || event.getMember().getVoiceState() == null
                || event.getMessage().getIdLong() != pi.playerGUIMessage.getIdLong()
                || event.getGuild().getAudioManager().getConnectedChannel() == null) return;

        if (event.getGuild().getAudioManager().getConnectedChannel().equals(event.getMember().getVoiceState().getChannel())) {
            if (event.getComponentId().equals("back")) {
                pi.back();
            } else if (event.getComponentId().equals("fb")) {
                pi.ffOrFb(-10000L);
            } else if (event.getComponentId().equals("pause")) {
                pi.togglePaused();
            } else if (event.getComponentId().equals("ff")) {
                pi.ffOrFb(10000L);
            } else if (event.getComponentId().equals("skip")) {
                pi.skip();

            } else if (event.getComponentId().equals("lqh")) {
                pi.lqhButtonClicked();
            } else if (event.getComponentId().equals("loop")) {
                pi.loopButtonClicked();
            } else if (event.getComponentId().equals("leave")) {
                pi.disconnect(event.getGuild());
            }
        }

    }
}
