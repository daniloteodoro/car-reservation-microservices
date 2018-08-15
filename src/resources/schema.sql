create table if not exists City (id varchar(255) not null, country varchar(255), name varchar(255), primary key (id));

create table if not exists Model (id int not null, BRAND varchar(255) not null, description varchar(255) not null, Category varchar(255), primary key (id));

create table if not exists Car (LICENSE_PLATE varchar(20) not null, dropoffDateTime timestamp, DROPOFF_LOCATION int, MODEL_ID int not null, 
pickupDateTime timestamp, PICKUP_LOCATION int, pricePerDay double, store varchar(255), primary key (LICENSE_PLATE));
