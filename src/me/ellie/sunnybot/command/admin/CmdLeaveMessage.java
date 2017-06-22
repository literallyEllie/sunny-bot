package me.ellie.sunnybot.command.admin;

import me.ellie.sunnybot.SunnyBot;
import me.ellie.sunnybot.command.SunnyCommand;
import me.ellie.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 06/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdLeaveMessage extends SunnyCommand {

    private String leave;
    private boolean enabled;

    public CmdLeaveMessage() {
        super("leavemsg", "Leave message options", Permission.KICK_MEMBERS, "leavemsg [toggle | <message>]");
        this.leave = SunnyBot.getSunnyBot().getSqlManager().getLeaveMsg();
        this.enabled = SunnyBot.getSunnyBot().getSqlManager().isLeaveEnabled();
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if(args.length == 1){
            textChannel.sendMessage(correctUsage()).queue();
            textChannel.sendMessage("Current leave message: `"+leave+"`\n" +
                    "Enabled "+enabled).queue();
            return;
        }

        if(args.length > 1){
            if(args[1].equalsIgnoreCase("toggle")){
                enabled = !enabled;
                textChannel.sendMessage("Leave message "+(enabled ? "enabled" : "disabled")).queue();
                updateToggled(enabled);
            }else{
                String message = DiscordUtil.getFinalArg(args, 1);
                textChannel.sendMessage("Leave message set to `"+message+"`");
                leave = message;
                updateMessage();
            }
        }
    }

    private void updateMessage(){
        SunnyBot sunnyBot = SunnyBot.getSunnyBot();
        sunnyBot.getOtherListener().setLeave(leave);
        sunnyBot.getSqlManager().setLeaveMsg(leave);
    }

    private void updateToggled(boolean enabled){
        SunnyBot sunnyBot = SunnyBot.getSunnyBot();
        sunnyBot.getOtherListener().setLeave("");
        sunnyBot.getSqlManager().setLeaveEnabled(enabled);
    }

}
