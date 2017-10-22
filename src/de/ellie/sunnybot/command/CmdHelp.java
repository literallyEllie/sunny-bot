package de.ellie.sunnybot.command;

import de.ellie.sunnybot.SunnyBot;
import de.ellie.sunnybot.util.UtilEmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Collection;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdHelp extends SunnyCommand {

    private UtilEmbedBuilder embedBuilder;

    public CmdHelp() {
        super("help", "Bot help", Permission.MESSAGE_WRITE, "help [staff]");
        embedBuilder = new UtilEmbedBuilder();
        embedBuilder.setTitle("Help for SunnyBot", null);
        embedBuilder.setColor(new Color(0x7B1A36));
        embedBuilder.setDescription("This is a bot made for the Sunny discord!");
        embedBuilder.setFooter("Developed by Ellie#0006", null);
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        Collection<SunnyCommand> commands = SunnyBot.getSunnyBot().getCommandManager().getCommands().values();
        String prefix = SunnyBot.getSunnyBot().getCommandManager().getBotPrefix();

        StringBuilder commandStr = new StringBuilder();
        embedBuilder.removeField("Commands");

        if(args.length == 2 && args[1].equalsIgnoreCase("staff") && e.getMember().hasPermission(Permission.KICK_MEMBERS)){
            commands.stream().filter(sunnyCommand -> sunnyCommand.isEnabled() && sunnyCommand.getPermission() != Permission.MESSAGE_WRITE).forEach(sunnyCommand ->
                    commandStr.append("- ").append(prefix).append(sunnyCommand.getName()).append(" | ").append(sunnyCommand.getDescription()).append("\n"));
        }else {
            commands.stream().filter(sunnyCommand -> sunnyCommand.isEnabled() && sunnyCommand.getPermission() == Permission.MESSAGE_WRITE).forEach(sunnyCommand ->
                    commandStr.append("- ").append(prefix).append(sunnyCommand.getName()).append(" | ").append(sunnyCommand.getDescription()).append("\n"));
            commandStr.append("\nDo !help staff for staff commands...");
        }
        embedBuilder.addField("Commands", commandStr.toString(), false);

        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
