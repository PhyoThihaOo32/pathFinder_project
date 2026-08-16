@interactions
Feature: Advanced browser interactions
  As a test engineer
  I want coverage of the trickier WebDriver interactions
  So that the framework is proven against checkboxes, selects, hovers and windows

  @regression
  Scenario: Checkbox state can be set in both directions
    Given the checkboxes page is open
    When I check checkbox 1
    And I uncheck checkbox 2
    Then checkbox 1 is checked
    And checkbox 2 is unchecked

  @regression
  Scenario: A native select exposes and accepts its options
    Given the dropdown page is open
    Then the dropdown offers:
      | Please select an option |
      | Option 1                |
      | Option 2                |
    When I select "Option 2" from the dropdown
    Then the dropdown shows "Option 2"

  @regression
  Scenario: Hovering an avatar reveals its caption
    Given the hovers page is open
    When I hover over avatar 1
    Then the caption for avatar 1 reads "name: user1"

  @regression
  Scenario: A link opens and closes a child window
    Given the windows page is open
    When I open the child window
    Then the child window heading is "New Window"
    When I close the child window
    Then 1 window remains open
