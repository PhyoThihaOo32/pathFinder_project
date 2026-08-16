package io.github.phyothihaoo.pathfinder.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Step one of checkout: the buyer's delivery details. */
public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME_FIELD = By.id("first-name");
    private static final By LAST_NAME_FIELD = By.id("last-name");
    private static final By POSTAL_CODE_FIELD = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By CANCEL_BUTTON = By.id("cancel");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    public boolean isLoaded() {
        return currentUrl().contains("/checkout-step-one.html");
    }

    public void enterDetails(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME_FIELD, firstName);
        type(LAST_NAME_FIELD, lastName);
        type(POSTAL_CODE_FIELD, postalCode);
    }

    /**
     * Submits the form and does not return until the application has actually responded —
     * either by advancing to the overview or by rejecting the input.
     *
     * <p>Submitting this form is not reliably idempotent from the browser's point of view. The
     * form re-renders as its controlled inputs commit, and a click dispatched into that window
     * can land on a node React is replacing, so the handler never runs and the page simply sits
     * there. Waiting for a definite outcome and clicking once more if none arrived removes the
     * intermittent failure this caused in parallel runs.
     */
    public void continueToOverview() {
        click(CONTINUE_BUTTON);
        if (!hasResponded()) {
            click(CONTINUE_BUTTON);
        }
    }

    private boolean hasResponded() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/checkout-step-two.html"),
                    ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void cancel() {
        click(CANCEL_BUTTON);
    }

    public String errorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
