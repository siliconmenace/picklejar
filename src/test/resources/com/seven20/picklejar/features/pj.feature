Feature: ATAFeature
Simple Test feature to test whether the environment is set up correctly
#ignoring this test because the last step sometimes fails. For some reason google's page
#will return different size text, causing the image being searched for to not be on screen.
#could refactor to pass, but should plan eventually to just use unit tests instead.
@ignore
Scenario Outline:: scenario description
    Given I have opened a link to "<url>" 
    When I do search for "<criteria>"
    Then I should see "<results>" using "<xpath>"
    
    Examples:
    |url|criteria|results|xpath|
    |Google|Gherkin|gherkinImages|Google_xpath_1|
#    |Yahoo|Gherkin|Gherkin · cucumber/cucumber Wiki|Google_xpath_1|
#    |Yahoo|BDD|Behavior-driven development|Google_xpath_1|