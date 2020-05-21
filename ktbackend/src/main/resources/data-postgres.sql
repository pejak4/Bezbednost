insert into users (role, first_name, last_name, email, password, first_time_logged) values ('USER', 'Jovan', 'Jenjic', 'jovan.jenjic@gmail.com', '$2a$10$67WhFHSky5EtaPlkTj1nqOIWRTHGNbWqJxtpJTUZMBQA.2am2yKqe', true);
insert into users (role, first_name, last_name, email, password, first_time_logged) values ('ADMIN', 'Stefan', 'Pejakovic', 'stefan.pejakovic@gmail.com', '$2a$10$67WhFHSky5EtaPlkTj1nqOIWRTHGNbWqJxtpJTUZMBQA.2am2yKqe', true);
insert into users (role, first_name, last_name, email, password, first_time_logged) values ('USER', 'Aleksandar', 'Kosic', 'aleksandar.kosic@gmail.com', '$2a$10$67WhFHSky5EtaPlkTj1nqOIWRTHGNbWqJxtpJTUZMBQA.2am2yKqe', true);

insert into authority (name) values ('MANAGE_CERTIFICATE');
insert into authority (name) values ('MANAGE_USER');

insert into role (role) values ('ADMIN');
insert into role (role) values ('USER');

insert into role_authorities (role_name, authority_name) values ('ADMIN', 'MANAGE_CERTIFICATE');
insert into role_authorities (role_name, authority_name) values ('USER', 'MANAGE_USER');

alter sequence users_id_seq restart with 4;