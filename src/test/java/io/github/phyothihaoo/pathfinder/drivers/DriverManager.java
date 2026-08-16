package io.github.phyothihaoo.pathfinder.drivers;

import org.openqa.selenium.WebDriver;

/**
 * Holds one {@link WebDriver} per thread.
 *
 * <p>Storing the driver in a {@link ThreadLocal} is what makes parallel scenario execution
 * safe: each TestNG data-provider thread gets its own browser, and step classes can reach the
 * right instance without passing it around or newing up a second one.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
        // Utility class.
    }

    public static void startDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(DriverFactory.create());
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "No driver started for thread '" + Thread.currentThread().getName()
                            + "'. Is the Hooks class on the Cucumber glue path?");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            // Clearing the entry matters: TestNG reuses threads, and a stale reference here
            // would hand the next scenario a dead browser.
            DRIVER.remove();
        }
    }
}
