package io.github.phyothihaoo.pathfinder.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** The basket review screen at {@code /cart.html}. */
public class CartPage extends BasePage {

    private static final By CART_ITEMS = By.className("cart_item");
    private static final By ITEM_NAMES = By.cssSelector(".inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");

    public boolean isLoaded() {
        return currentUrl().contains("/cart.html");
    }

    public int itemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public List<String> itemNames() {
        return allVisible(ITEM_NAMES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public void proceedToCheckout() {
        click(CHECKOUT_BUTTON);
    }

    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
    }
}
