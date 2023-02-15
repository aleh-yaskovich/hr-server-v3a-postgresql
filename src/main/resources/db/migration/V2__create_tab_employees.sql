CREATE TABLE employees (
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	email VARCHAR(100) NOT NULL UNIQUE,
	department VARCHAR(255) NOT NULL REFERENCES departments(title) ON DELETE CASCADE,
	position VARCHAR(100) NOT NULL,
	salary numeric NOT NULL CHECK (salary > 0),
	hiring DATE,
	summary TEXT
);