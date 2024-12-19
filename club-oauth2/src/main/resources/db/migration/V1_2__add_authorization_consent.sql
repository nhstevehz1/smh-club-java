CREATE TABLE auth.authorization_consent (
    registered_client_id varchar(50)  NOT NULL,
    principal_name       varchar(50)  NOT NULL,

    CONSTRAINT pk_auth_consent
        PRIMARY KEY (registered_client_id, principal_name)
);

CREATE TABLE auth.authorization_consent_authorities_set (
    registered_client_id varchar(50) NOT NULL,
    principal_name varchar(50) NOT NULL,
    authority varchar(100) NOT NULL,

    CONSTRAINT fk_reg_client_id__principal_name__authorization_consent
        FOREIGN KEY (registered_client_id, principal_name)
            REFERENCES auth.authorization_consent (registered_client_id, principal_name)
);
