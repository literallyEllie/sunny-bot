package de.elliepotato.sunnybot.listener;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class UserLeaveJoinListener extends ListenerAdapter {

    private final SunnyBot sunnyBot;
    private String join, leave;

    public UserLeaveJoinListener(SunnyBot sunnyBot, String join, String leave) {
        this.sunnyBot = sunnyBot;
        this.join = join;
        this.leave = leave;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (join == null || join.isEmpty()) return;

        final Member user = event.getMember();
        sunnyBot.messageChannel(DiscordUtil.CHANNEL_GENERAL, join.replace("{taggedUser}",
                "<@" + user.getUser().getId() + ">").replace("{namedUser}", user.getEffectiveName()));
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (leave == null || leave.isEmpty()) return;

        final Member user = event.getMember();
        sunnyBot.messageChannel(DiscordUtil.CHANNEL_GENERAL, leave.replace("{taggedUser}",
                "<@" + user.getUser().getId() + ">").replace("{namedUser}", user.getEffectiveName()));
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

}
