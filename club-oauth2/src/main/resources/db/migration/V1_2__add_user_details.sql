CREATE TABLE auth.user
(
    id                      bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    username                varchar(20) NOT NULL,
    password                varchar(20) NOT NULL,
    account_non_expired     boolean DEFAULT FALSE,
    account_non_locked      boolean DEFAULT FALSE,
    credentials_non_expired boolean DEFAULT FALSE,
    enabled                 boolean DEFAULT FALSE, -- for safety

    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT unique_password_user UNIQUE (username)
);

CREATE TABLE auth.user_granted_authority
(
    id        bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id   bigint      NOT NULL,
    authority varchar(20) NOT NULL,

    CONSTRAINT pk_user_granted_authority PRIMARY KEY (id),
    CONSTRAINT fk_user_granted_authorities__user FOREIGN KEY (user_id) REFERENCES auth.user (id)
);
