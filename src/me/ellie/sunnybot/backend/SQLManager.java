package me.ellie.sunnybot.backend;

import me.ellie.sunnybot.SunnyBot;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ellie on 06/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class SQLManager {

    private Sql2o sql2o;
    private final String TABLE = "sunnybot";

    private String joinMsg, leaveMsg;
    private boolean joinEnabled, leaveEnabled;
    private List<Long> blacklist = new ArrayList<>();
    private long guild;

    public SQLManager(String url, String username, String password) {
        sql2o = new Sql2o(url, username, password);

        try (Connection connection = sql2o.open()) {
            connection.createQuery("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                    "`id` INT(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "`guild` BIGINT, " +
                    "`join_msg` LONGTEXT," +
                    "`leave_msg` LONGTEXT, " +
                    "`join_enabled` TINYINT, " +
                    "`leave_enabled` TINYINT, " +
                    "`blacklist` LONGTEXT, " +
                    "INDEX(guild)) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;").executeUpdate();

            this.guild = connection.createQuery("SELECT `guild` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(Long.class).get(0);
            this.joinMsg = connection.createQuery("SELECT `join_msg` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(String.class).get(0);
            this.leaveMsg = connection.createQuery("SELECT `leave_msg` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(String.class).get(0);
            this.joinEnabled = connection.createQuery("SELECT `join_enabled` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(Boolean.class).get(0);
            this.leaveEnabled = connection.createQuery("SELECT `leave_enabled` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(Boolean.class).get(0);
            String blacklist = connection.createQuery("SELECT `blacklist` FROM `" + TABLE + "` WHERE id = 1;").executeAndFetch(String.class).get(0);

            JSONParser parser = new JSONParser();
            try {
                JSONArray object = (JSONArray) parser.parse(blacklist);
                for (Object o : object) {
                    this.blacklist.add((Long) o);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                SunnyBot.getSunnyBot().logWarning("Failed to parse blacklist!");
            }
            connection.close();
        }
    }

    public void finish(){
        if(sql2o != null){
            try {
                sql2o.getDataSource().getConnection().close();
            }catch(Exception e){
            }finally {
                sql2o = null;
            }
        }
    }

    public Sql2o getSql2o() {
        return sql2o;
    }

    public String getJoinMessage(){
        return joinMsg;
    }

    public void setJoinMsg(String joinMsg) {
        this.joinMsg = joinMsg;
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `join_msg` = :join WHERE guild = "+guild+";")
                    .addParameter("join", joinMsg).executeUpdate().close();
        }
    }

    public void setJoinEnabled(boolean joinEnabled) {
        this.joinEnabled = joinEnabled;
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `join_enabled` = :enabled WHERE guild = "+guild)
                    .addParameter("enabled", joinEnabled ? 1 : 0).executeUpdate().close();
        }
    }

    public boolean isJoinEnabled() {
        return joinEnabled;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `leave_msg` = :leave WHERE guild = "+guild)
                    .addParameter("leave", leaveMsg).executeUpdate().close();
        }
    }


    public void setLeaveEnabled(boolean leaveEnabled) {
        this.leaveEnabled = leaveEnabled;
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `leave_enabled` = :enabled WHERE guild = "+guild)
                    .addParameter("enabled", leaveEnabled ? 1 : 0).executeUpdate().close();
        }
    }

    public boolean isLeaveEnabled() {
        return leaveEnabled;
    }

    public List<Long> getBlacklist() {
        return blacklist;
    }

    public void addBlacklist(long id){
        blacklist.add(id);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(blacklist);
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `blacklist` = :blacklist WHERE guild = "+guild).
            addParameter("blacklist", jsonArray.toJSONString()).executeUpdate().close();
        }
    }

    public void removeBlacklist(long id){
        blacklist.remove(id);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(blacklist);
        try(Connection connection = sql2o.open()){
            connection.createQuery("UPDATE `"+TABLE+"` SET `blacklist` = :blacklist WHERE guild = "+guild).
                    addParameter("blacklist", jsonArray.toJSONString()).executeUpdate().close();
        }
    }

}
