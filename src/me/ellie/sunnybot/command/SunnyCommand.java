package me.ellie.sunnybot.command;

import me.ellie.sunnybot.SunnyBot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public abstract class SunnyCommand {

    private final String name, description;
    private final Permission permission;
    private final String syntax;
    private boolean enabled;
    private final ArrayList<String> aliases;
    private int cooldown = -1;

    protected SunnyCommand(String name, String description, Permission permission, String syntax) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.syntax = syntax;
        this.enabled = true;
        this.aliases = new ArrayList<>();
    }

    protected abstract void onCommand(GuildMessageReceivedEvent e, String[] args);

    public void execute(GuildMessageReceivedEvent e, String[] args){

        if(enabled){
            if(PermissionUtil.checkPermission(e.getChannel(), e.getMember(), permission)){
                try {
                    SunnyBot.getSunnyBot().logInfo(e.getAuthor().getName()+"#"+e.getAuthor().getDiscriminator()+" executed command "+
                    getName()+" with args "+ Arrays.toString(args));
                    onCommand(e, args);
                }catch(Exception ex) {
                    SunnyBot.getSunnyBot().logError("Failed to execute command " + name + "!");
                    ex.printStackTrace();
                    e.getChannel().sendMessage("Owchie... something went wrong. Quick, call the medics! (nerd stuff: " + ex.getCause() + ": " + ex.getMessage() + ")").queue();
                }
            }else{
                e.getChannel().sendMessage("You need the permission "+permission.toString()+" to execute this command!").queue();
            }
        }else{
            e.getChannel().sendMessage("That command is being prevented from running by some unknown mystical force...").queue();
        }
    }

    public String correctUsage(){
        return "Correct usage: "+SunnyBot.getSunnyBot().getSunnyConfig().getBotPrefix().trim()+syntax+" - "+description;
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

}
