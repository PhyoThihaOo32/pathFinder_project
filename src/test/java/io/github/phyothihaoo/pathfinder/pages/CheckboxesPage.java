package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Practice page for checkbox state handling. */
public class CheckboxesPage extends BasePage {

    private static final By CHECKBOXES = By.cssSelector("#checkboxes input[type='checkbox']");

    public void open() {
        navigateTo(Configuration.practiceUrl() + "/checkboxes");
    }

    public int count() {
        return allVisible(CHECKBOXES).size();
    }

    public boolean isChecked(int index) {
        return checkbox(index).isSelected();
    }

    /** Setting state rather than blind-clicking keeps the step idempotent. */
    public void setChecked(int index, boolean desired) {
        WebElement box = checkbox(index);
        if (box.isSelected() != desired) {
            box.click();
        }
    }

    private WebElement checkbox(int index) {
        List<WebElement> boxes = allVisible(CHECKBOXES);
        if (index < 1 || index > boxes.size()) {
            throw new IllegalArgumentException(
                    "Checkbox " + index + " does not exist; the page has " + boxes.size());
        }
        return boxes.get(index - 1);
    }
}
