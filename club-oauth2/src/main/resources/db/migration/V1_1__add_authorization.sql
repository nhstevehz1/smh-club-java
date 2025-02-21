CREATE TABLE auth.authorizations (
    id                            varchar(50) NOT NULL,
    registered_client_id          varchar(50) NOT NULL,
    principal_name                varchar(50) NOT NULL,
    authorization_grant_type      varchar(30) NOT NULL,
    state                         varchar(500)  DEFAULT NULL,
    attributes                    varchar(4000) DEFAULT NULL,

    CONSTRAINT pk_authorization
        PRIMARY KEY (id)
);

CREATE TABLE auth.authorization_scopes_set (
    auth_id varchar(50)   NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_authorization_scopes_set__authorizations
       FOREIGN KEY (auth_id) REFERENCES auth.authorizations (id)
);


CREATE TABLE auth.authorization_token (
    id varchar(50) NOT NULL,
    auth_id varchar(50),
    token_type varchar(30) NOT NULL,
    token_value varchar(1000) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at TIMESTAMP DEFAULT NULL,
    metadata varchar(1000) NOT NULL,
    claims varchar(1000) DEFAULT NULL,

    CONSTRAINT pk_authorization_token
        PRIMARY KEY (id),
    CONSTRAINT fk_authorization_token__authorizations
        FOREIGN KEY (auth_id) REFERENCES auth.authorizations (id)
);


CREATE TABLE auth.token_scopes_set (
    auth_id varchar(50)   NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_token_scopes_set__authorization_token
           FOREIGN KEY (auth_id) REFERENCES auth.authorization_token(id)
);
