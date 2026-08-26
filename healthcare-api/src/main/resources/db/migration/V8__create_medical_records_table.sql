CREATE TABLE medical_records
(
    id                   BIGSERIAL PRIMARY KEY,
    visit_date           DATE         NOT NULL,
    symptoms             VARCHAR(500) NOT NULL,
    notes                VARCHAR(500) NOT NULL,
    prescriptions        VARCHAR(500) NOT NULL,
    prescriptions_number VARCHAR(500) NOT NULL UNIQUE,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version              BIGINT       NOT NULL DEFAULT 0,
    patient_id           BIGINT       NOT NULL,
    doctor_id            BIGINT       NOT NULL,
    CONSTRAINT fk_medical_record_patient
        FOREIGN KEY (patient_id)
            REFERENCES patients (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_medical_record_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctors (id)
            ON DELETE CASCADE
);