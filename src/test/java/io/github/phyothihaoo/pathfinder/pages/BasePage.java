package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import io.github.phyothihaoo.pathfinder.drivers.DriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
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

    /** How long to give the application to react to a click before assuming it was lost. */
    private static final Duration RESPONSE_PROBE = Duration.ofSeconds(5);

    /** How many times to send a click that produces no visible response. */
    private static final int CLICK_ATTEMPTS = 3;

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
     * Clicks, then confirms the application actually responded, clicking once more if it did not.
     *
     * <p>A click on this application can succeed at the WebDriver level and still do nothing.
     * The page is a React SPA whose components re-render as state settles, and a click
     * dispatched into that window lands on a node being replaced, so the handler never runs.
     * WebDriver reports success because it did click something; the page simply sits there, and
     * the next step fails somewhere unrelated with a confusing timeout.
     *
     * <p>Every click that is expected to navigate or change state goes through here, with the
     * outcome it should produce. Verifying the outcome — rather than trusting the click — is
     * what makes the suite deterministic on a loaded CI runner.
     */
    protected void clickExpecting(By locator, ExpectedCondition<?> outcome) {
        for (int attempt = 1; attempt <= CLICK_ATTEMPTS; attempt++) {
            // On a retry, a control that has disappeared means the page did respond after all,
            // just more slowly than the probe allows. Stop clicking and wait it out.
            if (attempt > 1 && !isPresent(locator)) {
                break;
            }
            try {
                click(locator);
            } catch (WebDriverException pageMovedUnderUs) {
                break;
            }
            if (responded(outcome)) {
                return;
            }
        }
        // Never return on the strength of the click alone. This final wait is what guarantees
        // the caller that the outcome actually happened, and it fails here — at the click that
        // did not take — instead of somewhere unrelated further down the scenario.
        wait.until(outcome);
    }

    private boolean responded(ExpectedCondition<?> outcome) {
        try {
            new WebDriverWait(driver, RESPONSE_PROBE).until(outcome);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isPresent(By locator) {
        try {
            return !driver.findElements(locator).isEmpty();
        } catch (WebDriverException e) {
            return false;
        }
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
