CREATE TYPE diaryentrytype AS ENUM ('GLUCOSE_ENTRY', 'INSULIN_ENTRY', 'CARBS_ENTRY', 'MEDICATION_ENTRY');
CREATE TYPE glucose_unit AS ENUM ('MILLIMOLES_PER_LITER', 'MILLIGRAMS_PER_DECILITER');
CREATE TYPE carbs_unit AS ENUM ('GRAMS', 'BREAD_UNITS_10', 'BREAD_UNITS_12', 'BREAD_UNITS_15');
CREATE TYPE insulintype AS ENUM ('LONG', 'SHORT_CARBS', 'SHORT_CORRECTION', 'SHORT');
CREATE TYPE measurementtype AS ENUM ('BEFORE_MEAL', 'AFTER_MEAL');
CREATE TYPE role AS ENUM ('ROLE_PATIENT', 'ROLE_DOCTOR', 'ROLE_ADMIN', 'ROLE_SUPERUSER');

CREATE TABLE Service_User (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username varchar(255) UNIQUE NOT NULL,
    password varchar NOT NULL,
    first_name varchar(255) NOT NULL,
    middle_name varchar(255),
    last_name varchar(255) NOT NULL,
    email varchar(255),
    birth_date date
);

CREATE TABLE User_Role (
    user_id int REFERENCES service_user(id) ON DELETE CASCADE,
    role role,
    PRIMARY KEY (user_id, role)
);

CREATE TABLE Patient_Profile (
    id int PRIMARY KEY REFERENCES service_user(id) ON DELETE CASCADE,
    glucose_unit glucose_unit NOT NULL DEFAULT 'MILLIMOLES_PER_LITER',
    carbs_unit carbs_unit NOT NULL DEFAULT 'GRAMS',
    diabetes_type smallint NOT NULL CHECK (diabetes_type IN (1, 2)) DEFAULT 1,
    hyper_glucose real NOT NULL CHECK (hyper_glucose >= 0.5 AND hyper_glucose <= 40 AND hyper_glucose >= high_glucose)
                             DEFAULT 15,
    high_glucose real NOT NULL CHECK (high_glucose >= 0.5 AND high_glucose <= 40 AND high_glucose >= low_glucose)
                             DEFAULT 8,
    low_glucose real NOT NULL CHECK (low_glucose >= 0.5 AND low_glucose <= 40 AND low_glucose >= hypo_glucose)
                             DEFAULT 4,
    hypo_glucose real NOT NULL CHECK (hypo_glucose >= 0.5 AND hypo_glucose <= 40) DEFAULT 2
);

CREATE TABLE Glucose_Entry (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    value real NOT NULL CHECK (value >= 0.5 AND value <= 40),
    type diaryentrytype NOT NULL CHECK (type = 'GLUCOSE_ENTRY') DEFAULT 'GLUCOSE_ENTRY',
    commited_at timestamptz(0) NOT NULL,
    measurement_type measurementtype,
    notes varchar(500),
    PRIMARY KEY (profile_id, commited_at, type)
);

CREATE TABLE Insulin_Entry (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    value real NOT NULL CHECK (value >= 1 AND value <= 100),
    type diaryentrytype NOT NULL CHECK (type = 'INSULIN_ENTRY') DEFAULT 'INSULIN_ENTRY',
    commited_at timestamptz(0) NOT NULL,
    insulin_type insulintype NOT NULL,
    notes varchar(500),
    PRIMARY KEY (profile_id, commited_at, type)
);

CREATE TABLE Medication_Entry (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    name varchar(200) NOT NULL,
    value real NOT NULL CHECK (value >= 0.1 AND value <= 1000),
    type diaryentrytype NOT NULL CHECK (type = 'MEDICATION_ENTRY') DEFAULT 'MEDICATION_ENTRY',
    commited_at timestamptz(0) NOT NULL,
    notes varchar(500),
    PRIMARY KEY (profile_id, commited_at, type)
);

CREATE TABLE Carbs_Entry (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    value real NOT NULL CHECK (value >= 0.1 AND value <= 300),
    type diaryentrytype NOT NULL CHECK (type = 'CARBS_ENTRY') DEFAULT 'CARBS_ENTRY',
    commited_at timestamptz(0) NOT NULL,
    notes varchar(500),
    PRIMARY KEY (profile_id, commited_at, type)
);

CREATE TABLE Doctor_Profile (
    id int PRIMARY KEY REFERENCES Service_User(id) ON DELETE CASCADE
);

CREATE TABLE Patient_Doctor (
    doctor_profile_id int REFERENCES Doctor_Profile(id) ON DELETE CASCADE,
    patient_profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    PRIMARY KEY (doctor_profile_id, patient_profile_id)
);

CREATE TABLE Insulin_Profile (
    patient_profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    default_icr real NOT NULL CHECK (default_icr >= 2 AND default_icr <= 100),
    default_isf real NOT NULL CHECK (default_isf >= 0.2 AND default_isf <= 55.5),
    dia int NOT NULL CHECK (dia >= 2 AND dia <= 9),
    PRIMARY KEY (patient_profile_id)
);

CREATE TABLE Insulin_To_Carb_Ratio (
    insulin_profile_id int REFERENCES Insulin_Profile(patient_profile_id) ON DELETE CASCADE,
    time_of_day time(0) NOT NULL,
    icr real NOT NULL CHECK (icr >= 2 AND icr <= 100),
    PRIMARY KEY (insulin_profile_id, time_of_day)
);

CREATE TABLE Insulin_Sensitivity_Factor (
    insulin_profile_id int REFERENCES Insulin_Profile(patient_profile_id) ON DELETE CASCADE,
    time_of_day time(0) NOT NULL,
    isf real NOT NULL CHECK (isf >= 0.2 AND isf <= 55.5),
    PRIMARY KEY (insulin_profile_id, time_of_day)
);

CREATE TABLE Patient_Meal (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    name varchar(200) NOT NULL,
    carbs_per_100_grams real NOT NULL CHECK (carbs_per_100_grams >= 0 AND carbs_per_100_grams <= 300),
    PRIMARY KEY (profile_id, name)
);

CREATE TABLE Patient_Medication (
    profile_id int REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    name varchar(200) NOT NULL,
    milligrams_in_portion real NOT NULL CHECK (milligrams_in_portion >= 0 AND milligrams_in_portion <= 1000),
    default_portions int NOT NULL CHECK (default_portions >= 1 AND default_portions <= 20) DEFAULT 1,
    PRIMARY KEY (profile_id, name)
);

CREATE TABLE Integration_Profile (
    patient_profile_id int PRIMARY KEY REFERENCES Patient_Profile(id) ON DELETE CASCADE,
    is_nightscout_enabled bool NOT NULL DEFAULT false,
    nightscout_api_secret varchar CHECK ((nightscout_api_secret IS NOT NULL AND length(nightscout_api_secret) >= 12)
        OR is_nightscout_enabled = false)
);
