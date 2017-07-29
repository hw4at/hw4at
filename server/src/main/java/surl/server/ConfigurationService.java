package surl.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {

    private Properties prop;

    private ConfigurationService() {
    }

    public final static ConfigurationService config = new ConfigurationService();

    public static void init() throws IOException {
        init("src/main/resources/surl.properties");
    }

    public static void init(String path) throws IOException {
        if (config.prop == null) {
            Properties prop = new Properties();
            try (InputStream in = new FileInputStream(path)) {
                prop.load(in);
            }
            config.prop = prop;
        }
    }

    public String getServerHost() {
        return prop.getProperty("DB_SCHEMA", "http://localhost");
    }

    public int getServerPort() {
        return str2int("SERVER_PORT", 9988);
    }

    public String getDBHost() {
        return prop.getProperty("DB_HOST", "localhost");
    }

    public int getDBPort() {
        return str2int("DB_PORT", 3306);
    }

    public String getDBSchema() {
        return prop.getProperty("DB_SCHEMA");
    }

    public String getDBUserName() {
        return prop.getProperty("DB_USER");
    }

    public String getDBPassword() {
        return prop.getProperty("DB_PWD");
    }

    private int str2int(String property, int def) {
        try {
            return Integer.parseInt(prop.getProperty(property));
        } catch(Exception e) {
            return def;
        }
    }
}
