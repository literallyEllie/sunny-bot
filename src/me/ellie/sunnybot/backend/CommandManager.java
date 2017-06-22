package me.ellie.sunnybot.backend;

import me.ellie.sunnybot.command.*;
import me.ellie.sunnybot.command.admin.*;

import java.util.HashMap;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CommandManager {

    private String botPrefix;

    private HashMap<String, SunnyCommand> commands;

    public CommandManager(String botPrefix) {
        this.botPrefix = botPrefix;
        commands = new HashMap<>();
        loadCommands();
    }

    private void loadCommands() {
        commands.put("shutdown", new CmdShutdown());
        commands.put("social", new CmdSocial());
        commands.put("ping", new CmdPing());
        commands.put("eval", new CmdEval());
        commands.put("help", new CmdHelp());
        commands.put("hug", new CmdHug());
        commands.put("raffle", new CmdRaffle());
        commands.put("guild", new CmdGuild());
        commands.put("blacklist", new CmdBlacklist());
        commands.put("leavemsg", new CmdLeaveMessage());
        commands.put("joinmsg", new CmdJoinMessage());
        commands.put("clear", new CmdClear());
    }

    public void finish() {
        if (commands != null) {
            commands.clear();
        }
    }

    public SunnyCommand getCommand(String command) {
        SunnyCommand sunnyCommand = commands.get(command.toLowerCase());
        if (sunnyCommand != null) return sunnyCommand;

        System.out.println(command);
        for (SunnyCommand cmd : commands.values()) {
            if (cmd.getAliases().contains(command.toLowerCase())) return cmd;
        }
        return null;
    }

    public boolean isCommand(String command) {
        boolean a = commands.containsKey(command.toLowerCase());
        if (!a) {
            for (SunnyCommand cmd : commands.values()) {
                if(cmd.getAliases().contains(command.toLowerCase())) return true;
            }
        }
        return a;
    }

    public String getBotPrefix() {
        return botPrefix;
    }

    public HashMap<String, SunnyCommand> getCommands() {
        return commands;
    }
}
