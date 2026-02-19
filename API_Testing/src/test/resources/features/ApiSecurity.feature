@api-security
Feature: SMSPy API Security Testing

  # Background: Common setup for authenticated tests
  Background:
    Given login as "bryan1" and store token

  Scenario: Test SQL Injection in Login Endpoint (could leak DB users)
    When I send POST _token_ with SQL injection in username "' OR 1=1 --" and password "any"
    Then the response should not succeed or leak database errors

  Scenario: Test SQL Injection in Message Content (could execute malicious DB commands)
    When I send POST _messages_send with receiver ID 2 and injection content "' ; DROP TABLE users --"
    Then the response should not succeed or leak database errors

  Scenario: Test Auth Bypass with Tampered Token (could allow unauthorized DB access)
    Given I have a valid token
    When I tamper the token by changing user_id to 2
    And send GET _protected_ with tampered token
    Then the response should return 401 or 403 Unauthorized

  Scenario: Test Broken Object Level Authorization (BOLA) in Profile Update (could modify other user's DB record)
    When I attempt PUT _profile_ with another user ID 2
    Then the response should deny access with 403 or 404

  Scenario: Test Unauthorized Access to Protected Endpoint (could expose DB user data)
    When I send GET _protected_ without token
    Then the response should return 401 Unauthorized

  Scenario: Test Injection in Register Endpoint (could insert malicious DB data)
    When I send POST _register_ with injection in email "test@injected.com' --"
    Then the response should not succeed or leak database errors

  Scenario: Test Rate Limiting Abuse on Send Message (could spam DB)
    When I send multiple POST _messages_send_ requests in quick succession
    Then the backend should rate-limit or reject after threshold

  Scenario: Test SQL Injection in Search/Query Params (if exists)
    When I send GET _users_ with injection param "?search=' OR 1=1 --"
    Then response should not return all DB users

  Scenario: Test DB Leak via Error Messages in Registration
    When I send invalid POST _register_ with excessively long username
    Then the error response should return 4xx or 500
    And the error response should not expose DB schema or SQL details

  Scenario Outline: Test DB Leak via Malicious Characters in Registration
    When I send POST _register_ with malicious characters in email <maliciousEmail>
    Then the error response should return 4xx or 500
    And the error response should not expose DB schema or SQL details
    Examples:
      | maliciousEmail                                                                                                         |
      | "'; DROP TABLE user_messages_customuser; --"                                                                           |
      | "<script>alert('xss')</script>"                                                                                        |
      | "test@domain.com\0"                                                                                                    |
      | "veryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryveryverylongemail@domain.com" |
