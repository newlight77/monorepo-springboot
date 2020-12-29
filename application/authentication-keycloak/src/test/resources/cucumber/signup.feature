Feature: signup
  In order to usse access some or all functionalities provided by the portal,
  a user must signup to create an account on the portal and the IAM service.
  He will then define be assigned one or many profile according to his status.

  The signup process will follow a workflow with multiple steps:

  1. user registration on IAM
  2. activate using code send by SMS
  3. upload of a resume
  4. choose a status
  5. account validation an administrator

  Depending on the role, the second part of the workflow varies:

  A. As a freelance:
    1. the user must fill the enterprise form
    2. he must also fill the mission seeking form
    3. upon completion of forms, the account is fully activated

  B. As a collaborator
    TBD

  C. As a customer
    TBD

  Rule:

  1. ensure uniqueness of username, identified by email
  2. password must be at least 8 characters, alphanumeric
  3. validation by code must go through a valid GMS phone number to receive the code by SMS
  4. the email address is validated at the same step as by SMS (later version)
  5. roles are assigned to the user upon status update
    ex: a freelance will have access to freelance views, and general purpose views regarding tricefal
    ex: a collaborator will have access to collaborator views, and general purpose views regarding tricefal
    ex : a customer will have access to customer views, and general purpose views regarding tricefal
  6.

  Glossary:

  - status: a given user is given one or many status such as freelance, collaborator, customer and backoffice
  - profile: a composition of roles according to a status (freelance, customer, collaborator, backoffice).
    A profile can be translated to a status, but it will be used purposely for authorization management
  - roles: a role is associated to a right that the portal would use to verify a user can or not access to a functionality
  - freelance: a freelance status is associated to a consultant who has his own enterprise declared juridically.
    He can then access the portal and benefit from the business model that tricefal provides. He will be assigned a mission
  - collaborator: a collaborator is an employee and consultant of tricefal. He will be assigned a mission
  - customer: a customer is the one who will give a mission order and he seeks for experts to get things done (ex. software developement)
  - backoffice: a backoffice status is a hidden as it is meant for internal purpose.
  - general purpose views regarding tricefal
  - resume: A resume is a formal document that a job applicant creates to itemize his or her qualifications for a position.
  - position: a position is what defines tasks, roles and responsibilities are assigned to a consultant
  - mission: a collaborator or a freelance will seek for a mission for consulting services.
    a mission is a mandate that a customer would give to a consultant expert for a given position and  timeframe (days , months or years)

  @signup @Disabled
  Scenario: user creation upon signup
    Given a user with the given details
      | email                 | password | lastname | firstname  | phoneNumber |
      | newlight77@gmail.com  | mysecret | To       | Kong      | 0123456789  |
    When he subscribes on the platform
    Then an account is created
    And and notification is send by email with a message
    And an activation code is send via SMS to the phone number

