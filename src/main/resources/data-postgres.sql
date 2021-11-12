insert into roles (name) values ('ROLE_SYSTEM_ADMIN');
insert into roles (name) values ('ROLE_PHARMACY_ADMIN');
insert into roles (name) values ('ROLE_DERMATOLOGIST');
insert into roles (name) values ('ROLE_PHARMACIST');
insert into roles (name) values ('ROLE_SUPPLIER');
insert into roles (name) values ('ROLE_PATIENT');


insert into users (email_address, name, last_name, password, phone_number, account_number) values
    ('prva@prva','mirko','miric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','225883','2131231231231');
INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1);
insert into bartender (users) values (1);
