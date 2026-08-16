package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import io.github.phyothihaoo.pathfinder.drivers.DriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared behaviour for every page object: element lookup that always waits explicitly.
 *
 * <p>This framework deliberately does not use {@code PageFactory} and {@code @FindBy}. Proxied
 * fields are resolved lazily and interact badly with explicit waits, which is a common source
 * of {@code StaleElementReferenceException}. Plain {@link By} constants resolved through a
 * {@link WebDriverWait} are more predictable and easier to reason about.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Configuration.explicitWaitSeconds()));
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> allVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    /**
     * Types into a field and does not return until the field actually holds the value.
     *
     * <p>The confirmation is not decoration. The application under test uses controlled React
     * inputs that commit their state asynchronously, so a fast follow-up click can submit a
     * form before the last field has registered. Waiting on the committed value turns an
     * intermittent failure into a deterministic one.
     */
    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        if (!text.isEmpty()) {
            field.sendKeys(text);
        }
        wait.until(ExpectedConditions.attributeToBe(locator, "value", text));
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    /** Returns false instead of throwing when the element never appears. */
    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isAbsent(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected void waitForUrlContaining(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String pageTitle() {
        return driver.getTitle();
    }
}
