package me.ellie.sunnybot.command.admin;

import me.ellie.sunnybot.SunnyBot;
import me.ellie.sunnybot.command.SunnyCommand;
import me.ellie.sunnybot.util.DiscordRaffle;
import me.ellie.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ellie on 05/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdRaffle extends SunnyCommand {

    private DiscordRaffle discordRaffle;

    public CmdRaffle(){
        super("raffle", "Create a raffle", Permission.BAN_MEMBERS, "raffle <start <keyword> | draw <amount> | cancel | info | blacklist <user>");
    }

    private final List<String> subCommands = Arrays.asList("start", "cancel", "draw", "blacklist", "toggle", "info");

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        TextChannel textChannel = e.getChannel();

        // raffle start
        if(args.length < 2){
            textChannel.sendMessage(correctUsage()).queue();
            return;
        }

        String sub = args[1].toLowerCase();
        if(!subCommands.contains(sub)) textChannel.sendMessage(correctUsage()).queue();

        switch (sub){
            case "start":
                if(discordRaffle != null){
                    textChannel.sendMessage("A raffle is currently on going!").queue();
                    return;
                }

                // raffle start keyword
                if(args.length == 3){
                    discordRaffle = new DiscordRaffle(args[2]);
                    SunnyBot.getSunnyBot().getCommandListener().setDiscordRaffle(discordRaffle);
                    e.getGuild().getTextChannelById(DiscordUtil.GENERAL).sendMessage("A raffle has been started! Type `" +
                            args[2]+"` to enter!").queue();
                    return;
                }else{
                    textChannel.sendMessage("Correct usage: !"+getName()+" start <keyword>").queue();
                }
                break;
            case "cancel":
                if(discordRaffle != null) {
                    e.getGuild().getTextChannelById(DiscordUtil.GENERAL).sendMessage("The raffle has been cancelled!").queue();
                    SunnyBot.getSunnyBot().getCommandListener().setDiscordRaffle(null);
                    discordRaffle.finish();
                    discordRaffle = null;
                    return;
                }
                textChannel.sendMessage("There is no current raffle.").queue();
                break;
            case "draw":
                if(args.length == 3){
                    int amount;
                    try{
                        amount = Integer.parseInt(args[2]);
                    }catch(NumberFormatException ex){
                        textChannel.sendMessage("Invalid integer "+args[2]).queue();
                        return;
                    }
                    if(discordRaffle != null){
                        e.getGuild().getTextChannelById(DiscordUtil.GENERAL).sendMessage("[Raffle] Congratulations "+discordRaffle.draw(amount)+"!").queue();
                    }else{
                        textChannel.sendMessage("There is no current raffle.").queue();
                    }
                }else{
                    textChannel.sendMessage("Correct usage: !"+getName()+" draw <amount>").queue();
                }
                break;
            case "blacklist":
                textChannel.sendMessage("Coming soon...").queue();
                break;
            case "toggle":
                if(discordRaffle != null){
                    discordRaffle.setClosed(!discordRaffle.isClosed());
                    textChannel.sendMessage("Raffle entries "+(discordRaffle.isClosed() ? "closed" :"opened")).queue();
                }
                break;
            case "info":
                if(discordRaffle != null){
                    textChannel.sendMessage("Participants: "+discordRaffle.getParticipants().size()+"\n" +
                            "Keyword: `"+ discordRaffle.getKeyWord()+"`").queue();
                }else{
                    textChannel.sendMessage("There is no current raffle.").queue();
                }
        }

    }

}
