Feature: signup
  Signup process for new users to create an account on okta, then activate using code send by SMS.

  @Disabled
  Scenario: subscription of new user
    Given a user with the given details
      | email                 | password | lastname | firstname  | phoneNumber |
      | newlight77@gmail.com  | mysecret | To       | Kong      | 0123456789  |
    When he subscribes on the platform
    Then an account is created
    And and notification is send by email with a message
    And an activation code is send via SMS to the phone number

