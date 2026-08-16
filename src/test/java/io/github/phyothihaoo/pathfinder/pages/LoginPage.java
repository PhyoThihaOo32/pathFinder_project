package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** The sign-in screen at the application root. */
public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    public void open() {
        navigateTo(Configuration.baseUrl());
    }

    public void loginAs(String username, String password) {
        type(USERNAME_FIELD, username);
        type(PASSWORD_FIELD, password);
        // Either outcome counts as the app having responded: the catalogue, or a rejection.
        clickExpecting(LOGIN_BUTTON, ExpectedConditions.or(
                ExpectedConditions.urlContains("/inventory.html"),
                ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)));
    }

    public String errorMessage() {
        return textOf(ERROR_MESSAGE);
    }

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON);
    }
}
