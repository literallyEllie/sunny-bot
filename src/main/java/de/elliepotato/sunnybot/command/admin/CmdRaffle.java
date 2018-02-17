package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import de.elliepotato.sunnybot.util.DiscordRaffle;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ellie on 05/05/2017 for YT-er Sunny.
 */
public class CmdRaffle extends SunnyCommand {

    private final List<String> subCommands = Arrays.asList("start", "cancel", "draw", "blacklist", "toggle", "info");
    private DiscordRaffle discordRaffle;

    public CmdRaffle(SunnyBot sunnyBot) {
        super(sunnyBot, "raffle", "Create a raffle", Permission.BAN_MEMBERS, "raffle <start | draw | cancel | info | blacklist>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        final TextChannel textChannel = e.getChannel();
        final String sub = args[0].toLowerCase();

        if (!subCommands.contains(sub)) getSunnyBot().messageChannel(textChannel, correctUsage());

        switch (sub) {
            case "start":
                if (discordRaffle != null) {
                    getSunnyBot().messageChannel(textChannel, "A raffle is currently on going!");
                    return;
                }

                // raffle start keyword
                if (args.length == 3) {
                    discordRaffle = new DiscordRaffle(args[1]);
                    getSunnyBot().setDiscordRaffle(discordRaffle);
                    getSunnyBot().messageChannel(DiscordUtil.CHANNEL_GENERAL, "**A raffle has been started!** Type `" + discordRaffle.getKeyWord()
                            + "` to enter!");
                    return;
                } else
                    getSunnyBot().messageChannel(textChannel, "Correct usage: " + getSunnyBot().getSunnyConfig().getBotPrefix() + getName() + " start <keyword>");
                break;
            case "cancel":
                if (discordRaffle != null) {
                    getSunnyBot().messageChannel(DiscordUtil.CHANNEL_GENERAL, "**The raffle has been cancelled!**");
                    getSunnyBot().setDiscordRaffle(null);
                    discordRaffle.finish();
                    discordRaffle = null;
                    return;
                }

                getSunnyBot().messageChannel(textChannel, "There is no current raffle to cancel.");
                break;
            case "draw":
                if (args.length == 3) {
                    if (!DiscordUtil.isInteger(args[1])) {
                        getSunnyBot().messageChannel(textChannel, "Invalid integer `" + args[0] + "`");
                        return;
                    }
                    int amount = Integer.parseInt(args[1]);

                    if (discordRaffle != null) {
                        getSunnyBot().messageChannel(DiscordUtil.CHANNEL_GENERAL,
                                "**Raffle draw** Congratulations to " + discordRaffle.draw(amount) + "!");
                    } else
                        getSunnyBot().messageChannel(textChannel, "There is no current raffle.");

                } else
                    getSunnyBot().messageChannel(textChannel, "Correct usage: !" + getName() + " draw <amount>");

                break;
            case "blacklist":
                getSunnyBot().messageChannel(textChannel, "Coming soon... "); // since 1 year ago.
                break;
            case "toggle":
                if (discordRaffle != null) {
                    discordRaffle.setClosed(!discordRaffle.isClosed());
                    getSunnyBot().messageChannel(textChannel, "Raffle entries " + (discordRaffle.isClosed() ? "closed" : "opened"));
                    return;
                }
                getSunnyBot().messageChannel(textChannel, "There is currently no raffle going on!");
                break;
            case "info":
                if (discordRaffle != null) {
                    getSunnyBot().messageChannel(textChannel, "Participants: " + discordRaffle.getParticipants().size() + "\n" +
                            "Keyword: `" + discordRaffle.getKeyWord() + "`");
                } else
                    getSunnyBot().messageChannel(textChannel, "There is no current raffle going on!");


        }

    }

}
