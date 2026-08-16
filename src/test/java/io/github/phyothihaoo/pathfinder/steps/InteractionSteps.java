package io.github.phyothihaoo.pathfinder.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.phyothihaoo.pathfinder.pages.CheckboxesPage;
import io.github.phyothihaoo.pathfinder.pages.DropdownPage;
import io.github.phyothihaoo.pathfinder.pages.HoversPage;
import io.github.phyothihaoo.pathfinder.pages.WindowsPage;
import java.util.List;
import org.testng.Assert;

/**
 * Steps for the browser-interaction practice pages. These replace the standalone
 * {@code main()} scripts the project used to carry, which never ran as part of any suite and
 * asserted nothing.
 */
public class InteractionSteps {

    private String parentWindowHandle;

    // --- Checkboxes ---------------------------------------------------------

    @Given("the checkboxes page is open")
    public void theCheckboxesPageIsOpen() {
        new CheckboxesPage().open();
    }

    @When("I check checkbox {int}")
    public void iCheckCheckbox(int index) {
        new CheckboxesPage().setChecked(index, true);
    }

    @When("I uncheck checkbox {int}")
    public void iUncheckCheckbox(int index) {
        new CheckboxesPage().setChecked(index, false);
    }

    @Then("checkbox {int} is checked")
    public void checkboxIsChecked(int index) {
        Assert.assertTrue(new CheckboxesPage().isChecked(index), "Checkbox " + index + " is not checked");
    }

    @Then("checkbox {int} is unchecked")
    public void checkboxIsUnchecked(int index) {
        Assert.assertFalse(new CheckboxesPage().isChecked(index), "Checkbox " + index + " is checked");
    }

    // --- Dropdown -----------------------------------------------------------

    @Given("the dropdown page is open")
    public void theDropdownPageIsOpen() {
        new DropdownPage().open();
    }

    @Then("the dropdown offers:")
    public void theDropdownOffers(List<String> expectedOptions) {
        Assert.assertEquals(new DropdownPage().optionLabels(), expectedOptions,
                "Unexpected dropdown options");
    }

    @When("I select {string} from the dropdown")
    public void iSelectFromTheDropdown(String label) {
        new DropdownPage().selectByLabel(label);
    }

    @Then("the dropdown shows {string}")
    public void theDropdownShows(String expected) {
        Assert.assertEquals(new DropdownPage().selectedLabel(), expected, "Unexpected dropdown selection");
    }

    // --- Hovers -------------------------------------------------------------

    @Given("the hovers page is open")
    public void theHoversPageIsOpen() {
        new HoversPage().open();
    }

    @When("I hover over avatar {int}")
    public void iHoverOverAvatar(int index) {
        new HoversPage().hoverOverAvatar(index);
    }

    @Then("the caption for avatar {int} reads {string}")
    public void theCaptionForAvatarReads(int index, String expected) {
        HoversPage page = new HoversPage();
        Assert.assertTrue(page.isCaptionVisible(index), "Caption " + index + " is not visible");
        Assert.assertEquals(page.captionFor(index), expected, "Unexpected caption text");
    }

    // --- Windows ------------------------------------------------------------

    @Given("the windows page is open")
    public void theWindowsPageIsOpen() {
        new WindowsPage().open();
    }

    @When("I open the child window")
    public void iOpenTheChildWindow() {
        parentWindowHandle = new WindowsPage().openChildWindow();
    }

    @Then("the child window heading is {string}")
    public void theChildWindowHeadingIs(String expected) {
        Assert.assertEquals(new WindowsPage().heading(), expected, "Unexpected child window heading");
    }

    @When("I close the child window")
    public void iCloseTheChildWindow() {
        new WindowsPage().closeChildAndReturnTo(parentWindowHandle);
    }

    @Then("{int} window remains open")
    public void windowRemainsOpen(int expected) {
        Assert.assertEquals(new WindowsPage().windowCount(), expected, "Unexpected number of open windows");
    }
}
