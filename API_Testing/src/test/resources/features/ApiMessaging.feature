@api-messaging
Feature: SMS Messaging API - Core Business Flows

  Background:
    # Authenticate a known sender
    Given login as sender "bryan1"

    # Get a known receiver
    Given I have receiver "bryan2" with ID 2

  # ──────────────────────────────────────────────
  # Core messaging flow
  # ──────────────────────────────────────────────

  Scenario: Send a simple text message and verify in receiver inbox
    When I send a message to receiver with content "Hello bryan2! This is a test from bryan1"
    Then the message should be successfully sent
    When login as receiver "bryan2"
    And I send a GET request to "/messages/received/"
    Then I should see the message I just sent in the list

  Scenario: Send a message with an image attachment
    When I send a message with image to receiver
    Then the message should be successfully sent
    And the response should contain a valid S3 image URL

  Scenario: Update profile avatar and verify propagation
    Given login as "user_9fbb"
    When I update my profile avatar with new image
    Then the user profile should show the new avatar URL

  # ──────────────────────────────────────────────
  # Negative / edge cases (real-world important)
  # ──────────────────────────────────────────────

  Scenario: Attempt to send message to non-existent receiver
    Given login as sender "bryan1"
    When I send a message to invalid receiver ID 9999 with content "This should fail"
    Then the response should return an error status

  Scenario: Attempt to send message without authentication
    When I send a POST request to "/messages/send/" without token with content "Unauthorized test"
    Then the response should return 401 Unauthorized

  # ──────────────────────────────────────────────
  # Bonus: Multi-user interaction chain (realistic fintech/messaging test)
  # ──────────────────────────────────────────────

  @exchange-messages
  Scenario Outline: Message exchange between two users
    Given login as sender "<sender>"
    And I lookup receiver "<receiver>" ID from users list
    When I send a message to receiver with content "<messageContent>"
    Then the message should be successfully sent
    When login as "<receiver>"
    And I send a GET request to "/messages/received/"
    Then I should see a message from "<sender>" with content "<messageContent>" as the most recent in the list

    Examples:
      | sender          | receiver        | messageContent                  |
      | bryan1          | smspy_test_user | Team update from bryan1         |
      | smspy_test_user | bryan2          | Reply from test user            |