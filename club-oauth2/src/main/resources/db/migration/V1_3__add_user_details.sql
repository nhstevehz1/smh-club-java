CREATE TABLE auth.users
(
    username                varchar(30) NOT NULL,
    password                varchar(20) NOT NULL,
    account_non_expired     boolean DEFAULT FALSE,
    account_non_locked      boolean DEFAULT FALSE,
    credentials_non_expired boolean DEFAULT FALSE,
    enabled                 boolean DEFAULT FALSE, -- for safety

    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE UNIQUE INDEX idx_users__username ON auth.users (username);

CREATE TABLE auth.user_authorities
(
    username   varchar(30)  NOT NULL,
    authority   varchar(20) NOT NULL,

    CONSTRAINT fk_user_granted_authorities__user
        FOREIGN KEY (username)
        REFERENCES auth.users (userName)
);

CREATE UNIQUE INDEX idx_auth__username ON auth.user_authorities (username, authority);
