package org.pipeman.pipebot.webinterface;

import org.pipeman.pipebot.Main;
import org.pipeman.pipebot.music.PlayerInstance;
import spark.Spark;

public class Server {
    public static void start() {
        Spark.port(4444);

        Spark.after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        Spark.get("/pbi/sessions/*", ((request, response) -> {
            String url = request.url();
            int sessionId;
            try {
                sessionId = Integer.parseInt(url.substring(url.indexOf("/pbi/sessions/") + 14));
            } catch (NumberFormatException e) {
                response.status(401);
                return "{\"message\": \"Invalid session id\"}";
            }
            for (PlayerInstance pi : Main.playerInstances.values()) {
                if (pi.sessionId == sessionId) {
                    return "";
                }
            }
            response.status(401);
            return "{\"message\": \"Invalid session id\"}";
        }));

        Spark.get("*", ((request, response) -> {
            response.status(404);
            return "{\"message\": \"404: Not Found\"}";
        }));
    }

    public static void main(String[] args) {
        start();
    }
}
