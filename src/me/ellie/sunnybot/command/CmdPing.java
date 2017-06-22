package me.ellie.sunnybot.command;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 06/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdPing extends SunnyCommand {

    public CmdPing() {
        super("ping", "Pong!", Permission.MESSAGE_WRITE, "ping");
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        long before = System.currentTimeMillis();
        e.getChannel().sendMessage("...")
                .queue(message -> message.editMessage("Pong! ("+(System.currentTimeMillis()-before)+"ms)").queue());
    }
}
