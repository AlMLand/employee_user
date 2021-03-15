insert into address (id, version, city, house_number, post_code, street) 
values(1, 0, 'test_city', 10, '12345', 'test_street');
insert into email (id, version, email)
values(1, 0, 'test@mail.com');
insert into user (id, version, password, user_role, username) 
values(1, 0, '12345', 'DEVELOPMENT', 'test_username');
insert into employee (id, version, address_employee, age, email_id, first_name, last_name, salary, user_id) 
values(1, 0, 1, 35, 1, 'test_firstname', 'test_lastname', 5000, 1);