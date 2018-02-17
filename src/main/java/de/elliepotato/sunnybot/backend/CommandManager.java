package de.elliepotato.sunnybot.backend;

import com.google.common.collect.Maps;
import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.*;
import de.elliepotato.sunnybot.util.CommandCooldown;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class CommandManager extends ListenerAdapter {

    private final SunnyBot sunnyBot;
    private String botPrefix;

    private Map<String, SunnyCommand> commands;
    private HashMap<String, CommandCooldown> cooldowns;

    public CommandManager(SunnyBot sunnyBot, String botPrefix) {
        this.sunnyBot = sunnyBot;
        this.botPrefix = botPrefix;
        this.commands = Maps.newHashMap();
        this.cooldowns = Maps.newHashMap();

        new Reflections("de.elliepotato.sunnybot.command")
                .getSubTypesOf(SunnyCommand.class)
                .forEach(aClass -> {
                    try {
                        SunnyCommand command = (SunnyCommand) aClass.getConstructors()[0].newInstance(sunnyBot);
                        commands.put(command.getName().toLowerCase(), command);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        sunnyBot.getLogger().error("Failed to load command " + aClass.getSimpleName() + "!");
                        e.printStackTrace();
                    }

                });

        sunnyBot.getLogger().info("Loaded " + commands.size() + " commands!");
    }

    public void finish() {
        if (commands != null)
            commands.clear();

        if (cooldowns != null)
            cooldowns.clear();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();
        final String msg = message.getContentRaw();

        if (!msg.startsWith(botPrefix)
                || msg.length() <= botPrefix.length())
            return;

        final Member member = event.getMember();
        if (member.getUser().getIdLong() == DiscordUtil.BOT_SELF || member.getUser().isBot()
                || sunnyBot.getSqlData().getBotBlacklist().isBlacklist(member.getUser().getIdLong()))
            return; // dont reply to self or bots.

        final String[] argsWLabel = msg.substring(botPrefix.length()).split(" ");

        final SunnyCommand command = getCommand(argsWLabel[0]);
        if (command == null) {
            sunnyBot.raffleEnter(event.getAuthor(), msg);
            return;
        }

        final CommandCooldown commandCooldown = getCooldown(command);

        if (commandCooldown != null) {
            if (commandCooldown.finished()) {
                removeCooldown(command);
            } else if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
                sunnyBot.tempMessage(event.getChannel(),
                        "This command is currently on cool-down! (" + commandCooldown.getRemaining() + "s left).", 5, null);
                return;
            }
        }

        // add exempt to staff bc its actually really annoying
        if (command.getCooldown() != -1 && !member.hasPermission(Permission.MESSAGE_MANAGE)) {
            addCooldown(command);
        }

        String[] argsNoLabel = null;
        if (argsWLabel.length > 1) {
            argsNoLabel = new String[argsWLabel.length - 1];
            System.arraycopy(argsWLabel, 1, argsNoLabel, 0, argsNoLabel.length);
        }

        command.execute(event, (argsNoLabel == null ? new String[0] : argsNoLabel));
    }

    public SunnyCommand getCommand(String command) {
        SunnyCommand sunnyCommand = commands.get(command.toLowerCase());
        if (sunnyCommand != null) return sunnyCommand;

        for (SunnyCommand cmd : commands.values()) {
            if (cmd.getAliases().contains(command.toLowerCase())) return cmd;
        }
        return null;
    }

    public String getBotPrefix() {
        return botPrefix;
    }

    public Map<String, SunnyCommand> getCommands() {
        return commands;
    }


    private CommandCooldown getCooldown(SunnyCommand command) {
        return cooldowns.get(command.getName().toLowerCase());
    }

    private void addCooldown(SunnyCommand sunnyCommand) {
        cooldowns.put(sunnyCommand.getName().toLowerCase(), new CommandCooldown(sunnyCommand.getCooldown()));
    }

    private void removeCooldown(SunnyCommand sunnyCommand) {
        cooldowns.remove(sunnyCommand.getName().toLowerCase());
    }



}
