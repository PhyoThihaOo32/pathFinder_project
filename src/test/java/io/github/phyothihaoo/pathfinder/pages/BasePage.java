package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import io.github.phyothihaoo.pathfinder.drivers.DriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared behaviour for every page object: element lookup that always waits explicitly.
 *
 * <p>This framework deliberately does not use {@code PageFactory} and {@code @FindBy}. Proxied
 * fields are resolved lazily and interact badly with explicit waits, which is a common source
 * of {@code StaleElementReferenceException}. Plain {@link By} constants resolved through a
 * {@link WebDriverWait} are more predictable and easier to reason about.
 */
public abstract class BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

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
     * Clicks and confirms the application actually responded, rather than trusting the click.
     *
     * <p>Headless Chrome sometimes does not deliver the mouse events WebDriver synthesizes. The
     * symptom is miserable to diagnose: the click reports success, no exception is raised, and
     * the element is present, visible and topmost at its own centre — yet nothing happens. This
     * was confirmed on CI by attaching listeners to the element and to the document, then
     * clicking: a native click produced an empty event log, while a scripted click on the very
     * same node fired normally and navigated. So no event was reaching the page at all; it was
     * not a mis-aimed click, a stale node, or anything the application did.
     *
     * <p>So: click natively, because that is what a user does and it is what exercises the real
     * event path. If several attempts produce no response, fall back to a scripted click and log
     * it, so a browser-level defect cannot masquerade as an application failure. Either way the
     * method ends by waiting for the outcome, and therefore cannot return unless it happened.
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

        if (isPresent(locator)) {
            LOG.warn("No response to {} native clicks on {} - the browser is most likely "
                    + "dropping synthesized input. Falling back to a scripted click.",
                    CLICK_ATTEMPTS, locator);
            scriptedClick(locator);
        }

        // Never return on the strength of the click alone. This final wait is what guarantees
        // the caller that the outcome actually happened, and it fails here — at the click that
        // did not take — instead of somewhere unrelated further down the scenario.
        wait.until(outcome);
    }

    private void scriptedClick(By locator) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", visible(locator));
        } catch (WebDriverException e) {
            LOG.warn("Scripted click on {} also failed: {}", locator, e.getMessage());
        }
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
