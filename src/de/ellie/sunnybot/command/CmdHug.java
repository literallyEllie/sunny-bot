package de.ellie.sunnybot.command;

import de.ellie.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by Ellie on 05/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdHug extends SunnyCommand {

    public CmdHug(){
        super("hug", "Show them who's boss!", Permission.MESSAGE_WRITE, "hug <member>");
        getAliases().add("slap");
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        if(args.length != 2){
            e.getChannel().sendMessage(correctUsage()).queue();
            return;
        }

        System.out.println(args[1].matches(DiscordUtil.PATTERN_USER_ID));
        String id = DiscordUtil.stripInputUser(args[1]);
        if(!args[1].matches(DiscordUtil.PATTERN_USER_ID) || e.getJDA().getUserById(id) == null){
            e.getChannel().sendMessage("The member `"+args[1]+"` was not found.").queue();
            return;
        }

        e.getChannel().sendMessage(args[1]+" **big hugs** from "+e.getAuthor().getName() +" :heart:").queue();

    }

}
