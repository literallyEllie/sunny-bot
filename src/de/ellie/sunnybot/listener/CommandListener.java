package de.ellie.sunnybot.listener;

import de.ellie.sunnybot.SunnyBot;
import de.ellie.sunnybot.backend.CommandManager;
import de.ellie.sunnybot.util.CommandCooldown;
import de.ellie.sunnybot.util.DiscordRaffle;
import de.ellie.sunnybot.util.DiscordUtil;
import de.ellie.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CommandListener extends ListenerAdapter {

    private CommandManager commandManager;
    private HashMap<String, CommandCooldown> cooldowns;

    private DiscordRaffle discordRaffle;

    public CommandListener(CommandManager commandManager){
        this.commandManager = commandManager;
        cooldowns = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel channel = event.getChannel();
        String raw = event.getMessage().getRawContent();

        if (user.getId().equals(DiscordUtil.SELF) || SunnyBot.getSunnyBot().getSqlManager().getBlacklist().contains(Long.parseLong(user.getId()))) return;

        if(!isCommand(raw) && discordRaffle != null && !discordRaffle.isClosed()){
            discordRaffle.add(Long.parseLong(user.getId()));
        }

        if (!isCommand(raw)) return;

        String[] args = raw.substring(commandManager.getBotPrefix().trim().length()).split(" ");
        if (!commandManager.isCommand(args[0])) return;
        SunnyCommand sunnyCommand = commandManager.getCommand(args[0]);

        if (cooldowns.containsKey(sunnyCommand.getName().toLowerCase())) {
            if (cooldowns.get(sunnyCommand.getName().toLowerCase()).finished()) {
                cooldowns.remove(sunnyCommand.getName().toLowerCase());
            } else {
                channel.sendMessage("This command is currently on cooldown! (" + cooldowns.get(sunnyCommand.getName().toLowerCase()).getRemaining() + "s left)").queue();
                return;
            }
        }
        if (sunnyCommand.getCooldown() != -1) {
            cooldowns.put(sunnyCommand.getName().toLowerCase(), new CommandCooldown(sunnyCommand.getCooldown()));
        }
        commandManager.getCommand(args[0]).execute(event, args);
    }


    public boolean isCommand(String raw){
        return raw.startsWith(commandManager.getBotPrefix().trim());
    }

    public HashMap<String, CommandCooldown> getCooldowns() {
        return cooldowns;
    }

    public DiscordRaffle getDiscordRaffle() {
        return discordRaffle;
    }

    public void setDiscordRaffle(DiscordRaffle discordRaffle) {
        this.discordRaffle = discordRaffle;
    }

}
