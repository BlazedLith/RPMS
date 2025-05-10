CREATE DATABASE IF NOT EXISTS rpms_db;
USE rpms_db;

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id VARCHAR(20) PRIMARY KEY,
    patient_id VARCHAR(20),
    date_time DATETIME,
    status VARCHAR(20),
    FOREIGN KEY (patient_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS feedback (
    feedback_id VARCHAR(20) PRIMARY KEY,
    doctor_id VARCHAR(20),
    patient_id VARCHAR(20),
    date DATETIME,
    comments TEXT,
    medications TEXT,
    FOREIGN KEY (doctor_id) REFERENCES users(user_id),
    FOREIGN KEY (patient_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS vitals (
    vital_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20),
    heart_rate DOUBLE,
    blood_pressure DOUBLE,
    temperature DOUBLE,
    oxygen_level DOUBLE,
    timestamp DATETIME,
    FOREIGN KEY (patient_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS communication_requests (
    request_id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(20),
    doctor_id VARCHAR(20),
    type VARCHAR(20),
    status VARCHAR(20),
    link VARCHAR(255),
    timestamp DATETIME,
    FOREIGN KEY (patient_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS patient_doctor (
    patient_id VARCHAR(20),
    doctor_id VARCHAR(20),
    PRIMARY KEY (patient_id, doctor_id),
    FOREIGN KEY (patient_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);

INSERT INTO users (user_id, name, email, password, role) VALUES
('admin001', 'Admin One', 'admin@nust.edu', 'admin123', 'admin'),
('doc001', 'Doctor1', 'wasiqamir3@gmail.com', 'docpass', 'doctor'),
('pat001', 'John Doe', 'john@example.com', 'pat123', 'patient');


INSERT INTO appointments (appointment_id, patient_id, date_time, status) VALUES
('a001', 'pat001', '2025-04-21 02:17:32', 'completed'),
('a002', 'pat001', '2025-04-24 02:17:32', 'completed'),
('a003', 'pat001', '2025-04-18 02:17:32', 'completed');

INSERT INTO communication_requests (request_id, patient_id, doctor_id, type, status, link, timestamp) VALUES
('3369b37c', 'pat001', 'doc001', 'video', 'rejected', '', '2025-04-21 21:49:44'),
('e157388b', 'pat001', 'doc001', 'chat', 'approved', 'meet', '2025-04-21 21:49:43'),
('e643ee5c', 'pat001', 'doc001', 'chat', 'rejected', '', '2025-04-21 21:49:46'),
('edd3e04e', 'pat001', 'doc001', 'video', 'approved', 'chat', '2025-04-21 21:49:39');

INSERT INTO feedback (feedback_id, doctor_id, patient_id, date, comments, medications) VALUES
('f001', 'doc001', 'pat001', '2025-04-16 02:17:32', 'Patient doing well. Maintain current medication.', 'Paracetamol, Vitamin C'),
('f002', 'doc001', 'pat001', '2025-04-18 02:17:32', 'Slight fever noted. Monitor temperature daily.', 'Ibuprofen');

INSERT INTO patient_doctor (patient_id, doctor_id) VALUES
('pat001', 'doc001');

INSERT INTO vitals (patient_id, heart_rate, blood_pressure, temperature, oxygen_level, timestamp) VALUES
('pat001', 75, 120, 98.6, 98, '2025-04-17 02:16:48'),
('pat001', 78, 118, 98.4, 97, '2025-04-18 02:16:48'),
('pat001', 80, 115, 99.1, 96, '2025-04-19 02:16:48');


ALTER TABLE patient_doctor
ADD CONSTRAINT fk_patient
FOREIGN KEY (patient_id) REFERENCES users(user_id)
ON DELETE CASCADE;

ALTER TABLE patient_doctor
ADD CONSTRAINT fk_doctor
FOREIGN KEY (doctor_id) REFERENCES users(user_id)
ON DELETE CASCADE;

update users set email = 'wasiqamir0@gmail.com' where user_id = 'pat001';
