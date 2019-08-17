package de.elliepotato.sunnybot.listener;

import com.google.common.collect.Maps;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

/**
 * @author Ellie :: 17/08/2019
 */
public class FunResponder extends ListenerAdapter {

    private Map<String, String> responses;

    public FunResponder() {

        this.responses = Maps.newHashMap();

        // lowkey kopierte fra /r/Norge discord :D
        this.responses.put("good morning", ":coffee: Morning");
        this.responses.put("good night", ":new_moon_with_face: Night");
        this.responses.put("bye", ":wave:");
        this.responses.put("thanks sunnybot", ":point_right::sunglasses::point_right:");
        this.responses.put("f", "F");
        this.responses.put("fight me", "(ง'̀-'́)ง");

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() == DiscordUtil.BOT_SELF ||
                event.getAuthor().isBot() || event.getChannel().getIdLong() != DiscordUtil.CHANNEL_GENERAL) return;

        final String message = responses.get(event.getMessage().getContentRaw().trim().toLowerCase());
        if (message != null)
            event.getChannel().sendMessage(message).queue();

    }

}
