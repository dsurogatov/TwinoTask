INSERT INTO person (id, firstname, surname) VALUES
	(1, 'firstName', 'surName'),
	(2, 'black', 'person');
	
INSERT INTO country (id, code) VALUES (3, 'ru');

INSERT INTO blacklist (id, person_id) VALUES (4, 2);

INSERT INTO loan (id, amount, term, status, person_id, country_id, created) VALUES
	(5, 123.45, 'term', 'APPROVED', 1, 3, NOW()),
	(6, 5000, 'term1', 'APPROVED', 1, 3, NOW());
  
ALTER SEQUENCE hibernate_sequence RESTART WITH 100;