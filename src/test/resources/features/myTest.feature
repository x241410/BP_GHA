Feature: BPED Apps HealthCheck

  Background: 
    Given test data configuration for "BPED Apps HealthCheck"

  @TestApp
  Scenario: TestApp
    Given user login into "TestApp"

  @TestApp
  Scenario: Get Gsheet Data
    Given display gsheet data
