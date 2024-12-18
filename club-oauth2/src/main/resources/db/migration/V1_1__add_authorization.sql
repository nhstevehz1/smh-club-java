CREATE TABLE auth.authorization (
    id                            varchar(50) NOT NULL,
    registered_client_id          varchar(50) NOT NULL,
    principal_name                varchar(30) NOT NULL,
    authorization_grant_type      varchar(30) NOT NULL,
    state                         varchar(500)  DEFAULT NULL,
    attributes                    varchar(4000) DEFAULT NULL,

    CONSTRAINT pk_auth PRIMARY KEY (id)
);

CREATE TABLE auth.authorization_scopes_set (
    auth_id varchar(50)   NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_authorization_scopes_set__authorization
       FOREIGN KEY (auth_id) REFERENCES auth.authorization (id)
);


CREATE TABLE auth.authorization_token (
    id varchar(50) NOT NULL,
    auth_id varchar(50),
    token_type varchar(30) NOT NULL,
    token_value varchar(500) NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expires_at TIMESTAMP DEFAULT NULL,
    metadata varchar(1000) NOT NULL,
    claims varchar(1000) DEFAULT NULL,

    CONSTRAINT pk_authorization_token PRIMARY KEY (id),
    CONSTRAINT unique_token_value UNIQUE (token_value),
    CONSTRAINT unique_auth_id__token_type UNIQUE (auth_id, token_type),
    CONSTRAINT fk_authorization_token__authorization
       FOREIGN KEY (auth_id) REFERENCES auth.authorization (id)
);


CREATE TABLE auth.token_scopes_set (
    auth_id varchar(50)   NOT NULL,
    token_type varchar(30) NOT NULL,
    scope varchar(30)     NOT NULL,

    CONSTRAINT fk_token_scopes_set__authorization_token
        FOREIGN KEY (auth_id, token_type)
            REFERENCES auth.authorization_token (auth_id, token_type)
);
