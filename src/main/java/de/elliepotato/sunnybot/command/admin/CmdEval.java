package de.elliepotato.sunnybot.command.admin;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.command.SunnyCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class CmdEval extends SunnyCommand {

    private final ScriptEngine scriptEngine;

    public CmdEval(SunnyBot sunnyBot) {
        super(sunnyBot, "eval", "Eval engine", Permission.BAN_MEMBERS, "eval <message>");

        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            scriptEngine.eval("var imports = new JavaImporter(java.io, java.lang, java.staff, Packages.net.dv8tion.jda.core, " +
                    "Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers);");
            scriptEngine.put("sunny", sunnyBot);
        } catch (ScriptException e) {
            sunnyBot.getLogger().error("Failed to setup eval manager!", e);
            e.printStackTrace();
        }
    }


    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        scriptEngine.put("jda", e.getJDA());
        scriptEngine.put("e", e);
        Object output;
        String input = e.getMessage().getContentDisplay().substring(getSunnyBot().getSunnyConfig().getBotPrefix().length());
        input = input.substring(input.indexOf(" "));
        try {
            output = scriptEngine.eval("(function() { with (imports) {\n" + input + "\n} })();");
        } catch (ScriptException ex) {
            getSunnyBot().tempMessage(e.getChannel(), "An error occurred whilst trying to execute that: " + ex.getMessage(), 7, null);
            return;
        }
        String outputString;
        if (output == null) {
            outputString = "Executed successfully with no output.";
        } else {
            outputString = output.toString();
            if (outputString.length() >= 2048) {
                getSunnyBot().tempMessage(e.getChannel(), "The output exceeds the Discord message limit.", 7, null);
                return;
            }
        }

        getSunnyBot().tempMessage(e.getChannel(), outputString, 10, null);
    }

}
