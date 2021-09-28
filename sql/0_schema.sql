CREATE TABLE tags (
	id SERIAL PRIMARY KEY,
	tag_value VARCHAR(50) UNIQUE NOT NULL,
	is_technology BOOLEAN,
	is_language BOOLEAN,
	checked BOOLEAN
);

CREATE TABLE lands (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) UNIQUE NOT NULL,
	iso_code VARCHAR(5) UNIQUE NOT NULL
);

CREATE TABLE cities (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50) UNIQUE NOT NULL,
	land_id INTEGER,
	FOREIGN KEY (land_id) REFERENCES lands (id)
);

CREATE TABLE posts (
	id SERIAL PRIMARY KEY,
	posted_at INTEGER,  -- post timestamp
	created_at TIMESTAMP, -- record creation timestamp
	city_id INTEGER,
	land_id INTEGER,
	min_salary DOUBLE PRECISION,
	max_salary DOUBLE PRECISION,
	tags INTEGER[],
	words TEXT,
	FOREIGN KEY (land_id) REFERENCES lands (id),
	FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE TABLE postaggregations (
	id SERIAL,
	year INTEGER,
	month INTEGER,
	city_id INTEGER,
	land_id INTEGER,
	tag_id INTEGER,
	count_agg INTEGER,
	avg_salary_agg DOUBLE PRECISION,
	FOREIGN KEY (land_id) REFERENCES lands (id),
	FOREIGN KEY (city_id) REFERENCES cities (id),
	FOREIGN KEY (tag_id) REFERENCES tags (id)
);
