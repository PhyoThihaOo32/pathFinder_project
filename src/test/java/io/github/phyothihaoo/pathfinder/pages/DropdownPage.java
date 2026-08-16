package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/** Practice page for native {@code <select>} handling. */
public class DropdownPage extends BasePage {

    private static final By DROPDOWN = By.id("dropdown");

    public void open() {
        navigateTo(Configuration.practiceUrl() + "/dropdown");
    }

    private Select select() {
        return new Select(visible(DROPDOWN));
    }

    /**
     * Returns the option labels. The original practice script printed the WebElement itself,
     * which yields an internal element reference rather than the visible text.
     */
    public List<String> optionLabels() {
        return select().getOptions().stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public void selectByLabel(String label) {
        select().selectByVisibleText(label);
    }

    public String selectedLabel() {
        return select().getFirstSelectedOption().getText().trim();
    }
}
