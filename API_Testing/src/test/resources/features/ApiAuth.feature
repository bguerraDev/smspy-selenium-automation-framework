@api-auth-feature
Feature: Real App API Authentication

  Scenario: Register a new user and immediately login successfully
    Given I have a new user payload with dynamic username
    When I send a POST request to "/register/"
    Then the response status code should be 201
    And the response should contain the success message
    When I send a POST request to "/token/" with the newly registered credentials
    Then the response status code should be 200
    And the response should contain valid access and refresh tokens

  @multiple-users
  Scenario Outline: Login with valid credentials
    Given I have valid credentials for user "<username>"
    When I send a POST request to "/token/" with those credentials
    And the response status code should be 200
    Then the response should contain valid access and refresh tokens
    Examples:
      | username        |
      | bryan1          |
      | bryan2          |
      | John_Doe        |
      | Bryan_Guerra    |
      | smspy_test_user |

  @api-auth
  Scenario: Login with all live users from database
    Given I have the list of live usernames from database
    When I attempt login for each live user
    Then all logins should succeed with valid tokens