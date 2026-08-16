package io.github.phyothihaoo.pathfinder.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.phyothihaoo.pathfinder.pages.CheckoutCompletePage;
import io.github.phyothihaoo.pathfinder.pages.CheckoutInformationPage;
import io.github.phyothihaoo.pathfinder.pages.CheckoutOverviewPage;
import java.util.List;
import org.testng.Assert;

public class CheckoutSteps {

    @When("I enter checkout details {string}, {string} and {string}")
    public void iEnterCheckoutDetails(String firstName, String lastName, String postalCode) {
        CheckoutInformationPage page = new CheckoutInformationPage();
        page.enterDetails(firstName, lastName, postalCode);
        page.continueToOverview();
    }

    @Then("I see the checkout error {string}")
    public void iSeeTheCheckoutError(String expected) {
        Assert.assertEquals(new CheckoutInformationPage().errorMessage(), expected,
                "Unexpected checkout validation message");
    }

    @Then("the order overview lists:")
    public void theOrderOverviewLists(List<String> expectedProducts) {
        Assert.assertEquals(new CheckoutOverviewPage().itemNames(), expectedProducts,
                "Unexpected products on the order overview");
    }

    @Then("the order item total is {double}")
    public void theOrderItemTotalIs(double expected) {
        Assert.assertEquals(new CheckoutOverviewPage().itemTotal(), expected, 0.001,
                "Unexpected order item total");
    }

    @When("I confirm the order")
    public void iConfirmTheOrder() {
        new CheckoutOverviewPage().finishOrder();
    }

    @Then("I see the order confirmation {string}")
    public void iSeeTheOrderConfirmation(String expected) {
        CheckoutCompletePage page = new CheckoutCompletePage();
        Assert.assertTrue(page.isLoaded(), "The confirmation page did not load");
        Assert.assertEquals(page.confirmationMessage(), expected, "Unexpected confirmation message");
    }
}
