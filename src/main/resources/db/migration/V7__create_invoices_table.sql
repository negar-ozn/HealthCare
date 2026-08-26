CREATE TABLE invoices
(
    id                       BIGSERIAL PRIMARY KEY,
    amount                   DECIMAL      NOT NULL,
    tax                      DECIMAL      NOT NULL,
    insurance_covered_amount DECIMAL      NOT NULL,
    patient_payable_amount   DECIMAL      NOT NULL,
    status                   VARCHAR(50),
    invoice_number           VARCHAR(100) NOT NULL UNIQUE,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                  BIGINT       NOT NULL DEFAULT 0,
    patient_id               BIGINT       NOT NULL,
    appointment_id           BIGINT       NOT NULL,
    CONSTRAINT fk_invoice_patient
        FOREIGN KEY (patient_id)
            REFERENCES patients (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_invoice_appointment
        FOREIGN KEY (appointment_id)
            REFERENCES appointments (id)
            ON DELETE CASCADE
);