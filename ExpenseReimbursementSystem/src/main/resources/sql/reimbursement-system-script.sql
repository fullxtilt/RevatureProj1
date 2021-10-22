
--DROPS
DROP VIEW ers_complete_user_view;
DROP VIEW ers_complete_reimbursement_view;

DROP TABLE ers_reimbursement;
DROP TABLE ers_users;
DROP TABLE ers_user_roles;
DROP TABLE ers_reimbursement_type;
DROP TABLE ers_reimbursement_status;


--CREATE TABLEs
-----------------------------------
CREATE TABLE ers_reimbursement_status(
	reimb_status_id SERIAL PRIMARY KEY
	, reimb_status VARCHAR(10) NOT NULL
);

CREATE TABLE ers_reimbursement_type(
	reimb_type_id SERIAL PRIMARY KEY
	, reimb_type VARCHAR(10) NOT NULL
);

CREATE TABLE ers_user_roles(
	ers_user_role_id SERIAL PRIMARY KEY
	, user_role VARCHAR(20) NOT NULL 
);

CREATE TABLE ers_users(
	ers_users_id SERIAL PRIMARY KEY
	, ers_username VARCHAR(50) UNIQUE NOT NULL 
	, ers_password VARCHAR(50) NOT NULL 
	, user_first_name VARCHAR(100) NOT NULL 
	, user_last_name VARCHAR(100) NOT NULL 
	, user_email VARCHAR(150) UNIQUE NOT NULL
	, user_role_id INTEGER NOT NULL 
	, FOREIGN KEY (user_role_id) REFERENCES ers_user_roles (ers_user_role_id)
);


CREATE TABLE ers_reimbursement(
	reimb_id SERIAL PRIMARY KEY 
	, reimb_amount NUMERIC NOT NULL --change this to numeric
	, reimb_submitted TIMESTAMP NOT NULL 
	, reimb_resolved TIMESTAMP
	, reimb_description VARCHAR(250)
	, reimb_author INTEGER NOT NULL 
	, reimb_resolver INTEGER
	, reimb_status_id INTEGER NOT NULL 
	, reimb_type_id INTEGER NOT NULL 
	, FOREIGN KEY (reimb_author) REFERENCES ers_users (ers_users_id)
	, FOREIGN KEY (reimb_resolver) REFERENCES ers_users (ers_users_id)
	, FOREIGN KEY (reimb_status_id) REFERENCES ers_reimbursement_status (reimb_status_id)
	, FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type (reimb_type_id)
);

--Index setup
------------------------------
CREATE UNIQUE INDEX ers_users_UNv1 ON ers_users (ers_username, user_email);

--Lookup tables setup
-----------------------------------------------
--Reimbursement status
INSERT INTO ers_reimbursement_status (reimb_status) VALUES ('PENDING');
INSERT INTO ers_reimbursement_status (reimb_status) VALUES ('APPROVED');
INSERT INTO ers_reimbursement_status (reimb_status) VALUES ('DENIED');

--Reimbursement types
INSERT INTO ers_reimbursement_type (reimb_type) VALUES ('LODGING');
INSERT INTO ers_reimbursement_type (reimb_type) VALUES ('TRAVEL');
INSERT INTO ers_reimbursement_type (reimb_type) VALUES ('FOOD');
INSERT INTO ers_reimbursement_type (reimb_type) VALUES ('OTHER');

--User roles
INSERT INTO ers_user_roles (user_role) VALUES ('EMPLOYEE');
INSERT INTO ers_user_roles (user_role) VALUES ('FINANCE MANAGER');

--Example setup
-------------------------------------------------------------------

--Employees
INSERT INTO ers_users(
	ers_username, ers_password
	, user_first_name, user_last_name
	, user_email, user_role_id) 
VALUES (
	'hunter1', 'pass'
	, 'Wyverian', 'Seeker'
	, 'Wyverian.Seeker@company.com', 1);

INSERT INTO ers_users(
	ers_username, ers_password
	, user_first_name, user_last_name
	, user_email, user_role_id) 
VALUES (
	'hunter2', 'pass'
	, 'Meowscular', 'Chef'
	, 'Meowscular.Chef@company.com', 1);

INSERT INTO ers_users(
	ers_username, ers_password
	, user_first_name, user_last_name
	, user_email, user_role_id) 
VALUES (
	'admin1', 'pass'
	, 'Commander', 'Smith'
	, 'Commander.Smith@company.com', 2);

--Reimbursements
INSERT INTO ers_reimbursement(
	reimb_amount
	, reimb_submitted
	, reimb_description
	, reimb_author
	, reimb_status_id
	, reimb_type_id) 
VALUES (
	100
	, '2021-10-6 00:00:00'
	, 'Cost of camping materials for my last hunt.'
	, 1
	, 1
	, 1
);
INSERT INTO ers_reimbursement(
	reimb_amount
	, reimb_submitted
	, reimb_description
	, reimb_author
	, reimb_status_id
	, reimb_type_id) 
VALUES (
	35.25
	, '2021-10-11 00:00:00'
	, 'Handler insisted I splurge on food buffs before my next quest. She said it was covered.'
	, 1
	, 1
	, 3
);
INSERT INTO ers_reimbursement(
	reimb_amount
	, reimb_submitted
	, reimb_description
	, reimb_author
	, reimb_status_id
	, reimb_type_id) 
VALUES (
	300
	, '2021-10-11 00:00:00'
	, 'Need more ingredients, meow.'
	, 2
	, 1
	, 3
);


--Views
---------------------------------------------------

--Complete user view
CREATE VIEW ers_complete_user_view AS
SELECT eu.ers_users_id
, eu.ers_username, eu.ers_password
, eu.user_first_name, eu.user_last_name 
, eu.user_email
, eur.ers_user_role_id
, eur.user_role 
FROM ers_users eu
INNER JOIN ers_user_roles eur 
ON eu.user_role_id = eur.ers_user_role_id;

--Complete reimbursement view
CREATE VIEW ers_complete_reimbursement_view AS
SELECT er.reimb_id
, er.reimb_amount , er.reimb_submitted
, er.reimb_resolved, er.reimb_description
, er.reimb_author AS reimb_author_id, eu_auth.ers_username AS ers_author_username
, eu_auth.user_first_name AS ers_author_firstname, eu_auth.user_last_name AS ers_author_lastname
, er.reimb_resolver AS reimb_resolver_id, eu_res.ers_username AS ers_resolver_username
, eu_res.user_first_name AS ers_resolver_firstname, eu_res.user_last_name AS ers_resolver_lastname
, ers.reimb_status_id , ers.reimb_status
, ert.reimb_type_id, ert.reimb_type 
FROM ers_reimbursement er 
INNER JOIN ers_reimbursement_status ers 
ON er.reimb_status_id = ers.reimb_status_id
INNER JOIN ers_reimbursement_type ert 
ON er.reimb_type_id = ert.reimb_type_id
INNER JOIN ers_users eu_auth
ON er.reimb_author = eu_auth.ers_users_id
LEFT JOIN ers_users eu_res
ON er.reimb_resolver = eu_res.ers_users_id 
ORDER BY er.reimb_id;

--Selects
--SELECT * FROM ers_complete_user_view;
SELECT * FROM ers_complete_reimbursement_view;
--
--SELECT * FROM ers_reimbursement;
--SELECT * FROM ers_users;
--SELECT * FROM ers_reimbursement_status;
--SELECT * FROM ers_reimbursement_type;
--SELECT * FROM ers_user_roles;





