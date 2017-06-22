package me.ellie.sunnybot.backend;

import me.ellie.sunnybot.SunnyBot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class JsonSunnyConfig {

    private File config;

    private transient String user, pass, url, botPrefix, botGame, botState, token;

    public JsonSunnyConfig() {
        config = new File("sunny.json");

        if (!config.exists()) {
            try {
                if (config.createNewFile()) {
                    String jsonConfig = "{\n" +
                            "\t\"token\":\"none\",\n" +
                            "\t\"botPrefix\":\"!\",\n" +
                            "\t\"botGame\":\"#SunnySquad\",\n" +
                            "\t\"botState\":\"online\",\n" +
                            "\t\"sql\":\"localhost:3306/database\",\n" +
                            "\t\"username\":\"user\",\n" +
                            "\t\"password\":\"pass\",\n" +
                            "}";
                    BufferedWriter writer = new BufferedWriter(new FileWriter(config));
                    writer.write(jsonConfig);
                    writer.close();
                } else {
                    throw new IllegalArgumentException("Configuration failed to be created!");
                }


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to create config file!");
                System.exit(1);
                return;
            }
        }
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(new FileReader(config));
            token = (String) object.get("token");
            botPrefix = (String) object.get("botPrefix");
            botGame = (String) object.get("botGame");
            botState = (String) object.get("botState");
            url = "jdbc:mysql://"+object.get("sql")+"?useUnicode=true&useSSL=true";
            user = (String) object.get("username");
            pass = (String) object.get("password");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            SunnyBot.getSunnyBot().logError("Failed to parse config file!");
            System.exit(1);
        }

    }

    public void finish(){
        config = null;
        botPrefix = botGame = user = pass = null;
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

    public File getConfig() {
        return config;
    }

}
