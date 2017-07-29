package surl.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationServiceHolder {

    public static ConfigurationService config;

    public static void init() throws IOException {
        init("src/main/resources/surl.properties");
    }

    public static void init(String path) throws IOException {
        if (ConfigurationServiceHolder.config == null) {
            Properties prop = new Properties();
            try (InputStream in = new FileInputStream(path)) {
                prop.load(in);
            }
            ConfigurationServiceHolder.config = new ConfigurationService(prop);
        }
    }
}
