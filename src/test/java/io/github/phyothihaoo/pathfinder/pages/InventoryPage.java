package io.github.phyothihaoo.pathfinder.pages;

import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/** The product catalogue shown after a successful sign-in. */
public class InventoryPage extends BasePage {

    private static final By PAGE_HEADING = By.className("title");
    private static final By PRODUCTS = By.className("inventory_item");
    private static final By PRODUCT_NAMES = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES = By.cssSelector(".inventory_item_price");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    public boolean isLoaded() {
        return currentUrl().contains("/inventory.html") && isDisplayed(PAGE_HEADING);
    }

    public String heading() {
        return textOf(PAGE_HEADING);
    }

    public int productCount() {
        return allVisible(PRODUCTS).size();
    }

    public List<String> productNames() {
        return allVisible(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public List<Double> productPrices() {
        return allVisible(PRODUCT_PRICES).stream()
                .map(WebElement::getText)
                .map(text -> text.replace("$", "").trim())
                .map(Double::parseDouble)
                .toList();
    }

    public void sortBy(String visibleOption) {
        new Select(visible(SORT_DROPDOWN)).selectByVisibleText(visibleOption);
    }

    public void addToCart(String productName) {
        click(addToCartButton(productName));
    }

    public void removeFromCart(String productName) {
        click(By.id("remove-" + slug(productName)));
    }

    /**
     * The demo app derives each button id from the product name, so one rule covers every
     * product without hard-coding six separate locators.
     */
    private By addToCartButton(String productName) {
        return By.id("add-to-cart-" + slug(productName));
    }

    private String slug(String productName) {
        return productName.toLowerCase(Locale.ROOT).replace(' ', '-');
    }

    /** The badge is absent rather than zeroed when the cart is empty. */
    public int cartItemCount() {
        try {
            return Integer.parseInt(driver.findElement(CART_BADGE).getText().trim());
        } catch (NoSuchElementException | NumberFormatException e) {
            return 0;
        }
    }

    public void openCart() {
        click(CART_LINK);
    }

    public void logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
    }
}
