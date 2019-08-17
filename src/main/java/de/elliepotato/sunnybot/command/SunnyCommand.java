package de.elliepotato.sunnybot.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.elliepotato.sunnybot.SunnyBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public abstract class SunnyCommand {

    private final SunnyBot sunnyBot;

    private final String name, description;
    private final Permission permission;
    private final String[] syntax;
    private final List<String> aliases;
    private boolean enabled;
    private int cooldown = -1, minArgs;

    protected SunnyCommand(SunnyBot sunnyBot, String name, String description, Permission permission, String... syntax) {
        this.sunnyBot = sunnyBot;
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.syntax = syntax;
        this.aliases = Lists.newArrayList();
        this.enabled = true;
        this.minArgs = (int) Arrays.stream(syntax).filter(s -> s.contains("<")).count();
    }

    protected abstract void onCommand(GuildMessageReceivedEvent e, String[] args);

    public final void execute(GuildMessageReceivedEvent e, String[] args) {
        if (!enabled) {
            sunnyBot.tempMessage(e.getChannel(), "That command is being prevented from running by some unknown mystical force... :yeh:", 5, null);
            return;
        }

        if (!PermissionUtil.checkPermission(e.getChannel(), e.getMember(), permission)) {
            sunnyBot.tempMessage(e.getChannel(),
                    e.getMember().getAsMention() + ", you need the permission " + permission.toString() + " to execute this command! <:kappa:311533388429525003>",
                    5, null);
            return;
        }

        if (args.length < minArgs) {
            sunnyBot.messageChannel(e.getChannel(), correctUsage());
            return;
        }

        try {
            sunnyBot.getLogger().info(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " executed command " +
                    getName() + " with args: " + Joiner.on(", ").join(args));
            onCommand(e, args);
        } catch (Throwable ex) {
            sunnyBot.getLogger().error("Failed to execute command " + name + "!", e);
            ex.printStackTrace();
            getSunnyBot().messageChannel(e.getChannel(), "Owchie... something went wrong. Quick, call the medics! (nerd stuff: " + ex.getCause() + ": " + ex.getMessage() + ") <:cringe:334422469878349835>");
        }

    }

    public final String correctUsage() {
        return "<:huh:309619682258976769> Correct usage for this command: " + sunnyBot.getSunnyConfig().getBotPrefix().trim() + Joiner.on(" ").join(syntax) + " **-** " + description;
    }

    protected final SunnyBot getSunnyBot() {
        return sunnyBot;
    }

    public final String getName() {
        return name;
    }

    public final Permission getPermission() {
        return permission;
    }

    public final List<String> getAliases() {
        return aliases;
    }

    public final String getDescription() {
        return description;
    }

    public final String[] getSyntax() {
        return syntax;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final int getCooldown() {
        return cooldown;
    }

    public final void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

}
