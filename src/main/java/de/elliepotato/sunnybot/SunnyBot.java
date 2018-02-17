package de.elliepotato.sunnybot;

import de.elliepotato.sunnybot.backend.CommandManager;
import de.elliepotato.sunnybot.backend.JsonSunnyConfig;
import de.elliepotato.sunnybot.backend.SQLData;
import de.elliepotato.sunnybot.backend.console.SunnyConsole;
import de.elliepotato.sunnybot.backend.mysql.MySQLManager;
import de.elliepotato.sunnybot.listener.UserLeaveJoinListener;
import de.elliepotato.sunnybot.util.DiscordRaffle;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class SunnyBot {

    public static String VERSION = "4";

    private final Logger LOGGER = LoggerFactory.getLogger("SunnyBot");

    private JDA jda;
    private SunnyConsole console;

    private JsonSunnyConfig sunnyConfig;
    private MySQLManager sqlManager;
    private CommandManager commandManager;

    private SQLData sqlData;

    private UserLeaveJoinListener userLeaveJoinListener;

    private DiscordRaffle discordRaffle;


    SunnyBot() {
        LOGGER.info("Beginning startup...");
        long start = System.currentTimeMillis();

        LOGGER.info("Initialising config...");
        sunnyConfig = new JsonSunnyConfig().load();
        LOGGER.info("Initialising SQL manager...");
        sqlManager = new MySQLManager(this);
        LOGGER.info("Loading SQL data...");
        sqlData = new SQLData(this);

        LOGGER.info("Initialising commands...");
        commandManager = new CommandManager(this, sunnyConfig.getBotPrefix());

        try {
            userLeaveJoinListener = new UserLeaveJoinListener(this, sqlData.getJoinMessage(), sqlData.getLeaveMsg());
            jda = new JDABuilder(AccountType.BOT).
                    setToken(sunnyConfig.getToken())
                    .setGame(Game.playing(sunnyConfig.getBotGame()))
                    .setStatus(OnlineStatus.fromKey(sunnyConfig.getBotState()))
                    .addEventListener(commandManager, userLeaveJoinListener)
                    .buildBlocking();

        } catch (LoginException | InterruptedException e) {
            LOGGER.error("Failed to setup JDA!", e);
            e.printStackTrace();
            shutdown(1);
            return;
        }

        LOGGER.info("Starting console thread...");
        console = new SunnyConsole(this);
        console.run();

        LOGGER.info("Startup finished in " + (System.currentTimeMillis() - start) + "ms!");

    }

    public void shutdown(int status) {
        try {
            if (console != null)
                console.join();

            if (jda != null)
                jda.shutdown();

            if (commandManager != null)
                commandManager.finish();

            if (sqlManager != null)
                sqlManager.shutdown();

        } catch (Exception e) {
            LOGGER.error("Error whilst shutting down!", e);
            e.printStackTrace();
        }
        System.exit(status);
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public JDA getJda() {
        return jda;
    }

    public JsonSunnyConfig getSunnyConfig() {
        return sunnyConfig;
    }

    public MySQLManager getSqlManager() {
        return sqlManager;
    }

    public SQLData getSqlData() {
        return sqlData;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public UserLeaveJoinListener getUserLeaveJoinListener() {
        return userLeaveJoinListener;
    }

    public DiscordRaffle getDiscordRaffle() {
        return discordRaffle;
    }

    public void setDiscordRaffle(DiscordRaffle discordRaffle) {
        this.discordRaffle = discordRaffle;
    }

    public void raffleEnter(User user, String message) {
        if (discordRaffle == null || discordRaffle.isClosed()
                || discordRaffle.getParticipants().contains(user.getIdLong())
                || !message.equalsIgnoreCase(discordRaffle.getKeyWord())) return;
        discordRaffle.add(user.getIdLong());
    }

    /**
     * Message a discord channel
     *
     * @param channel Channel ID
     * @param message Message to send
     */
    public void messageChannel(long channel, String message) {
        jda.getTextChannelById(channel).sendMessage(message).queue();
    }

    /**
     * Message any channel that implements {@link Channel}
     *
     * @param channel Channel ID
     * @param message Message to send
     */
    public void messageChannel(Channel channel, String message) {
        messageChannel(channel.getIdLong(), message);
    }

    /**
     * Message a discord channel
     *
     * @param channel Channel ID
     * @param message embed to send
     */
    public void messageChannel(long channel, MessageEmbed message) {
        if (jda.getTextChannelById(channel) != null) {
            jda.getTextChannelById(channel).sendMessage(message).queue();
        }
    }

    /**
     * Message any channel that implements {@link Channel}
     *
     * @param channel Channel ID
     * @param message embed to send
     */
    public void messageChannel(Channel channel, MessageEmbed message) {
        messageChannel(channel.getIdLong(), message);
    }

    /**
     * Send a temporary message to a discord channel
     *
     * @param channel    The channel to send to
     * @param message    The message content
     * @param expireTime After what delay should the message be deleted
     */
    public void tempMessage(long channel, String message, int expireTime, Message cleanupMsg) {
        jda.getTextChannelById(channel).sendMessage(message).queue(message1 -> {
            try {
                message1.delete().queueAfter(expireTime, TimeUnit.SECONDS);
            } catch (ErrorResponseException ignored) {
                // if message deletes before we get to it!
            }
            if (cleanupMsg != null) cleanupMsg.delete().queueAfter(expireTime, TimeUnit.SECONDS);
        });
    }

    /**
     * Send a temporary message to a discord channel that implements {@link Channel} (aka all of them)
     *
     * @param channel    The channel to send to
     * @param message    The message content
     * @param expireTime After what delay should the message be deleted
     */
    public void tempMessage(Channel channel, String message, int expireTime, Message cleanupMsg) {
        tempMessage(channel.getIdLong(), message, expireTime, cleanupMsg);
    }

    /**
     * Send a private message to a user
     *
     * @param user    User to message
     * @param content the message content
     */
    public void privateMessage(User user, String content) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    /**
     * Send a private message to a member
     *
     * @param member  Member to message
     * @param content the message content
     */
    public void privateMessage(Member member, String content) {
        privateMessage(member.getUser(), content);
    }

    /**
     * Attempt to parse from an input string to a {@link User}.
     *  Will attempt to parse from: a raw ID, a mention or a User#Discrim
     * @param input The input to parse from.
     * @return the user or null if failed to parse
     */
    public User parseUser(String input) {

        // raw id
        long id;
        try {
            id = Long.parseLong(input);
            return jda.getUserById(id);
        } catch (NumberFormatException e) {
        }

        // a mention
        if (DiscordUtil.PATTERN_USER_ID.matcher(input).matches()) {
            try {
                id = Long.parseLong((input.replace("<", "")
                        .replace(">", "").replace("@", "")
                        .replace("!", ""))); // idk
                return jda.getUserById(id);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        // accept user#discrim
        if (input.contains("#")) {

            final String[] parts = input.split("#");
            final String name = parts[0];
            final String discrim = parts[1]; // not parsing to int cus 0006 == 6

            for (final User user : jda.getUsersByName(name, true)) {
                if (user.getName().equalsIgnoreCase(name) && user.getDiscriminator().equals(discrim)) {
                    return user;
                }
            }

        }

        return null;
    }

}
