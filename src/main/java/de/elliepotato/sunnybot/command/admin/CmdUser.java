package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;

/**
 * Created by Ellie on 13/03/2018 for YT-er Sunny.
 */
public class CmdUser extends SunnyCommand {

    public CmdUser(SunnyBot bot) {
        super(bot, "user", "Shows information about the user", Permission.KICK_MEMBERS, "<userid>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            getSunnyBot().messageChannel(e.getChannel(), "Invalid id!");
            return;
        }

        User user = e.getJDA().getUserById(id);
        if (user == null) {
            getSunnyBot().messageChannel(e.getChannel(), "Couldn't find user with that ID.");
            return;
        }

        getSunnyBot().messageChannel(e.getChannel(), "**" + user.getName() + "#" + user.getDiscriminator() + "** has existed on this :earth_africa: since "
                + user.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
    }

}
