CREATE TABLE clubmgmt.members (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_number int NOT NULL,
	lifetime boolean DEFAULt FALSE,
	first_name varchar(40) NOT NULL,
    middle_name varchar(40),
    last_name varchar(40) NOT NULL,
	suffix varchar(10),
	joined_year varchar(4),
	birth_date DATE,
	active boolean DEFAULT FALSE,

	CONSTRAINT pk_members PRIMARY KEY (id),
	CONSTRAINT u_mem_num__members UNIQUE (member_number)
);

CREATE TABLE clubmgmt.address (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	address1 varchar(100) NOT NULL,
	address2 varchar(100),
	city varchar(30) NOT NULL,
	state varchar(30) NOT NULL,
	zip varchar(15) NOT NULL,
	address_type smallint NOT NULL,

	CONSTRAINT pk_address PRIMARY KEY (id),
	CONSTRAINT fk_address_members FOREIGN KEY (member_id) REFERENCES clubmgmt.members (id)

);

CREATE TABLE clubmgmt.email (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	email varchar(100),
	email_type smallint NOT NULL,

	CONSTRAINT pk_email PRIMARY KEY (id),
	CONSTRAINT fk_email_members FOREIGN KEY (member_id) REFERENCES clubmgmt.members (id)
);

CREATE TABLE clubmgmt.renewals (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	renewal_date DATE,
	renewal_year int NOT NULL,

	CONSTRAINT pk_renewals PRIMARY KEY (id),
	CONSTRAINT fk_renewals_members FOREIGN KEY (member_id) REFERENCES clubmgmt.members (id)
);

CREATE TABLE clubmgmt.phone (
	id int NOT NULL GENERATED ALWAYS AS IDENTITY,
	member_id int NOT NULL,
	phone varchar(10),
	phone_type integer,

	CONSTRAINT pk_phone PRIMARY KEY(id),
	CONSTRAINT fk_phone_members FOREIGN KEY (member_id) REFERENCES clubmgmt.members (id)
);