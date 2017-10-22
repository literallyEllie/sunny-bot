package de.ellie.sunnybot.command.admin;

import de.ellie.sunnybot.SunnyBot;
import de.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ellie on 07/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdBlacklist extends SunnyCommand {

    public CmdBlacklist(){
        super("blacklist", "Blacklist a user from usage of the bot.", Permission.BAN_MEMBERS, "blacklist <list | user|id>");
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        if(args.length != 2){
            textChannel.sendMessage(correctUsage()).queue();
            return;
        }

        args = e.getMessage().getRawContent().split(" ");

        List<Long> blacklist = SunnyBot.getSunnyBot().getSqlManager().getBlacklist();

        if(args[1].equalsIgnoreCase("list")){
            textChannel.sendMessage("Blacklisted users: "+Arrays.toString(blacklist.toArray()).replace("[", "").replace("]", "")).queue();
            return;
        }

        long id;
        try{
            id = Long.parseLong(args[1]);
        }catch(NumberFormatException ex){
            try {
                id = Long.parseLong(args[1].replace("<", "").replace(">", "").replace("@", ""));
            }catch(NumberFormatException ext){
                textChannel.sendMessage("Invalid input.").queue();
                return;
            }
        }

        if(blacklist.contains(id)){
            SunnyBot.getSunnyBot().getSqlManager().removeBlacklist(id);
            textChannel.sendMessage("Unblacklisted ID "+e.getJDA().getUserById(""+id).getName()).queue();
        }else{
            SunnyBot.getSunnyBot().getSqlManager().addBlacklist(id);
            textChannel.sendMessage("Blacklisted ID "+e.getJDA().getUserById(""+id).getName()).queue();
        }


    }
}
