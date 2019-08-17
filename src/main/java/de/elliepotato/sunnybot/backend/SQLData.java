package de.elliepotato.sunnybot.backend;

import de.arraying.kotys.JSON;
import de.elliepotato.sunnybot.SunnyBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Ellie on 06/05/2017 for YT-er Sunny.
 */
public class SQLData {

    private final String TABLE = "sunnybot";

    private SunnyBot sunnyBot;
    private long guild;
    private String joinMsg, leaveMsg;
    private boolean joinEnabled, leaveEnabled;
    private BotBlacklist botBlacklist;

    public SQLData(SunnyBot sunnyBot) {
        this.sunnyBot = sunnyBot;

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                    "`id` INT(100) NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "`guild` BIGINT, " +
                    "`join_msg` LONGTEXT," +
                    "`leave_msg` LONGTEXT, " +
                    "`join_enabled` TINYINT, " +
                    "`leave_enabled` TINYINT, " +
                    "`blacklist` LONGTEXT, " +
                    "INDEX(guild)) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;")
                    .execute();
            connection.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to execute initial statement!", e);
            e.printStackTrace();
        }

        loadData();
    }

    private void loadData() {

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + TABLE + "` WHERE id = 1");
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.guild = resultSet.getLong("guild");
                this.joinMsg = resultSet.getString("join_msg");
                this.leaveMsg = resultSet.getString("leave_msg");
                this.joinEnabled = resultSet.getBoolean("join_enabled");
                this.leaveEnabled = resultSet.getBoolean("leave_enabled");
                this.botBlacklist = new JSON(resultSet.getString("blacklist")).marshal(BotBlacklist.class);

                if (botBlacklist.getBlacklistArray() != null)
                    botBlacklist.setActualBlacklist(Arrays.stream(botBlacklist.getBlacklistArray()).collect(Collectors.toList()));
            }

        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to load SQL data!", e);
            e.printStackTrace();
        }

    }

    public String getJoinMessage() {
        return joinMsg;
    }

    public void setJoinMsg(String joinMsg) {
        this.joinMsg = joinMsg;

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `" + TABLE + "` SET `join_msg` = ? WHERE guild = " + guild);
            preparedStatement.setString(1, joinMsg);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to set the join message!", e);
            e.printStackTrace();
        }

    }

    public boolean isJoinEnabled() {
        return joinEnabled;
    }

    public void setJoinEnabled(boolean joinEnabled) {
        this.joinEnabled = joinEnabled;

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `" + TABLE + "` SET `join_enabled` = ? WHERE guild = " + guild);
            preparedStatement.setBoolean(1, joinEnabled);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to set the join enabled!", e);
            e.printStackTrace();
        }
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `" + TABLE + "` SET `leave_msg` = ? WHERE guild = " + guild);
            preparedStatement.setString(1, leaveMsg);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to set the leave message!", e);
            e.printStackTrace();
        }
    }

    public boolean isLeaveEnabled() {
        return leaveEnabled;
    }

    public void setLeaveEnabled(boolean leaveEnabled) {
        this.leaveEnabled = leaveEnabled;

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `" + TABLE + "` SET `leave_enabled` = ? WHERE guild = " + guild);
            preparedStatement.setBoolean(1, leaveEnabled);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to set the leave enabled!", e);
            e.printStackTrace();
        }
    }

    public BotBlacklist getBotBlacklist() {
        return botBlacklist;
    }

    public void addBlacklist(long id) {
        botBlacklist.addBlacklist(id);

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            connection.prepareStatement("UPDATE `" + TABLE + "` SET `blacklist` = " + botBlacklist.toString() + " WHERE guild = " + guild).execute();
            connection.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to add to blacklist!", e);
            e.printStackTrace();
        }
    }

    public void removeBlacklist(long id) {
        botBlacklist.removeBlacklist(id);

        try (Connection connection = sunnyBot.getSqlManager().getConnection()) {
            connection.prepareStatement("UPDATE `" + TABLE + "` SET `blacklist` = " + botBlacklist.toString() + " WHERE guild = " + guild).execute();
            connection.close();
        } catch (SQLException e) {
            sunnyBot.getLogger().error("Failed to remove from blacklist!", e);
            e.printStackTrace();
        }
    }

}
