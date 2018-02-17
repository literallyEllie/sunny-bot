package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class CmdShutdown extends SunnyCommand {

    public CmdShutdown(SunnyBot sunnyBot) {
        super(sunnyBot, "shutdown", "Shutdown the bot", Permission.BAN_MEMBERS, "shutdown");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("Ha det! <:rappin:383735839118721024>").queue();
        try {
            Thread.sleep(7);
        } catch (Exception ignored) {
        } finally {
            getSunnyBot().shutdown(0);
        }

    }
}
