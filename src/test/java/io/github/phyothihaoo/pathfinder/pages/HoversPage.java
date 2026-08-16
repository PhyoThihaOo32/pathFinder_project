package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

/** Practice page for mouse-hover interactions. */
public class HoversPage extends BasePage {

    private static final By AVATARS = By.cssSelector(".figure img");

    public void open() {
        navigateTo(Configuration.practiceUrl() + "/hovers");
    }

    public void hoverOverAvatar(int index) {
        new Actions(driver)
                .moveToElement(allVisible(AVATARS).get(index - 1))
                .perform();
    }

    /** The caption only becomes visible once the matching avatar is hovered. */
    public String captionFor(int index) {
        return textOf(By.cssSelector(".figure:nth-of-type(" + index + ") .figcaption h5"));
    }

    public boolean isCaptionVisible(int index) {
        return isDisplayed(By.cssSelector(".figure:nth-of-type(" + index + ") .figcaption h5"));
    }
}
