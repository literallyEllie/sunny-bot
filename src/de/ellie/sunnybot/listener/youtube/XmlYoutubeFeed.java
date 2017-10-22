package de.ellie.sunnybot.listener.youtube;

import com.rometools.rome.feed.synd.SyndEntry;
import de.ellie.sunnybot.SunnyBot;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class XmlYoutubeFeed {

    private SyndEntry lastPoll;

    public XmlYoutubeFeed(){

        try {

            //System.out.println(syndFeed.getEntries().get(0).getLinks());

//            SunnyBot.getSunnyBot().getJda().getTextChannelById(DiscordUtil.GENERAL).sendMessage("").queue();


        }catch(Exception e){
            e.printStackTrace();
            SunnyBot.getSunnyBot().logWarning("Failed to get Youtube feed!");
        }
    }

    public void subscribe(){
        try {
            HttpURLConnection a = open("https://www.youtube.com/feeds/videos.xml?user=ihascakes");
        }catch(Exception e){
            e.printStackTrace();
            SunnyBot.getSunnyBot().logError("Failed to subscribe to XML feed!");
        }

    }

    private HttpURLConnection open(String Url) throws Exception {
        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        return connection;
    }

}
