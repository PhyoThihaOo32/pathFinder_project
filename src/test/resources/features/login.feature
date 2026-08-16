@login
Feature: Signing in to the store
  As a shopper
  I want to sign in with my credentials
  So that I can browse the catalogue and place an order

  Background:
    Given the login page is open

  @smoke
  Scenario: A valid shopper reaches the catalogue
    When I sign in as the standard user
    Then I land on the products page
    And the page heading is "Products"

  @regression
  Scenario: A locked out account is refused
    When I sign in as the locked out user
    Then I see the login error "Epic sadface: Sorry, this user has been locked out."

  @regression
  Scenario Outline: Invalid credentials are refused
    When I sign in with username "<username>" and password "<password>"
    Then I see the login error "<message>"

    Examples:
      | username      | password     | message                                                                   |
      | standard_user | wrong_secret | Epic sadface: Username and password do not match any user in this service |
      | ghost_user    | secret_sauce | Epic sadface: Username and password do not match any user in this service |
      |               | secret_sauce | Epic sadface: Username is required                                        |
      | standard_user |              | Epic sadface: Password is required                                        |
