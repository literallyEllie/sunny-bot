package de.elliepotato.sunnybot.command;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.UtilEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ellie on 07/05/2017 for YT-er Sunny.
 */
public class CmdGuild extends SunnyCommand {

    private UtilEmbedBuilder embedBuilder;

    public CmdGuild(SunnyBot sunnyBot) {
        super(sunnyBot, "guild", "Guild information", Permission.MESSAGE_WRITE, "guild");
        embedBuilder = new UtilEmbedBuilder();
        embedBuilder.setTitle("Guild information", null)
                .setColor(new Color(0x7B1A36))
                .setFooter("SunnyBot v" + SunnyBot.VERSION, null);
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        Guild guild = e.getGuild();
        embedBuilder.removeField("Owner")
                .removeField("Members")
                .removeField("Created")
                .removeField("Text/Voice channels")
                .addField("Owner", guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), false)
                .addField("Members", guild.getMembers().size() + "", false)
                .addField("Created", guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                .addField("Text/Voice channels", guild.getTextChannels().size() + "/" + guild.getVoiceChannels().size(), false);
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
