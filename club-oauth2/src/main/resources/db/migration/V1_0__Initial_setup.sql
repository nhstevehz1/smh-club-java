CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.client
(
    id                            varchar(50)   NOT NULL,
    client_id                     varchar(50)   NOT NULL,
    client_id_issued_at           timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(100)  DEFAULT NULL,
    client_secret_expires_at      timestamp     DEFAULT NULL,
    client_name                   varchar(50)   NOT NULL,

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

CREATE TABLE auth.redirect_uri_set (
    client_id varchar(30)       NOT NULL,
    redirect_uri varchar(100)   NOT NULL ,

    CONSTRAINT fk_redirect_uri_set__client
       FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.logout_redirect_uri_set (
    client_id varchar(30)       NOT NULL,
    redirect_uri varchar(100)   NOT NULL,

    CONSTRAINT fk_logout_redirect_uri_set__client
        FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.scopes_set (
    client_id varchar(30)   NOT NULL,
    scope varchar(30)       NOT NULL,

    CONSTRAINT fk_scopes_set__client
       FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.client_settings_map (
    client_id varchar(50)       NOT NULL,
    setting_name varchar(30)    NOT NULL,
    setting varchar(30),

    CONSTRAINT fk_client_settings__client
        FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.token_settings_map (
    client_id varchar(50)       NOT NULL,
    setting_name varchar(30)    NOT NULL,
    setting varchar(30),

    CONSTRAINT fk_token_settings__client
        FOREIGN KEY (client_id)  REFERENCES auth.client (id)
);

CREATE TABLE auth.authorization
(
    id                            varchar(50) NOT NULL,
    registered_client_id          varchar(50) NOT NULL,
    principal_name                varchar(30) NOT NULL,
    authorization_grant_type      varchar(255) NOT NULL,
    authorized_scopes             varchar(1000) DEFAULT NULL,
    attributes                    varchar(4000) DEFAULT NULL,
    state                         varchar(500)  DEFAULT NULL,

    CONSTRAINT pk_auth PRIMARY KEY (id)
);

CREATE TABLE auth.oauth2_token (
    id varchar(50),
    auth_id varchar(50),
    token_type varchar(30) NOT NULL,
    token_value varchar(4000) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at TIMESTAMP DEFAULT NULL,
    metadata varchar(2000) NOT NULL,
    scopes varchar(1000) DEFAULT NULL,
    claims varchar(1000) DEFAULT NULL,

    CONSTRAINT pk_oath2_token PRIMARY KEY (id, token_type),
    CONSTRAINT fk_oath2_token__authorization
        FOREIGN KEY (auth_id)
        references auth.authorization (id)
);

CREATE TABLE auth.authorization_consent (
    registered_client_id varchar(50)  NOT NULL,
    principal_name       varchar(255)  NOT NULL,
    authorities          varchar(1000) NOT NULL,

    CONSTRAINT pk_auth_consent PRIMARY KEY (registered_client_id, principal_name)
);

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

CREATE TABLE auth.granted_authority
(
    id        bigint      NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id   bigint      NOT NULL,
    authority varchar(20) NOT NULL,

    CONSTRAINT pk_granted_authorities PRIMARY KEY (id),
    CONSTRAINT fk_granted_authorities_user FOREIGN KEY (user_id) REFERENCES auth.user (id)
);
