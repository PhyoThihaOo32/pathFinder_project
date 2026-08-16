package io.github.phyothihaoo.pathfinder.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.phyothihaoo.pathfinder.config.Configuration;
import io.github.phyothihaoo.pathfinder.pages.InventoryPage;
import io.github.phyothihaoo.pathfinder.pages.LoginPage;
import org.testng.Assert;

public class LoginSteps {

    private LoginPage loginPage() {
        return new LoginPage();
    }

    @Given("the login page is open")
    public void theLoginPageIsOpen() {
        loginPage().open();
        Assert.assertTrue(loginPage().isLoaded(), "The login page did not load");
    }

    @Given("I am signed in as the standard user")
    public void iAmSignedInAsTheStandardUser() {
        LoginPage page = loginPage();
        page.open();
        page.loginAs(Configuration.standardUser(), Configuration.password());
        Assert.assertTrue(new InventoryPage().isLoaded(), "Sign-in did not reach the products page");
    }

    @When("I sign in as the standard user")
    public void iSignInAsTheStandardUser() {
        loginPage().loginAs(Configuration.standardUser(), Configuration.password());
    }

    @When("I sign in as the locked out user")
    public void iSignInAsTheLockedOutUser() {
        loginPage().loginAs(Configuration.lockedOutUser(), Configuration.password());
    }

    @When("I sign in with username {string} and password {string}")
    public void iSignInWith(String username, String password) {
        loginPage().loginAs(username, password);
    }

    @Then("I land on the products page")
    public void iLandOnTheProductsPage() {
        Assert.assertTrue(new InventoryPage().isLoaded(),
                "Expected the products page but was at " + new InventoryPage().currentUrl());
    }

    @Then("I see the login error {string}")
    public void iSeeTheLoginError(String expected) {
        Assert.assertEquals(loginPage().errorMessage(), expected, "Unexpected login error message");
    }
}
