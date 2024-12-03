CREATE SCHEMA IF NOT EXISTS member_mgmt;

CREATE TABLE member_mgmt.member (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_number int NOT NULL,
	first_name varchar(40) NOT NULL,
    middle_name varchar(40),
    last_name varchar(40) NOT NULL,
	suffix varchar(10),
	joined_date DATE NOT NULL,
	birth_date DATE NOT NULL,
	active boolean DEFAULT FALSE,

	CONSTRAINT pk_member PRIMARY KEY (id),
	CONSTRAINT u_mem_num__members UNIQUE (member_number)
);

CREATE TABLE member_mgmt.address (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	address1 varchar(100) NOT NULL,
	address2 varchar(100),
	city varchar(30) NOT NULL,
	state varchar(30) NOT NULL,
	zip varchar(10) NOT NULL,
	address_type smallint NOT NULL,

	CONSTRAINT pk_address PRIMARY KEY (id),
	CONSTRAINT fk_address_members FOREIGN KEY (member_id) REFERENCES member_mgmt.member (id)

);

CREATE TABLE member_mgmt.email (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	email varchar(25) NOT NULL,
	email_type smallint NOT NULL,

	CONSTRAINT pk_email PRIMARY KEY (id),
	CONSTRAINT fk_email_members FOREIGN KEY (member_id) REFERENCES member_mgmt.member (id)
);

CREATE TABLE member_mgmt.renewal (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	renewal_date DATE NOT NULL,
	renewal_year char(4) NOT NULL,

	CONSTRAINT pk_renewals PRIMARY KEY (id),
	CONSTRAINT fk_renewals_members FOREIGN KEY (member_id) REFERENCES member_mgmt.member (id)
);

CREATE TABLE member_mgmt.phone (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	phone_number varchar(10) NOT NULL,
	phone_type smallint NOT NULL,

	CONSTRAINT pk_phone PRIMARY KEY(id),
	CONSTRAINT fk_phone_members FOREIGN KEY (member_id) REFERENCES member_mgmt.member (id)
);
