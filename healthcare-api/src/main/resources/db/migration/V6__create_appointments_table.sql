CREATE TABLE appointments
(
    id                  BIGSERIAL PRIMARY KEY,
    appointment_date    DATE         NOT NULL,
    appointment_time    TIME         NOT NULL,
    status              VARCHAR(50)  NOT NULL,
    visit_reason        VARCHAR(500),
    cancellation_reason VARCHAR(500),
    rescheduled         BOOLEAN               DEFAULT FALSE,
    appointment_number  VARCHAR(100) NOT NULL UNIQUE,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version             BIGINT       NOT NULL DEFAULT 0,
    patient_id          BIGINT       NOT NULL,
    doctor_id           BIGINT       NOT NULL,
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patients (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctors (id)
            ON DELETE CASCADE
);