package surl.server.services;

import java.util.Properties;

public class ConfigurationService {

    public static Properties config;

    public ConfigurationService(Properties config) {
        this.config = config;
    }

    public int getServerPort() {
        return str2int("SERVER_PORT", 9988);
    }

    public String getDBHost() {
        return config.getProperty("DB_HOST", "localhost");
    }

    public int getDBPort() {
        return str2int("DB_PORT", 3306);
    }

    public String getDBSchema() {
        return config.getProperty("DB_SCHEMA");
    }

    public String getDBUserName() {
        return config.getProperty("DB_USER");
    }

    public String getDBPassword() {
        return config.getProperty("DB_PSWD");
    }

    private int str2int(String key, int def) {
        try {
            return Integer.parseInt(config.getProperty("SERVER_PORT"));
        } catch(Exception e) {
            return def;
        }
    }
}
