INSERT INTO roles (name) values ('ROLE_SYSTEM_ADMIN');
INSERT INTO roles (name) values ('ROLE_BARTENDER');
INSERT INTO roles (name) values ('ROLE_COOK');
INSERT INTO roles (name) values ('ROLE_MANAGER');
INSERT INTO roles (name) values ('ROLE_WAITER');


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('mirkomiric@gmail.com','mirko','miric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0608963214','2131231231231',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO admin (users) values (1);


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('lukaperic@gmail.com','luka','peric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0632589641','6332238931255',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO bartender (users, priority) values (2, false);

INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('milossaric@gmail.com','milos','saric', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0645599641','1132258931278',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (3, 2);
INSERT INTO bartender (users, priority) values (3, true);


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('kristinamisic@gmail.com','kristina','misic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0697425831','85315612318963',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (4, 3);
INSERT INTO cook (users, priority) values (4, false);

INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('urosmatic@gmail.com','uros','matic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0698620031','42318612311963',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (5, 3);
INSERT INTO cook (users, priority) values (5, true);


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('sarajovic@gmail.com','sara','jovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0647456821','78615616918959',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (6, 4);
INSERT INTO manager (users) values (6);


INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('jovanpetrovic@gmail.com','jovan','petrovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0607425922','22365612316263',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (7, 5);
INSERT INTO waiter (users) values (7);

INSERT INTO users (email_address, name, last_name, password, phone_number, account_number,enabled,deleted) values
    ('anapopovic@gmail.com','ana','popovic', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra','0627412922','22005612314563',true,false);
INSERT INTO user_role (user_id, role_id) VALUES (8, 5);
INSERT INTO waiter (users) values (8);

INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (45000.00, '2021-11-18', '2022-11-18', 1, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (50000.00, '2021-10-05', '2022-10-05', 2, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (60000.00, '2021-08-18', '2022-08-18', 3, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (50000.00, '2021-11-18', '2022-11-18', 4, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (65000.00, '2021-02-01', '2022-02-01', 5, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (70000.00, '2021-01-05', '2022-01-05', 6, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (62000.00, '2021-11-18', '2022-11-18', 7, false);
INSERT INTO salary (value, start_date, end_date, user_id, deleted) values (62000.00, '2021-11-18', '2022-11-18', 8, false);


INSERT INTO restaurant_table (table_number, capacity, x_coordinate, y_coordinate, deleted) values (1, 4, 0, 1, false);
INSERT INTO restaurant_table (table_number, capacity, x_coordinate, y_coordinate, deleted) values (2, 6, 2, 1, false);
INSERT INTO restaurant_table (table_number, capacity, x_coordinate, y_coordinate, deleted) values (3, 2, 2, 3, false);
INSERT INTO restaurant_table (table_number, capacity, x_coordinate, y_coordinate, deleted) values (4, 4, 4, 1, false);


INSERT INTO table_reservation (name, duration_start, table_id, deleted) values ('Milica Petric', '2021-11-18T16:00', 1, false);
INSERT INTO table_reservation (name, duration_start, table_id, deleted) values ('Ana Jojic', '2021-11-20T19:00', 2, false);


INSERT INTO menu (name, duration_start, duration_end, deleted) values ('standardni', '2021-11-18T08:00', '2022-11-18T08:00', false);
INSERT INTO menu (name, duration_start, duration_end, deleted) values ('letnji', '2021-06-01T08:00', '2021-08-31T23:59', false);


INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('coca cola', 'bezalkoholno gazirano pice', 0, 7, 1, true, false, 2);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('mohito', 'koktel  od belog kubanskog ruma, soka limetе, sveze nane i mineralne vode', 0, 5, 1, false, false, 8);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('domaca kafa', 'topli napitak', 0, 6, 1, true, false, 5);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('limunada', 'bezalkoholno pice', 0, 7, 1, true, false, 2);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('domaca supa', 'pileca supa sa rezancima', 1, 0, 1, true, false, 10);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('paradajz corba', 'paradajz corba sa slaninicom', 1, 0, 1, true, false, 12);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('dimljeni saran', 'dimljeni saran sa grilovanim povrcem', 1, 3, 1, true, false, 15);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('lignje sa zara', 'lignje sa zara, prilog krompir sa blitvom', 1, 3, 1, true, false, 17);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('pileci file u senfu i medu', 'pileci file u senfu i medu, prilog grilovano povrce ', 1, 3, 1, true, false, 13);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('punjeni pileci batak na zaru', 'pileci batak punjen sirom i slaninom, prilog peceni krompir', 1, 3, 1, true, false, 15);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('strudla sa makom', 'strudla sa makom', 1, 4, 1, true, false, 5);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('baklava', 'baklava sa orasima', 1, 4, 1, true, false, 5);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('krempita', 'krempita sa dodatkom visnje', 1, 4, 2, true, false, 5);
INSERT INTO menu_item (name, description, type, category, menu_id, accepted, deleted, preparation_time) values
    ('pita sa visnjama', 'pita sa visnjama, belom cokoladom i sladoledom od vannile', 1, 4, null, false, false, 5);


INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 180.00, '2021-11-18', '2022-12-22', 1, true, false, 100.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 600.00, '2021-11-18', '2022-12-22', 2, true, false, 450.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 100.00, '2021-11-18', '2022-12-22', 3, true, false, 35.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 150.00, '2021-11-18', '2022-12-22', 4, true, false, 60.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 265.00, '2021-11-18', '2022-12-22', 5, true, false, 130.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 290.00, '2021-11-18', '2022-12-22', 6, true, false, 175.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 990.00, '2021-11-18', '2022-12-22', 7, true, false, 390.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 1070.00, '2021-11-18', '2022-12-22', 8, true, false, 450.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 820.00, '2021-11-18', '2022-12-22', 9, true, false, 250.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 760.00, '2021-11-18', '2022-12-22', 10, true, false, 210.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 250.00, '2021-11-18', '2022-12-22', 11, true, false, 100.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 250.00, '2021-11-18', '2022-12-22', 12, true, false, 100.00);
INSERT INTO price_item (value, start_date, end_date, menu_item_id, is_current, deleted, preparation_value) values
    ( 280.00, '2021-11-18', '2022-12-22', 13, true, false, 125.00);


INSERT INTO discount (value, start_date, end_date, menu_item_id, is_current, deleted) values
    (10, '2021-11-20', '2021-11-25', 8, false, false);


INSERT INTO restaurant_order (status, date_of_order, price, table_id, waiter_users, deleted) values
    (0, '2021-11-19T14:15', 1520, 3, 7, false);
INSERT INTO restaurant_order (status, date_of_order, price, table_id, waiter_users, deleted) values
    (0, '2022-11-18T13:00', 1830, 4, 8, false);
INSERT INTO restaurant_order (status, date_of_order, price, table_id, waiter_users, deleted) values
    (0, '2022-11-18T12:48', 810, 2, 7, false);

INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 1, 2, 0, null, null ,1, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 0, null, null, 7, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 0, null, null, 8, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 1, 1, 1, 2, null, 3, 2, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 1, 1, 1, 3 , null, 4, 2, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 1, null, 4, 9, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 1, null, 5, 10, 1, false);

INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 1, 1, 2, 2, null, 1, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 1, 1, 2, 3 , null, 3, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 1, null, 5, 12, 1, false);
INSERT INTO order_item (note, priority, quantity, status, bartender_users,  cook_users, menu_item_id, order_id, deleted) values
    ('', 2, 1, 1, null, 4, 13, 1, false);

INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Napravljena je nova porudzbina.', 2, true, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Napravljena je nova porudzbina.', 3, true, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Pileci file u senfu i medu je u pripremi.', 4, true, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Punjeni pileci batak na zaru je u pripremi.', 5, true, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Domaca kafa je pripremljena.', 6, false, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Baklava je u pripremi.', 7, false, false);
INSERT INTO notification (message, order_item_id, seen, deleted) values
    ('Krempita je u pipremi.', 8, false, false);

