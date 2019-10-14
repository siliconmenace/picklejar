Feature: Testing the logic to take screenshots present in Hooks class

  @screenshot
  Scenario Outline: : scenario description1
    Given I have opened a link to "<url>"
    When I do search for "<criteria>"
    Then I should see "<results>" using "<xpath>"

    # should take screen shot at the end of each step and at the very end
    Examples: 
      | url    | criteria | results       | xpath          |
      | Google | Gherkin  | gherkinImages | Google_xpath_1 |


  @noscreenshot
  Scenario Outline: : scenario description3
    Given I have opened a link to "<url>"
    When I do search for "<criteria>"
    Then I should see "<results>" using "<xpath>"

    # do not take the screen shot if the sscenarios/steps pass
    Examples: 
      | url    | criteria | results       | xpath          |
      | Google | Gherkin  | gherkinImages | Google_xpath_1 |

  @Test
  Scenario Outline: : scenario description5
    Given I have opened a link to "<url>"
    When I do search for "<criteria>"
    Then I should see "<results>" using "<xpath>"

    #must not execute as the tag is not specified in the runner
    Examples: 
      | url    | criteria | results       | xpath          |
      | Google | Gherkin  | gherkinImages | Google_xpath_1 |
