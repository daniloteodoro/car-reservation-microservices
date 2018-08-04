Feature: Rent a car
  Ensure that I can book a car

  Scenario: A visitor can book a car
    Given that I want to rent a VW Golf in Rotterdam from 03/07/2018 to 08/07/2018
    When I try to make a reservation
    Then An order is placed to reserve the car to me