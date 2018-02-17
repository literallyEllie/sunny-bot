package de.elliepotato.sunnybot.command;

import de.elliepotato.sunnybot.SunnyBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class CmdSocial extends SunnyCommand {

    private MessageEmbed messageEmbed;

    public CmdSocial(SunnyBot sunnyBot) {
        super(sunnyBot, "social", "Gives social links", Permission.MESSAGE_WRITE, "social");
        messageEmbed = new EmbedBuilder()
                .addField("Here are my social links!", "", false)
                .setColor(new Color(0x7B1A36))
                .addField("Youtube", "https://www.youtube.com/ihascakes", false)
                .addField("Twitter", "https://twitter.com/ihascakes", false)
                .addField("Instagram", "https://www.instagram.com/ihascakes", false)
                .addField("Steam", "http://steamcommunity.com/id/cptnsunny", false)
                .addField("Facebook", "https://www.facebook.com/ihascakes", false)
                .addField("Patreon", "https://www.patreon.com/ihascakes", false)
                .addField("SnapChat", "@sunnijeep", false)
                .addField("Twitch", "https://www.twitch.tv/cptnsunny", false)
                .build();
    }

    @Override
    protected void onCommand(GuildMessageReceivedEvent e, String[] args) {
        getSunnyBot().messageChannel(e.getChannel(), messageEmbed);
    }
}
