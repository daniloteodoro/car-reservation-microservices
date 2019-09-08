delete from Reservation;
delete from Customer;
delete from Car;
delete from Model;
delete from Category;
delete from City;

-- Sample cities
insert into City (id, country, name)
values (-1, 'UNKNOWN', 'Unknown');

insert into City (id, country, name)
values (1, 'NL', 'Rotterdam');

insert into City (id, country, name)
values (2, 'US', 'New York');

insert into City (id, country, name)
values (3, 'NL', 'Amsterdam');


-- Sample categories
insert into Category (CATEGORY_TYPE, PRICE, STANDARD_INSURANCE, FULL_INSURANCE)
values ('COMPACT', 35.0, 0, 12.0);

insert into Category (CATEGORY_TYPE, PRICE, STANDARD_INSURANCE, FULL_INSURANCE)
values ('MEDIUMSIZED', 40.0, 0, 18.0);


-- Sample models
insert into Model (id, brand, description, CATEGORY_ID)
values (100, 'VW', 'Golf', 'MEDIUMSIZED');

insert into Model (id, brand, description, CATEGORY_ID)
values (101, 'Peugeot', '307', 'MEDIUMSIZED');

insert into Model (id, brand, description, CATEGORY_ID)
values (102, 'Citroen', 'C3', 'COMPACT');

insert into Model (id, brand, description, CATEGORY_ID)
values (103, 'Peugeot', '207', 'COMPACT');


-- Sample Cars
insert into CAR (LICENSE_PLATE, MODEL_ID)
values ('AB-1234', 100);

insert into CAR (LICENSE_PLATE, MODEL_ID)
values ('BC-3333', 100);

insert into CAR (LICENSE_PLATE, MODEL_ID)
values ('CD-4431', 101);

insert into CAR (LICENSE_PLATE, MODEL_ID)
values ('DE-8271', 102);


-- Sample customers
insert into Customer (id, fullName, email, phoneNumber, address, city_id)
values (-1, 'Unknown', '(N/A)', '(N/A)', '(N/A)', -1);

insert into Customer (id, fullName, email, phoneNumber, address, city_id)
values (500, '(Car Rental)', 'adm@carrental.com', '12-2345-82828', 'Street X, 888', 1);


-- Sample Reservations
--insert into Reservation (reservationNumber, customer, car, insurance, pickupLocation, pickupDateTime, dropofflocation, dropoffDateTime)
--values ('65a1bec3-73e9-4b48-844c-020f9ff2b897', 502, 'AB-1234', 'STANDARD_INSURANCE', 1, '2018-07-01 08:00:00', 1, '2018-07-03 16:00:00');
--
--insert into Reservation (reservationNumber, customer, car, insurance, pickupLocation, pickupDateTime, dropofflocation, dropoffDateTime)
--values ('95a627d4-31b7-457e-85a0-26337b1501c1', 501, 'CD-4431', 'FULL_INSURANCE', 1, '2018-07-07 08:00:00', 1, '2018-07-14 16:00:00');
--
--insert into Reservation (reservationNumber, customer, car, insurance, pickupLocation, pickupDateTime, dropofflocation, dropoffDateTime)
--values ('31e47ae3-c764-4bc8-9415-70d3a6bcb4e5', 500, 'AB-1234', 'STANDARD_INSURANCE', 1, '2018-07-15 08:00:00', 1, '2018-07-15 18:00:00');
--
--insert into Reservation (reservationNumber, customer, car, insurance, pickupLocation, pickupDateTime, dropofflocation, dropoffDateTime)
--values ('7991b9c5-99b0-4fe0-bf97-392441d27c91', 501, 'AB-1234', 'FULL_INSURANCE', 1, '2018-07-16 08:00:00', 1, '2018-07-20 18:00:00');
--
--insert into Reservation (reservationNumber, customer, car, insurance, pickupLocation, pickupDateTime, dropofflocation, dropoffDateTime)
--values ('441ed396-eadd-4bde-abab-2d0aa9bae118', 502, 'DE-8271', 'STANDARD_INSURANCE', 1, '2018-07-29 08:00:00', 1, '2018-08-02 15:00:00');

