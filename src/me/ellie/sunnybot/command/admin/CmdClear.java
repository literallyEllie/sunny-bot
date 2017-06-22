package me.ellie.sunnybot.command.admin;

import me.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 07/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdClear extends SunnyCommand {

    public CmdClear() {
        super("clear", "Clear x amount of messages", Permission.KICK_MEMBERS, "clear <amount>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if (args.length != 2) {
            textChannel.sendMessage(correctUsage()).queue();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            textChannel.sendMessage("Invalid amount").queue();
            return;
        }

        e.getChannel().getHistory().retrievePast(amount + 1).queue(messages -> {
            try {
                e.getChannel().deleteMessages(messages).queue(success ->
                        e.getChannel().sendMessage("Cleared " + amount + " messages.").queue());
            } catch (IllegalArgumentException ex) {
                e.getChannel().sendMessage("Some of the messages are older than 2 weeks so can't delete 'em all :(").queue();
            }
        });

    }

}
