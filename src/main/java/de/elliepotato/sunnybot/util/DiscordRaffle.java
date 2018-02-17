package de.elliepotato.sunnybot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ellie on 06/05/2017 for YT-er Sunny.
 */
public class DiscordRaffle {

    private String keyWord;
    private List<Long> participants;
    private boolean closed;

    public DiscordRaffle(String keyword) {
        this.keyWord = keyword;
        participants = new ArrayList<>();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void add(long id) {
        if (!closed && !participants.contains(id)) {
            participants.add(id);
        }
    }

    public String draw(int toDraw) {
        closed = true;
        Collections.shuffle(participants);
        final StringBuilder stringBuilder = new StringBuilder();
        participants.stream().limit(toDraw).forEach(aLong -> stringBuilder.append(" <@").append(aLong).append(">,"));
        return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
    }

    public void finish() {
        participants.clear();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}
