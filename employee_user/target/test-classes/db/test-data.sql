insert into addresses (id, version, city, housenumber, postcode, street) 
values(1, 0, 'test_city', 10, '12345', 'test_street');

insert into emails (id, version, email) 
values(1, 0, 'test@mail.com');

insert into users (id, version, password, username) 
values(1, 0, '12345', 'test_username');

insert into roles (id, version, role) 
values (1, 0, 'DEVELOPMENT');

insert into employees (id, version, address_employee, birthdate, age, email_id, firstname, lastname, salary, user_id) 
values(1, 0, 1, '1985-06-05', 35, 1, 'test_firstname', 'test_lastname', 5000, 1);

insert into users_roles (user_fk, role_fk) 
values (1, 1);