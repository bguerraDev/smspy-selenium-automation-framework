@ui-full-flow
Feature: SMSPy UI End-to-End Flows

  Scenario Outline: Login and send simple text message
    Given I am on the login page
    When I login as "<sender>"
    Then I am on the inbox page
    When I navigate to compose message page
    And I select receiver "<receiver>" and type content "<message>"
    Then the message is sent successfully
    When I logout and login as "<receiver>" with receiver password
    Then I should see the "<message>" in my inbox

    Examples:
      | sender | receiver | message                                                                       |
      | bryan1 | bryan2   | UI Test message done with Selenium + Cucumber. TAG -> UI-FULL-FLOW . CI/CD WF |

  @update-avatar
  Scenario: Login, send message with image, and verify in inbox
    Given I am on the login page
    When I login as "bryan1"
    Then I am on the inbox page
    When I navigate to compose message page
    And I select receiver "bryan2" and type content "UI test with image. TAG -> UI-FULL-FLOW . CI/CD WF" with image
    And I upload image "src/test/resources/image_message_placeholder.webp"
    Then the message is sent successfully
    When I logout and login as "bryan2" with receiver password
    Then I should see the "UI test with image. TAG -> UI-FULL-FLOW . CI/CD WF" in my inbox

  @update-avatar
  Scenario: Update profile avatar via UI
    Given I am on the login page
    When I login as "bryan1"
    When I navigate to profile page
    And I upload new avatar "src/test/resources/test_avatar_selenium.webp"
    Then the profile avatar is updated