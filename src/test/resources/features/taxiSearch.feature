Feature: Taxi search on Booking.com
  Scenario: Search by place
    Given User is looking for a taxi in 'London Luton Airport' place
    When User sets 'Piccadilly Circus Stylish Studio Apartment, London, GB' as a destination
    Then User searches for a taxi
    And Sets the date of the ride
    And Sets the time of the ride
    And Sets the quantity of passengers
    Then Taxi category 'Standard' should be on the search results page
