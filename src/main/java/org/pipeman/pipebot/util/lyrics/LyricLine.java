package org.pipeman.pipebot.util.lyrics;

public class LyricLine {
    final String content;
    final long startTime;
    final long duration;
    public boolean highlighted = false;

    public LyricLine(String content, long startTime, long duration) {
        this.content = content;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String toString() {
        return "\"" + content + "\"";
    }
}
