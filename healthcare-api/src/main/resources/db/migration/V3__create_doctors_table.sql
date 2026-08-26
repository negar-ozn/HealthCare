CREATE TABLE doctors
(
    id                    BIGSERIAL PRIMARY KEY,
    specialization        VARCHAR(255) NOT NULL,
    licence_number        BIGINT       NOT NULL UNIQUE,
    availability_schedule BOOLEAN      NOT NULL,
    status                VARCHAR(50),
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version               BIGINT       NOT NULL DEFAULT 0,
    department_id         BIGINT       NOT NULL,
    personnel_id          BIGINT       NOT NULL,
    CONSTRAINT fk_doctor_department
        FOREIGN KEY (department_id)
            REFERENCES departments (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_doctor_personnel
        FOREIGN KEY (personnel_id)
            REFERENCES personnel (id)
            ON DELETE CASCADE
);