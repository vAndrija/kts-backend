INSERT INTO roles (name) values ('ROLE_SYSTEM_ADMIN');
INSERT INTO roles (name) values ('ROLE_BARTENDER');
INSERT INTO roles (name) values ('ROLE_COOK');
INSERT INTO roles (name) values ('ROLE_MANAGER');
INSERT INTO roles (name) values ('ROLE_WAITER');


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('prva@prva','mirko','miric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','225883','2131231231231',true,false);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1);
INSERT INTO bartender (users) values (1);

