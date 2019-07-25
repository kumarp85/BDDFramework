#Sample Feature Definition Template
@tag
Feature: User Manager Campaign Config

  @tag1
  Scenario: User Manager Campaign Config Demo
    Given I Navigate to Saviynt cloud Page
    When I Enter userName as userName_Field1 
    And I Enter password as password_Field1
    And I Click on login button
    And I Click on Admin button
    And I Click on Configurations button
    And I Click on Global_Configurations button
    And I Click on User_Manager_Campaign_Config button
    And I Click on Include_Users_without_Accounts dropdown
    And I Select Users_without_Accounts value as Yes
    And I Switch Certify_All_Users_By_Default value as ON
    And I Select Reassign checkbox
