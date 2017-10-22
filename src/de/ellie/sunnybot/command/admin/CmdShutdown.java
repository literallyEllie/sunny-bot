package de.ellie.sunnybot.command.admin;

import de.ellie.sunnybot.SunnyBot;
import de.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdShutdown extends SunnyCommand {

    public CmdShutdown(){
        super("shutdown", "Shutdown the bot", Permission.BAN_MEMBERS, "shutdown");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("Ha det!").queue();
        try {
            Thread.sleep(7);
        }catch(Exception ignored){
        }finally {
            SunnyBot.getSunnyBot().shutdown(0);
        }

    }
}
