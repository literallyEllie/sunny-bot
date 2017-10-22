package de.ellie.sunnybot.listener;

import de.ellie.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class OtherListener extends ListenerAdapter {

    private String join, leave;

    public OtherListener(String join, String leave){
        this.join = join;
        this.leave = leave;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member user = event.getMember();

        if(join.isEmpty()) return;

        event.getGuild().getTextChannelById(DiscordUtil.GENERAL).sendMessage(join
        .replace("{taggedUser}", "<@"+user.getUser().getId()+">").replace("{namedUser}", user.getEffectiveName())).queue();
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Member user = event.getMember();
        if(leave.isEmpty()) return;

        event.getGuild().getTextChannelById(DiscordUtil.GENERAL).sendMessage(leave
                .replace("{taggedUser}", "<@"+user.getUser().getId()+">").replace("{namedUser}", user.getEffectiveName())).queue();
    }



    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        String message = e.getMessageId();
        e.getChannel().getMessageById(e.getMessageId()).complete().getEmotes().forEach(emote -> {
            if(emote.getId().equalsIgnoreCase("")){

            }
        });
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

}
