Feature: FindConditions

  @find
  Scenario: Find Conditions Feature
    Given I have logged into "devpti1console" using "user1"
    When I click on "Record.to.Report"
    And select "Setup"
    Then I must see the setup binders page

  @find
  Scenario: 
    Given I am on the setup binders page
    When I open an "Binders.Name" under the Binders Page
    Then I must click on the "Binders.Entity.Link"
