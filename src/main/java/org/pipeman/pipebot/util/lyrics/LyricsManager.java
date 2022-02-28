package org.pipeman.pipebot.util.lyrics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pipeman.pipebot.music.PlayerInstance;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LyricsManager {
    public PlayerInstance playerInstance;
    public final ArrayList<LyricLine> lines = new ArrayList<>();
    private final Timer timer = new Timer();

    public LyricsManager() {}

    public int loadLyrics(String videoID) throws URISyntaxException, IOException, InterruptedException, JSONException {
        HttpRequest r = HttpRequest.newBuilder()
                .uri(new URI("http://127.0.0.1:4567/api/lapi/get?video_id=" + videoID))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(r, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) return response.statusCode();

        JSONArray lines = new JSONArray(response.body());
        for (int i = 0; i < lines.length(); i++) {
            JSONObject o = lines.getJSONObject(i);
            this.lines.add(new LyricLine(o.getString("c"), o.getLong("t"), o.getLong("d")));
        }
        return 200;
    }

    public String genLines() {
        int firstLineIndex = 0;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).highlighted) {
                firstLineIndex = i;
                break;
            }
        }
        for (int i = (Math.max(firstLineIndex - 2, 0)); i < (Math.min(firstLineIndex + 5, lines.size())); i++) {
            LyricLine l = lines.get(i);
            out.append(l.content).append("\n");
        }
        return out.toString();
    }
    
    public void scheduleUpdates(ArrayList<LyricLine> lines) {
        System.out.println(lines);
        for (LyricLine l : lines) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    l.highlighted = true;
                }
            }, l.startTime);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    l.highlighted = false;
                }
            }, l.startTime + l.duration);
        }
    } // TODO Make pausing work.
}
