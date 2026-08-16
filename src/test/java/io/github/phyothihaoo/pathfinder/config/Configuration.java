package io.github.phyothihaoo.pathfinder.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Single source of truth for run-time settings.
 *
 * <p>Values come from {@code config.properties} on the test classpath, but any key can be
 * overridden with a system property. That is what lets CI run the same suite headless against
 * a different environment without editing a tracked file:
 *
 * <pre>mvn test -Dbrowser=firefox -Dheadless=true -DbaseUrl=https://staging.example.com</pre>
 */
public final class Configuration {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private Configuration() {
        // Utility class.
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(CONFIG_FILE + " was not found on the test classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read " + CONFIG_FILE, e);
        }
        return properties;
    }

    private static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override.trim();
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration key: " + key);
        }
        return value.trim();
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String practiceUrl() {
        return get("practiceUrl");
    }

    public static String browser() {
        return get("browser").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(get("explicitWaitSeconds"));
    }

    public static int pageLoadTimeoutSeconds() {
        return Integer.parseInt(get("pageLoadTimeoutSeconds"));
    }

    public static String standardUser() {
        return get("standardUser");
    }

    public static String lockedOutUser() {
        return get("lockedOutUser");
    }

    public static String password() {
        return get("password");
    }
}
