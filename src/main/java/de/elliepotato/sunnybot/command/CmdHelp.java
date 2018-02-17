package de.elliepotato.sunnybot.command;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.UtilEmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Collection;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class CmdHelp extends SunnyCommand {

    private UtilEmbedBuilder embedBuilder;

    public CmdHelp(SunnyBot sunnyBot) {
        super(sunnyBot, "help", "Bot help", Permission.MESSAGE_WRITE, "help [staff]");
        embedBuilder = new UtilEmbedBuilder();
        embedBuilder.setTitle("Help for SunnyBot", null)
                .setColor(new Color(0x7B1A36))
                .setDescription("This is a bot made for the official Sunny discord!")
                .setFooter("Developed by Ellie#0006", null);
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        final Collection<SunnyCommand> commands = getSunnyBot().getCommandManager().getCommands().values();
        final String prefix = getSunnyBot().getSunnyConfig().getBotPrefix();

        final StringBuilder commandStr = new StringBuilder();
        embedBuilder.removeField("Commands");

        if (args.length == 2 && args[1].equalsIgnoreCase("staff") && e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            commands.stream().filter(sunnyCommand -> sunnyCommand.isEnabled() && sunnyCommand.getPermission() != Permission.MESSAGE_WRITE).forEach(sunnyCommand ->
                    commandStr.append("- ").append(prefix).append(sunnyCommand.getName()).append(" | ").append(sunnyCommand.getDescription()).append("\n"));
        } else {
            commands.stream().filter(sunnyCommand -> sunnyCommand.isEnabled() && sunnyCommand.getPermission() == Permission.MESSAGE_WRITE).forEach(sunnyCommand ->
                    commandStr.append("- ").append(prefix).append(sunnyCommand.getName()).append(" | ").append(sunnyCommand.getDescription()).append("\n"));
            commandStr.append("\nDo !help staff for staff commands...");
        }
        embedBuilder.addField("Commands", commandStr.toString(), false);

        getSunnyBot().messageChannel(e.getChannel(), embedBuilder.build());
    }
}
