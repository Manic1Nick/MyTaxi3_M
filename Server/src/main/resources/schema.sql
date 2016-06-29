CREATE DATABASE mytaxi;

use mytaxi;

CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    country VARCHAR (20),
    city VARCHAR (20),
    street VARCHAR (20),
    house_num VARCHAR (10)
);

CREATE TABLE cars (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR (20),
    model VARCHAR (20),
    number VARCHAR (8)
);

CREATE TABLE identifiers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR (1),
    description VARCHAR (255)
);

CREATE TABLE statuses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR (20),
    description VARCHAR (255)
);

CREATE TABLE users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    identifier_id INT,
    FOREIGN KEY (identifier_id) REFERENCES identifiers(id),
    phone VARCHAR (12),
    pass VARCHAR (20),
    name VARCHAR (20),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES addresses(id),
    car_id INT,
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status_id INT,
    FOREIGN KEY (status_id) REFERENCES statuses(id),
    addressfrom_id INT,
    FOREIGN KEY (addressfrom_id) REFERENCES addresses(id),
    addressto_id INT,
    FOREIGN KEY (addressto_id) REFERENCES addresses(id),
    passenger_id INT,
    FOREIGN KEY (passenger_id) REFERENCES users(id),
    driver_id INT,
    FOREIGN KEY (driver_id) REFERENCES users(id),
    distance INT,
    price INT,
    message VARCHAR (255)
);

INSERT INTO statuses (status) VALUES ('NEW');
INSERT INTO statuses (status) VALUES ('IN_PROGRESS');
INSERT INTO statuses (status) VALUES ('CANCELLED');
INSERT INTO statuses (status) VALUES ('DONE');

INSERT INTO identifiers (type) VALUES ('P');
INSERT INTO identifiers (type) VALUES ('D');
INSERT INTO identifiers (type) VALUES ('A');

