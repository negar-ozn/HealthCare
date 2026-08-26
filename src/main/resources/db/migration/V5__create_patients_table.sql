CREATE TABLE patients
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    birthdate      DATE         NOT NULL,
    gender         VARCHAR(50)  NOT NULL,
    national_id    BIGINT       NOT NULL UNIQUE,
    phone          VARCHAR(20)  NOT NULL,
    email          VARCHAR(255) UNIQUE,
    address        VARCHAR(500) NOT NULL,
    arrival        BOOLEAN,
    status         VARCHAR(50),
    patient_number VARCHAR(255) NOT NULL UNIQUE,
    version        BIGINT       NOT NULL DEFAULT 0,
    insurance_id   BIGINT       NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_patient_insurance
        FOREIGN KEY (insurance_id)
            REFERENCES insurances (id)
            ON DELETE CASCADE
);