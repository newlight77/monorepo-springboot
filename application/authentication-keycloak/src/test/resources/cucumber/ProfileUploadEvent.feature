Feature: Profile Files Upload Events

  @Disabled
  Scenario: Profile creatioin upon a resume file upload
    Given a valid user with username "newlight77@gmail.com"
    When resume file is uploaded
    Then an a profile is created with a resume file for that user