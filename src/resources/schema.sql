create table if not exists City (id varchar(255) not null, country varchar(255) not null, name varchar(255) not null, primary key (id));

create table if not exists Category (CATEGORY_TYPE varchar(50) not null, PRICE double not null, STANDARD_INSURANCE double not null, FULL_INSURANCE double not null, 
									 primary key (CATEGORY_TYPE));

create table if not exists Model (id int not null, BRAND varchar(255) not null, description varchar(255) not null, CATEGORY_ID varchar(255) not null, primary key (id));

create table if not exists Car (LICENSE_PLATE varchar(20) not null, MODEL_ID int not null, primary key (LICENSE_PLATE));

create table if not exists Customer (id int not null, fullName varchar(100) not null, email varchar(100), phoneNumber varchar(30), 
									 address varchar(100), zipCode varchar(20), city int not null, primary key (id));

create table if not exists Reservation (reservationNumber VARCHAR(50) not null, customer int not null, car varchar(50) not null, insurance varchar(50) not null,
										pickupLocation int not null, pickupDateTime datetime not null, dropofflocation int not null, dropoffDateTime datetime not null,
										primary key (reservationNumber));
