CREATE TABLE ServiceUser (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username varchar(255) UNIQUE NOT NULL,
    password varchar NOT NULL
);

CREATE TABLE User_Role (
                           user_id int REFERENCES serviceuser(id) ON DELETE CASCADE,
                           role varchar,
                           PRIMARY KEY (user_id, role)
);

CREATE TABLE Role_Authority (
    role varchar,
    authority varchar,
    is_deletable bool DEFAULT false,
    PRIMARY KEY (role, authority)
);
