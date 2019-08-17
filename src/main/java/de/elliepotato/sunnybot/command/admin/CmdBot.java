package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 12/05/2017 for YT-er Sunny.
 */
public class CmdBot extends SunnyCommand {

    public CmdBot(SunnyBot sunnyBot) {
        super(sunnyBot, "bot", "Bot information", Permission.KICK_MEMBERS, "bot");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {

    }

}
