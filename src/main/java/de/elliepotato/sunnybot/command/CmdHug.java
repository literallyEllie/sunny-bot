package de.elliepotato.sunnybot.command;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 05/05/2017 for YT-er Sunny.
 */
public class CmdHug extends SunnyCommand {

    public CmdHug(SunnyBot sunnyBot) {
        super(sunnyBot, "hug", "Hug your buds", Permission.MESSAGE_WRITE, "hug <member>");
        getAliases().add("slap");
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {

        final User user = getSunnyBot().parseUser(args[0]);
        if (user == null) {
            getSunnyBot().messageChannel(e.getChannel(), "The member `" + args[0] + "` was not found.");
            return;
        }

        getSunnyBot().messageChannel(e.getChannel(), ":heart: " + user.getAsMention() + " **big hugs** from " +
                e.getAuthor().getName() + " :heart:");
    }

}
