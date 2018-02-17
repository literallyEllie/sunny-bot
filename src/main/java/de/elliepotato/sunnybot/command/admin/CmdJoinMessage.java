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
public class CmdJoinMessage extends SunnyCommand {

    private String join;
    private boolean enabled;

    public CmdJoinMessage(SunnyBot sunnyBot) {
        super(sunnyBot, "joinmsg", "Join message options", Permission.KICK_MEMBERS, "joinmsg [toggle | <message>]");
        this.join = getSunnyBot().getSqlData().getJoinMessage();
        this.enabled = getSunnyBot().getSqlData().isJoinEnabled();
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if (args.length == 0) {
            textChannel.sendMessage(correctUsage()).queue();
            textChannel.sendMessage("Current join message: `" + join + "`\n" +
                    "Enabled " + enabled).queue();
            return;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            enabled = !enabled;
            textChannel.sendMessage("Join message " + (enabled ? "enabled" : "disabled")).queue();
            updateToggled(enabled);
        } else {
            String message = DiscordUtil.getFinalArg(args, 1);
            getSunnyBot().tempMessage(e.getChannel(), "<:ellie:309625495400611840> Join message sent to `" + message + "`", 5,
                    e.getMessage());
            join = message;
            updateMessage();
        }

    }

    private void updateMessage() {
        getSunnyBot().getUserLeaveJoinListener().setJoin(join);
        getSunnyBot().getSqlData().setJoinMsg(join);
    }

    private void updateToggled(boolean enabled) {
        getSunnyBot().getUserLeaveJoinListener().setJoin("");
        getSunnyBot().getSqlData().setJoinEnabled(enabled);
    }

}
