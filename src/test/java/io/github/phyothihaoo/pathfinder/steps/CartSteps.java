package io.github.phyothihaoo.pathfinder.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.phyothihaoo.pathfinder.pages.CartPage;
import java.util.List;
import org.testng.Assert;

public class CartSteps {

    private CartPage cartPage() {
        return new CartPage();
    }

    @Then("the cart page is displayed")
    public void theCartPageIsDisplayed() {
        Assert.assertTrue(cartPage().isLoaded(), "The cart page did not load");
    }

    @Then("the cart contains:")
    public void theCartContains(List<String> expectedProducts) {
        Assert.assertEquals(cartPage().itemNames(), expectedProducts, "Unexpected cart contents");
    }

    @Then("the cart holds {int} items")
    public void theCartHoldsItems(int expected) {
        Assert.assertEquals(cartPage().itemCount(), expected, "Unexpected number of cart items");
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() {
        cartPage().proceedToCheckout();
    }
}
