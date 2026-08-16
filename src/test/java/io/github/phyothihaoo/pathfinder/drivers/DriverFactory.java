package io.github.phyothihaoo.pathfinder.drivers;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Builds a configured {@link WebDriver} for the browser named in configuration.
 *
 * <p>No driver binaries are managed here. Selenium Manager (bundled since Selenium 4.6)
 * downloads and matches the correct driver automatically, which is why this project has no
 * WebDriverManager dependency.
 *
 * <p>Note that no implicit wait is ever set. Implicit and explicit waits do not compose —
 * mixing them produces timeouts that are hard to predict and hard to debug. All waiting is
 * done explicitly in {@code BasePage}.
 */
public final class DriverFactory {

    private static final String WINDOW_SIZE = "1920,1080";

    private DriverFactory() {
        // Utility class.
    }

    public static WebDriver create() {
        String browser = Configuration.browser();
        boolean headless = Configuration.headless();

        WebDriver driver = switch (browser) {
            case "chrome" -> new ChromeDriver(chromeOptions(headless));
            case "firefox" -> new FirefoxDriver(firefoxOptions(headless));
            case "edge" -> new EdgeDriver(edgeOptions(headless));
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browser + "'. Use chrome, firefox or edge.");
        };

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(Configuration.pageLoadTimeoutSeconds()));

        if (!headless) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=" + WINDOW_SIZE);
        if (headless) {
            options.addArguments("--headless=new");
            // Required for containerised CI runners, harmless locally.
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--window-size=" + WINDOW_SIZE);
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        }
        return options;
    }
}
