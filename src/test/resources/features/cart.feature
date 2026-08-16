@cart
Feature: Managing the shopping cart
  As a shopper
  I want to add and remove items
  So that I buy exactly what I intended

  Background:
    Given I am signed in as the standard user

  @smoke
  Scenario: Adding an item updates the cart badge
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge shows 1

  @regression
  Scenario: Every added item reaches the cart
    When I add "Sauce Labs Backpack" to the cart
    And I add "Sauce Labs Bike Light" to the cart
    And I open the cart
    Then the cart page is displayed
    And the cart contains:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |

  @regression
  Scenario: Removing an item clears the badge
    When I add "Sauce Labs Backpack" to the cart
    And I remove "Sauce Labs Backpack" from the cart
    Then the cart badge shows 0
