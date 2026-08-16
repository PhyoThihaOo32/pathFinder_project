package io.github.phyothihaoo.pathfinder.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Step two of checkout: the order summary and totals. */
public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_NAMES = By.cssSelector(".inventory_item_name");
    private static final By ITEM_TOTAL_LABEL = By.className("summary_subtotal_label");
    private static final By FINISH_BUTTON = By.id("finish");

    public boolean isLoaded() {
        return currentUrl().contains("/checkout-step-two.html");
    }

    public List<String> itemNames() {
        return allVisible(ITEM_NAMES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    /** Parses "Item total: $29.99" down to the numeric value. */
    public double itemTotal() {
        String label = textOf(ITEM_TOTAL_LABEL);
        return Double.parseDouble(label.substring(label.indexOf('$') + 1).trim());
    }

    public void finishOrder() {
        clickExpecting(FINISH_BUTTON,
                ExpectedConditions.urlContains("/checkout-complete.html"));
    }
}
