package de.elliepotato.sunnybot.backend;

import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONField;
import de.elliepotato.sunnybot.SunnyBot;

import java.io.*;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class JsonSunnyConfig {

    private File config;

    @JSONField(key = "sqlUsername")
    private String user;
    @JSONField(key = "sqlPassword")
    private String pass;
    @JSONField(key = "sqlUrl")
    private String url;
    @JSONField(key = "botPrefix")
    private String botPrefix;
    @JSONField(key = "botGame")
    private String botGame;
    @JSONField(key = "botState")
    private String botState;
    @JSONField(key = "token")
    private String token;

    public JsonSunnyConfig() {
        config = new File("sunny.json");

        if (!config.exists()) {
            try {
                if (config.createNewFile()) {
                    create();
                } else throw new IllegalArgumentException("Configuration failed to be created!");

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public void create() {
        final JSON json = new JSON()
                .put("token", "none")
                .put("botPrefix", "!")
                .put("botGame", "#SunnySquad")
                .put("botState", "online")
                .put("sqlUrl", "jdbc:mysql://localhost:3306/database?useUnicode=true&useSSL=true")
                .put("sqlUsername", "username")
                .put("sqlPassword", "password");

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(config));
            writer.write(json.marshal());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonSunnyConfig load() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(config));
            final StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            final String jsonString = stringBuilder.toString();
            JSON json = new JSON(jsonString);

            return json.marshal(JsonSunnyConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getToken() {
        return token;
    }

    public String getBotPrefix() {
        return botPrefix;
    }

    public String getBotGame() {
        return botGame;
    }

    public String getBotState() {
        return botState;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

}
