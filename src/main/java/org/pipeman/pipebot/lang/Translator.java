package org.pipeman.pipebot.lang;

public class Translator {
    Enum<Languages> language;
    public Translator(long guildId) {
        language = Languages.GERMAN;
    }

    public String translate() {
        return "";
    }
}
