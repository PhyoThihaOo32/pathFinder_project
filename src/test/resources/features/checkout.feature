@checkout
Feature: Placing an order
  As a shopper
  I want to complete checkout
  So that the items in my cart are purchased

  Background:
    Given I am signed in as the standard user

  @smoke
  Scenario: A shopper completes an order end to end
    When I add "Sauce Labs Backpack" to the cart
    And I open the cart
    And I proceed to checkout
    And I enter checkout details "Phyo", "Oo" and "10001"
    And I confirm the order
    Then I see the order confirmation "Thank you for your order!"

  @regression
  Scenario: The overview reflects the cart before payment
    When I add "Sauce Labs Backpack" to the cart
    And I add "Sauce Labs Bike Light" to the cart
    And I open the cart
    And I proceed to checkout
    And I enter checkout details "Phyo", "Oo" and "10001"
    Then the order overview lists:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the order item total is 39.98

  @regression
  Scenario Outline: Checkout refuses incomplete delivery details
    When I add "Sauce Labs Backpack" to the cart
    And I open the cart
    And I proceed to checkout
    And I enter checkout details "<first>", "<last>" and "<postcode>"
    Then I see the checkout error "<message>"

    Examples:
      | first | last | postcode | message                          |
      |       | Oo   | 10001    | Error: First Name is required    |
      | Phyo  |      | 10001    | Error: Last Name is required     |
      | Phyo  | Oo   |          | Error: Postal Code is required   |
