-- Flyway migration: initial schema

CREATE TABLE patients (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL
);

CREATE TABLE units (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL
);

CREATE TABLE unit_supported_types (
  unit_id VARCHAR(50) NOT NULL,
  type VARCHAR(100) NOT NULL,
  CONSTRAINT fk_unit_supported_unit FOREIGN KEY (unit_id) REFERENCES units(id) ON DELETE CASCADE
);

CREATE TABLE slots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  date_time TIMESTAMP NOT NULL,
  professional_id VARCHAR(50),
  unit_id VARCHAR(50) NOT NULL,
  CONSTRAINT fk_slot_unit FOREIGN KEY (unit_id) REFERENCES units(id) ON DELETE CASCADE
);

CREATE TABLE appointments (
  id VARCHAR(50) NOT NULL PRIMARY KEY,
  patient_id VARCHAR(50) NOT NULL,
  unit_id VARCHAR(50) NOT NULL,
  professional_id VARCHAR(50),
  type VARCHAR(100) NOT NULL,
  date_time TIMESTAMP NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
  CONSTRAINT fk_appointment_unit FOREIGN KEY (unit_id) REFERENCES units(id)
);

