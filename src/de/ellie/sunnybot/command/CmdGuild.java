package de.ellie.sunnybot.command;

import de.ellie.sunnybot.util.UtilEmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ellie on 07/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdGuild extends SunnyCommand {

    private UtilEmbedBuilder embedBuilder;

    public CmdGuild(){
        super("guild", "Guild information", Permission.MESSAGE_WRITE, "guild");
        embedBuilder = new UtilEmbedBuilder();
        embedBuilder.setTitle("Guild information", null);
        embedBuilder.setColor(new Color(0x7B1A36));
        embedBuilder.setFooter("SunnyBot v3.8", null);
        setCooldown(20);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        Guild guild = e.getGuild();
        embedBuilder.removeField("Owner");
        embedBuilder.removeField("Members");
        embedBuilder.removeField("Created");
        embedBuilder.removeField("Text/Voice channels");
        embedBuilder.addField("Owner", guild.getOwner().getUser().getName()+"#"+guild.getOwner().getUser().getDiscriminator(), false);
        embedBuilder.addField("Members", guild.getMembers().size()+"", false);
        embedBuilder.addField("Created", guild.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), false);
        embedBuilder.addField("Text/Voice channels", guild.getTextChannels().size()+"/"+guild.getVoiceChannels().size(), false);
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
