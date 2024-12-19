CREATE TABLE auth.users
(
    id                      bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    username                varchar(30) NOT NULL,
    password                varchar(20) NOT NULL,
    account_non_expired     boolean DEFAULT FALSE,
    account_non_locked      boolean DEFAULT FALSE,
    credentials_non_expired boolean DEFAULT FALSE,
    enabled                 boolean DEFAULT FALSE, -- for safety

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_users__username ON auth.users (username);

CREATE TABLE auth.user_authorities
(
    id          bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id     bigint NOT NULL,
    authority   varchar(20) NOT NULL,

    CONSTRAINT pk_user_authorities PRIMARY KEY (id),

    CONSTRAINT fk_user_authorities__user
        FOREIGN KEY (user_id)
        REFERENCES auth.users (id)
);

CREATE INDEX idx_user_authorities__user_id
    ON auth.user_authorities (user_id);
