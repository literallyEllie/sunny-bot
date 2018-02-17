package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 06/05/2017 for YT-er Sunny.
 */
public class CmdLeaveMessage extends SunnyCommand {

    private String leave;
    private boolean enabled;

    public CmdLeaveMessage(SunnyBot sunnyBot) {
        super(sunnyBot,"leavemsg", "Leave message options", Permission.KICK_MEMBERS, "leavemsg [toggle | <message>]");
        this.leave = getSunnyBot().getSqlData().getLeaveMsg();
        this.enabled = getSunnyBot().getSqlData().isLeaveEnabled();
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if (args.length == 0) {
            textChannel.sendMessage(correctUsage()).queue();
            textChannel.sendMessage("Current leave message: `" + leave + "`\n" +
                    "Enabled " + enabled).queue();
            return;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            enabled = !enabled;
            textChannel.sendMessage("Leave message " + (enabled ? "enabled" : "disabled")).queue();
            updateToggled(enabled);
        } else {
            final String message = DiscordUtil.getFinalArg(args, 1);
            getSunnyBot().tempMessage(e.getChannel(), "<:ellie:309625495400611840> Leave message sent to `" + message + "`", 5,
                    e.getMessage());
            leave = message;
            updateMessage();
        }

    }

    private void updateMessage() {
        getSunnyBot().getUserLeaveJoinListener().setLeave(leave);
        getSunnyBot().getSqlData().setLeaveMsg(leave);
    }

    private void updateToggled(boolean enabled) {
        getSunnyBot().getUserLeaveJoinListener().setLeave("");
        getSunnyBot().getSqlData().setLeaveEnabled(enabled);
    }

}
