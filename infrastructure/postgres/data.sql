ALTER SEQUENCE serviceuser_id_seq RESTART;
INSERT INTO service_user(username, password, first_name, middle_name, last_name, email, birth_date)
    VALUES ('root','$2a$12$HB8VG0RtaTXHpaB.0fyzC./Lw20b7BkZF.a1F1mRz68zI1Xpx0h6a',
            'root', null, 'root', null, null);
INSERT INTO user_role VALUES (1, 'ROLE_SUPERUSER');
