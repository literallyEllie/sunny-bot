package de.elliepotato.sunnybot.listener;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class UserLeaveJoinListener extends ListenerAdapter {

    private final SunnyBot sunnyBot;
    private String join, leave;

    private long lastJoin;
    private int threshold;

    public UserLeaveJoinListener(SunnyBot sunnyBot, String join, String leave) {
        this.sunnyBot = sunnyBot;
        this.join = join;
        this.leave = leave;

        threshold = 0;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (join == null || join.isEmpty()) return;

        if (threshold == 5) {
            sunnyBot.messageChannel(DiscordUtil.CHANNEL_STAFF, "A suspicious amount of people have joined within the recent time. " +
                    "Join messages have been disabled.");
            join = null;
            threshold = 0;
            return;
        }

        // If someone last joined less than 1 seconds ago.
        if (System.currentTimeMillis() - lastJoin <= 1000) {
            threshold++;
            lastJoin = System.currentTimeMillis();
            return;
        }

        final Member user = event.getMember();
        sunnyBot.messageChannel(DiscordUtil.CHANNEL_GENERAL, join.replace("{taggedUser}",
                "<@" + user.getUser().getId() + ">").replace("{namedUser}", user.getEffectiveName()));

        lastJoin = System.currentTimeMillis();
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
