package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ellie on 07/05/2017 for YT-er Sunny.
 */
public class CmdClear extends SunnyCommand {

    public CmdClear(SunnyBot sunnyBot) {
        super(sunnyBot, "clear", "Clear x amount of messages", Permission.MESSAGE_MANAGE, "clear <amount>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        final TextChannel textChannel = e.getChannel();

        if (!DiscordUtil.isInteger(args[0])) {
            getSunnyBot().messageChannel(textChannel, "Invalid amount `" + args[0] + "`.");
            return;
        }
        int amount = Integer.parseInt(args[0]);

        e.getChannel().getHistory().retrievePast(amount + 1).queue(messages -> {
            try {
                e.getChannel().deleteMessages(messages).queue(success ->
                        getSunnyBot().tempMessage(e.getChannel(), "Cleared " + amount + " messages. <:suckface:309619070917935104>", 5, null));
            } catch (IllegalArgumentException ex) {
                getSunnyBot().tempMessage(e.getChannel(), "Some of the messages are older than 2 weeks so can't delete 'em all :(", 7, null);
            }
        });

    }

}
