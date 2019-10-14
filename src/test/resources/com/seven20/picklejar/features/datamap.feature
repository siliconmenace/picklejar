Feature: Tests that various values can be stored across step definitions

@datamap
Scenario Outline:: scenario description
    Given I have stored "<input>"
    Then I should retrieve the same "<output>"
    
    Examples:
    |input     | output    |
    |Inputset  | Inputset  |
   
	
	
