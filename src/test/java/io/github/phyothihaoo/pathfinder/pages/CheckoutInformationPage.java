package io.github.phyothihaoo.pathfinder.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    /** Advancing to the overview and a validation error both count as a response. */
    public void continueToOverview() {
        clickExpecting(CONTINUE_BUTTON, ExpectedConditions.or(
                ExpectedConditions.urlContains("/checkout-step-two.html"),
                ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)));
    }

    public void cancel() {
        click(CANCEL_BUTTON);
    }

    public String errorMessage() {
        return textOf(ERROR_MESSAGE);
    }
}
