@inventory
Feature: Browsing the product catalogue
  As a shopper
  I want to see and reorder the product list
  So that I can find the item I want quickly

  Background:
    Given I am signed in as the standard user

  @smoke
  Scenario: The catalogue lists every product
    Then I see 6 products listed

  @regression
  Scenario Outline: Products can be reordered
    When I sort the products by "<option>"
    Then the products are sorted by "<field>" in "<direction>" order

    Examples:
      | option              | field | direction  |
      | Name (A to Z)       | name  | ascending  |
      | Name (Z to A)       | name  | descending |
      | Price (low to high) | price | ascending  |
      | Price (high to low) | price | descending |
