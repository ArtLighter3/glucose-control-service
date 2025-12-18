CREATE TABLE Role_Authority (
    role varchar,
    authority varchar,
    is_deletable bool DEFAULT false,
    PRIMARY KEY (role, authority)
);
