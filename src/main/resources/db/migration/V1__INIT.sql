CREATE SCHEMA project;
SET search_path = project, pg_catalog;

CREATE SEQUENCE roles_seq START WITH 3 INCREMENT BY 1;

CREATE TABLE users (
    id uuid,
    username text,
    email text,
    password text,
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id integer,
    name text,
    PRIMARY KEY (id)
);

CREATE TABLE user_role (
    user_id uuid NOT NULL,
    role_id integer NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES project.users (id),
    FOREIGN KEY (role_id) REFERENCES project.roles (id)
);

INSERT INTO roles (id, name)
VALUES
(1, 'ADMIN'),
(2, 'USER');


