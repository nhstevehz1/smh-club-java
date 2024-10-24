CREATE SCHEMA clubmgmt;

CREATE TABLE clubmgmt.users
(
    user_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    username varchar(25) NOT NULL,
    first_name varchar(40) NOT NULL,
    middle_name varchar(40),
    last_name varchar(40) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT u_users__username UNIQUE (username)
);

CREATE TABLE clubmgmt.roles
(
    role_id int NOT NULL,
    role_name varchar(30) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (role_id)
);

CREATE TABLE clubmgmt.user_roles
(
    user_id int NOT NULL,
    role_id int NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES clubmgmt.users (user_id),
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES clubmgmt.roles (role_id),
    CONSTRAINT u_user_id__role_id UNIQUE (user_id, role_id)
);