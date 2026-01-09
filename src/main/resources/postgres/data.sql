DELETE FROM service_user;
DELETE FROM user_role;

ALTER SEQUENCE serviceuser_id_seq RESTART;
INSERT INTO service_user(username, password) VALUES ('root',
                                                    '$2a$12$HB8VG0RtaTXHpaB.0fyzC./Lw20b7BkZF.a1F1mRz68zI1Xpx0h6a');
INSERT INTO service_user(username, password) VALUES ('user',
                                                    '$2a$12$hdhdBUpnmMwTitY0o7Tai./KlMys.46Z3RuJZ5/yMBy7g8G3g3tpC');
INSERT INTO user_role VALUES (1, 'ROLE_SUPERUSER');
INSERT INTO user_role VALUES (2, 'ROLE_PATIENT');
INSERT INTO role_authority VALUES ('ROLE_PATIENT', 'GLUCOSE_SHOW_OWN', false);
INSERT INTO role_authority VALUES ('ROLE_PATIENT', 'GLUCOSE_ADD_OWN', false);

