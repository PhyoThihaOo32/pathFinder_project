package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Practice page for multi-window handling. */
public class WindowsPage extends BasePage {

    private static final By CLICK_HERE_LINK = By.linkText("Click Here");
    private static final By HEADING = By.tagName("h3");

    public void open() {
        navigateTo(Configuration.practiceUrl() + "/windows");
    }

    /**
     * Opens the child window and switches to it, returning the original handle so the caller
     * can switch back.
     *
     * <p>The original practice script looped over every handle and switched back to the parent
     * whenever a title did not match, so it could finish on the wrong window. Stopping at the
     * first non-parent handle is both correct and simpler.
     */
    public String openChildWindow() {
        String originalHandle = driver.getWindowHandle();
        click(CLICK_HERE_LINK);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        return originalHandle;
    }

    public String heading() {
        return textOf(HEADING);
    }

    public void closeChildAndReturnTo(String originalHandle) {
        driver.close();
        driver.switchTo().window(originalHandle);
    }

    public int windowCount() {
        return driver.getWindowHandles().size();
    }
}
