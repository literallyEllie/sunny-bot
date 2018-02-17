package de.elliepotato.sunnybot.backend;

import com.google.common.collect.Lists;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONField;

import java.util.List;

/**
 * Created by Ellie on 17/02/2018 for YT-er Sunny.
 */
public class BotBlacklist {

    @JSONField(key = "blacklist")
    private Long[] blacklist;
    private List<Long> actualBlacklist = Lists.newArrayList();

    public List<Long> getBlacklist() {
        return actualBlacklist;
    }

    void setActualBlacklist(List<Long> actualBlacklist) {
        this.actualBlacklist = actualBlacklist;
    }

    Long[] getBlacklistArray() {
        return blacklist;
    }

    public void addBlacklist(long id) {
        actualBlacklist.add(id);
    }

    public void removeBlacklist(long id) {
        actualBlacklist.remove(id);
    }

    public boolean isBlacklist(long id) {
        return actualBlacklist.contains(id);
    }

    @Override
    public String toString() {
        return new JSON(actualBlacklist).marshal();
    }

}
