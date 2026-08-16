package io.github.phyothihaoo.pathfinder.pages;

import io.github.phyothihaoo.pathfinder.config.Configuration;
import org.openqa.selenium.By;

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
        click(LOGIN_BUTTON);
    }

    public String errorMessage() {
        return textOf(ERROR_MESSAGE);
    }

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON);
    }
}
