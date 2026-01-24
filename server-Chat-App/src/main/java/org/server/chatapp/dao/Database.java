package org.server.chatapp.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;

public class Database {

    private static final HikariDataSource datasource;

    static {
        HikariConfig config = new HikariConfig();
        Properties props = new Properties();
        try (InputStream is = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(is);

            config.setJdbcUrl(props.getProperty("MYSQL_DB_URL"));
            config.setUsername(props.getProperty("MYSQL_DB_USERNAME"));
            config.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
            config.setMaximumPoolSize(10); // أقصى عدد Connections في الـ Pool
            config.setMinimumIdle(2);      // أقل عدد Connections جاهزة
            config.setIdleTimeout(30000);  // 30 ثانية قبل ما idle connection ينقفل
            config.setConnectionTimeout(30000); // وقت الانتظار للحصول على connection
        }
        catch (Exception e) {
            System.out.println("[!] Error while reading properties file");
            e.printStackTrace();
        }

        datasource = new HikariDataSource(config);
    }

    private Database() {}

    public static HikariDataSource getDataSource() {
        return datasource;
    }
}
