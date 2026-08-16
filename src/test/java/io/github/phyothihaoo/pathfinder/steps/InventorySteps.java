package io.github.phyothihaoo.pathfinder.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.phyothihaoo.pathfinder.pages.InventoryPage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.testng.Assert;

public class InventorySteps {

    private InventoryPage inventoryPage() {
        return new InventoryPage();
    }

    @Then("the page heading is {string}")
    public void thePageHeadingIs(String expected) {
        Assert.assertEquals(inventoryPage().heading(), expected, "Unexpected page heading");
    }

    @Then("I see {int} products listed")
    public void iSeeProductsListed(int expected) {
        Assert.assertEquals(inventoryPage().productCount(), expected, "Unexpected product count");
    }

    @When("I sort the products by {string}")
    public void iSortTheProductsBy(String option) {
        inventoryPage().sortBy(option);
    }

    @Then("the products are sorted by {string} in {string} order")
    public void theProductsAreSortedBy(String field, String direction) {
        boolean ascending = "ascending".equalsIgnoreCase(direction);

        switch (field.toLowerCase()) {
            case "name" -> {
                List<String> actual = inventoryPage().productNames();
                List<String> expected = new ArrayList<>(actual);
                expected.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
                Assert.assertEquals(actual, expected, "Products are not in " + direction + " name order");
            }
            case "price" -> {
                List<Double> actual = inventoryPage().productPrices();
                List<Double> expected = new ArrayList<>(actual);
                expected.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
                Assert.assertEquals(actual, expected, "Products are not in " + direction + " price order");
            }
            default -> throw new IllegalArgumentException(
                    "Cannot sort by '" + field + "'. Use 'name' or 'price'.");
        }
    }

    @When("I add {string} to the cart")
    public void iAddToTheCart(String product) {
        inventoryPage().addToCart(product);
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromTheCart(String product) {
        inventoryPage().removeFromCart(product);
    }

    @Then("the cart badge shows {int}")
    public void theCartBadgeShows(int expected) {
        Assert.assertEquals(inventoryPage().cartItemCount(), expected, "Unexpected cart badge count");
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        inventoryPage().openCart();
    }

    @When("I sign out")
    public void iSignOut() {
        inventoryPage().logout();
    }
}
