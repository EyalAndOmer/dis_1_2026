package uniza.fri.majba.dis1;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import java.util.Random;

public enum Config {
    INSTANCE;

    public static final String PROPERTIES_PATH = "config.properties";

    private final Properties properties;
    private final Random seedGenerator = new Random(100);

    Config() {
        var props = new Properties();
        try (InputStream is = Config.class.getResourceAsStream("/" + PROPERTIES_PATH)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Failed to load " + PROPERTIES_PATH, ex);
        }
        this.properties = props;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public Random getSeedGeneratorInstance() {
        return seedGenerator;
    }

    public static Random getSeedGenerator() {
        return INSTANCE.seedGenerator;
    }
}
