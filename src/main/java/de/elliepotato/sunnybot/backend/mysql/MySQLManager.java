package de.elliepotato.sunnybot.backend.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.backend.JsonSunnyConfig;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Ellie on 06/05/2017 for YT-er Sunny.
 */
public class MySQLManager {

    private HikariDataSource dataSource;

    public MySQLManager(SunnyBot sunnyBot) {
        final JsonSunnyConfig config = sunnyBot.getSunnyConfig();

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPass());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(hikariConfig);
    }

    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
