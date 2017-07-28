package surl.server;

import java.util.Properties;

public class ConfigurationService {

    private Properties config;

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
        return config.getProperty("DB_PWD");
    }

    private int str2int(String property, int def) {
        try {
            return Integer.parseInt(config.getProperty(property));
        } catch(Exception e) {
            return def;
        }
    }
}
