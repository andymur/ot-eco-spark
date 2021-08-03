DROP DATABASE IF EXISTS otus;
CREATE DATABASE otus;

CREATE TABLE pickup_by_hour (
    hour_of_day           INT,
    total_trips           INT,
    min_distance          DOUBLE PRECISION,
    max_distance          DOUBLE PRECISION,
    avg_distance          DOUBLE PRECISION,
    min_paycheck          DOUBLE PRECISION,
    max_paycheck          DOUBLE PRECISION,
    avg_paycheck          DOUBLE PRECISION
);

