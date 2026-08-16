package io.github.phyothihaoo.pathfinder.pages;

import org.openqa.selenium.By;

/** The order confirmation screen at {@code /checkout-complete.html}. */
public class CheckoutCompletePage extends BasePage {

    private static final By CONFIRMATION_HEADER = By.className("complete-header");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public boolean isLoaded() {
        return currentUrl().contains("/checkout-complete.html");
    }

    public String confirmationMessage() {
        return textOf(CONFIRMATION_HEADER);
    }

    public void backToProducts() {
        click(BACK_HOME_BUTTON);
    }
}
