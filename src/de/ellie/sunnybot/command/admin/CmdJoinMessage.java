package de.ellie.sunnybot.command.admin;

import de.ellie.sunnybot.SunnyBot;
import de.ellie.sunnybot.util.DiscordUtil;
import de.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 06/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdJoinMessage extends SunnyCommand {

    private String join;
    private boolean enabled;

    public CmdJoinMessage() {
        super("joinmsg", "Join message options", Permission.KICK_MEMBERS, "joinmsg [toggle | <message>]");
        this.join = SunnyBot.getSunnyBot().getSqlManager().getJoinMessage();
        this.enabled = SunnyBot.getSunnyBot().getSqlManager().isJoinEnabled();
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if(args.length == 1){
            textChannel.sendMessage(correctUsage()).queue();
            textChannel.sendMessage("Current join message: `"+join+"`\n" +
                    "Enabled "+enabled).queue();
            return;
        }

        if(args.length > 1){
            if(args[1].equalsIgnoreCase("toggle")){
                enabled = !enabled;
                textChannel.sendMessage("Join message "+(enabled ? "enabled" : "disabled")).queue();
                updateToggled(enabled);
            }else{
                String message = DiscordUtil.getFinalArg(args, 1);
                textChannel.sendMessage("Join message set to `"+message+"`");
                join = message;
                updateMessage();
            }
        }
    }

    private void updateMessage(){
        SunnyBot sunnyBot = SunnyBot.getSunnyBot();
        sunnyBot.getOtherListener().setJoin(join);
        sunnyBot.getSqlManager().setJoinMsg(join);
    }

    private void updateToggled(boolean enabled){
        SunnyBot sunnyBot = SunnyBot.getSunnyBot();
        sunnyBot.getOtherListener().setJoin("");
        sunnyBot.getSqlManager().setJoinEnabled(enabled);
    }

}
