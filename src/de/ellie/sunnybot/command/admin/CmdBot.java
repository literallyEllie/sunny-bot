package de.ellie.sunnybot.command.admin;

import de.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 12/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdBot extends SunnyCommand {

    public CmdBot(){
        super("bot", "Bot information", Permission.KICK_MEMBERS, "bot");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {

    }

}
