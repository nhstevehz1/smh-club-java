CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.client
(
    id                            varchar(50)   NOT NULL,
    client_id                     varchar(50)   NOT NULL,
    client_id_issued_at           timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(100)  DEFAULT NULL,
    client_secret_expires_at      timestamp     DEFAULT NULL,
    client_name                   varchar(50)   NOT NULL,
    client_settings               varchar(100) NOT NULL, -- holds JSON
    client_token_settings         varchar(1000) NOT NULL, -- holds JSON

    CONSTRAINT pk_client PRIMARY KEY (id),
    CONSTRAINT unique_client_id UNIQUE (client_id)
);

CREATE TABLE auth.client_auth_methods_set (
    client_id varchar(30)   NOT NULL,
    auth_method varchar(30) NOT NULL,

    CONSTRAINT fk_client_auth_methods_set__client
        FOREIGN KEY (client_id)
            REFERENCES auth.client (id)
);

CREATE TABLE auth.client_grant_types_set (
    client_id varchar(30)   NOT NULL,
    grant_type varchar(30)  NOT NULL,

    CONSTRAINT fk_client_grant_types_set__client
        FOREIGN KEY (client_id)
            REFERENCES auth.client (id)
);

CREATE TABLE auth.client_redirect_uri_set (
    client_id varchar(30)       NOT NULL,
    redirect_uri varchar(100)   NOT NULL ,

    CONSTRAINT fk_client_redirect_uri_set__client
       FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.client_logout_redirect_uri_set (
    client_id varchar(30)       NOT NULL,
    redirect_uri varchar(100)   NOT NULL,

    CONSTRAINT fk_logout_redirect_uri_set__client
        FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.client_scopes_set (
    client_id varchar(30)   NOT NULL,
    scope varchar(30)       NOT NULL,

    CONSTRAINT fk_client_scopes_set__client
       FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);
