package de.elliepotato.sunnybot.command.admin;

import com.google.common.base.Joiner;
import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ellie on 07/05/2017 for YT-er Sunny.
 */
public class CmdBlacklist extends SunnyCommand {

    public CmdBlacklist(SunnyBot sunnyBot) {
        super(sunnyBot, "blacklist", "Blacklist a user from usage of the bot.", Permission.BAN_MEMBERS, "blacklist <list | user|id>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        final TextChannel textChannel = e.getChannel();
        final List<Long> blacklist = getSunnyBot().getSqlData().getBotBlacklist().getBlacklist();

        if (args[0].equalsIgnoreCase("list")) {
            getSunnyBot().messageChannel(textChannel, "Blacklisted users: " + Joiner.on(", ").join(blacklist));
            return;
        }

        User toBlacklist;
        long blacklistId = -1;
        try {
            long id = Long.parseLong(args[0]);
            toBlacklist = e.getJDA().getUserById(id);

            if (toBlacklist == null)
                blacklistId = id;

        } catch (NumberFormatException ex) {
            toBlacklist = getSunnyBot().parseUser(args[0]);

            if (toBlacklist == null) {
                getSunnyBot().messageChannel(textChannel, "User not found: `" + args[0] + "`.");
                return;
            }
        }

        if ((toBlacklist != null && blacklist.contains(toBlacklist.getIdLong())) || blacklist.contains(blacklistId)) {
            getSunnyBot().getSqlData().removeBlacklist((toBlacklist != null ? toBlacklist.getIdLong() : blacklistId));
            getSunnyBot().messageChannel(textChannel, "Un-blacklisted ID " + (toBlacklist != null ? toBlacklist.getName() : blacklistId));
        } else {
            getSunnyBot().getSqlData().addBlacklist((toBlacklist != null ? toBlacklist.getIdLong() : blacklistId));
            getSunnyBot().messageChannel(textChannel, "Blacklisted ID " + (toBlacklist != null ? toBlacklist.getName() : blacklistId));
        }

    }

}
