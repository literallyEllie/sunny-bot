package de.ellie.sunnybot.command;

import de.ellie.sunnybot.util.UtilEmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class CmdSocial extends SunnyCommand {

    private UtilEmbedBuilder embedBuilder;

    public CmdSocial(){
        super("social", "Gives social links", Permission.MESSAGE_WRITE, "social");
        embedBuilder = new UtilEmbedBuilder();
        embedBuilder.addField("Here are my social links!", "", false);
        embedBuilder.setColor(new Color(0x7B1A36));
        embedBuilder.addField("Youtube", "https://www.youtube.com/ihascakes", false);
        embedBuilder.addField("Twitter", "https://twitter.com/ihascakes", false);
        embedBuilder.addField("Instagram", "https://www.instagram.com/ihascakes", false);
        embedBuilder.addField("Steam", "http://steamcommunity.com/id/cptnsunny", false);
        embedBuilder.addField("Facebook", "https://www.facebook.com/ihascakes", false);
        embedBuilder.addField("Patreon", "https://www.patreon.com/ihascakes", false);
        embedBuilder.addField("SnapChat", "@sunnijeep", false);
        embedBuilder.addField("Twitch", "https://www.twitch.tv/cptnsunny", false);
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
