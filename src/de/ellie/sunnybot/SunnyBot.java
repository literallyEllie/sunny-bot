package de.ellie.sunnybot;

import de.ellie.sunnybot.backend.CommandManager;
import de.ellie.sunnybot.backend.JsonSunnyConfig;
import de.ellie.sunnybot.backend.SQLManager;
import de.ellie.sunnybot.listener.CommandListener;
import de.ellie.sunnybot.listener.OtherListener;
import de.ellie.sunnybot.listener.youtube.XmlYoutubeFeed;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.utils.SimpleLog;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class SunnyBot {

    private static SunnyBot sunnyBot;

    private JDA jda;
    private SimpleLog logger;

    private JsonSunnyConfig sunnyConfig;
    private CommandManager commandManager;
    private SQLManager sqlManager;

    private XmlYoutubeFeed youtubeFeed;

    private CommandListener commandListener;
    private OtherListener otherListener;

    SunnyBot(){
        sunnyBot = this;
        logger = SimpleLog.getLog("SunnyBot");
        logInfo("Beginning startup...");
        long start = System.currentTimeMillis();

        logInfo("Initialising config..");
        sunnyConfig = new JsonSunnyConfig();
        logInfo("Initialising sql command..");
        sqlManager = new SQLManager(sunnyConfig.getUrl(), sunnyConfig.getUser(), sunnyConfig.getPass());
        logInfo("Initialising commands..");
        commandManager = new CommandManager(sunnyConfig.getBotPrefix());


        try{
            commandListener = new CommandListener(commandManager);
            otherListener = new OtherListener(sqlManager.getJoinMessage(), sqlManager.getLeaveMsg());
            jda = new JDABuilder(AccountType.BOT).
                    setToken(sunnyConfig.getToken())
                    .setGame(Game.of(sunnyConfig.getBotGame()))
                    .setStatus(OnlineStatus.fromKey(sunnyConfig.getBotState()))
                    .addEventListener(commandListener)
                    .addListener(otherListener)
                    .buildBlocking();

        }catch(Exception e){
            e.printStackTrace();
            logError("Failed to setup JDA!");
            shutdown(1);
            return;
        }

        logInfo("Initialising XML feed..");
        youtubeFeed = new XmlYoutubeFeed();


        logInfo("Startup finished in "+(System.currentTimeMillis() - start)+"ms!");
    }

    public void shutdown(int status){
        try {
            if (jda != null) {
                jda.shutdown(true);
                jda = null;
            }
            if (sunnyConfig != null) {
                sunnyConfig.finish();
                sunnyConfig = null;
            }
            if (commandManager != null) {
                commandManager.finish();
                commandManager = null;
            }
            if (sqlManager != null) {
                sqlManager.finish();
                sqlManager = null;
            }
            if(commandListener != null){
                commandListener.getCooldowns().clear();
                commandListener = null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.exit(status);
        }

    }

    public void logInfo(String info){
        logger.info(info);
    }

    public void logWarning(String warn){
        logger.warn(warn);
    }

    public void logError(String error){
        logger.fatal(error);
    }

    public static SunnyBot getSunnyBot() {
        return sunnyBot;
    }

    public JsonSunnyConfig getSunnyConfig() {
        return sunnyConfig;
    }

    public XmlYoutubeFeed getYoutubeFeed() {
        return youtubeFeed;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public JDA getJda() {
        return jda;
    }

    public OtherListener getOtherListener() {
        return otherListener;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

}
