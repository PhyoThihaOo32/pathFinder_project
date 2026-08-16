package io.github.phyothihaoo.pathfinder.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.phyothihaoo.pathfinder.drivers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Per-scenario browser lifecycle.
 *
 * <p>Teardown belongs here rather than at the end of a {@code @Then} step. A step-level quit is
 * skipped whenever an earlier assertion fails, which is exactly when a browser is most likely
 * to be left running. The {@code finally} block below guarantees the driver is released on
 * every exit path.
 */
public class Hooks {

    @Before
    public void startBrowser() {
        DriverManager.startDriver();
    }

    @After
    public void closeBrowser(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                captureScreenshot(scenario);
            }
        } finally {
            DriverManager.quitDriver();
        }
    }

    private void captureScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        } catch (RuntimeException e) {
            // A screenshot failure must never mask the real test failure.
            scenario.log("Could not capture screenshot: " + e.getMessage());
        }
    }
}
