package de.elliepotato.sunnybot.command;

import de.elliepotato.sunnybot.SunnyBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 06/05/2017 for YT-er Sunny.
 */
public class CmdPing extends SunnyCommand {

    public CmdPing(SunnyBot sunnyBot) {
        super(sunnyBot, "ping", "Pong!", Permission.MESSAGE_WRITE, "ping");
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        long before = System.currentTimeMillis();
        e.getChannel().sendMessage("...")
                .queue(message -> message.editMessage("Pong! (" + (System.currentTimeMillis() - before) + "ms) <:huh:309619682258976769>").queue());
    }
}
