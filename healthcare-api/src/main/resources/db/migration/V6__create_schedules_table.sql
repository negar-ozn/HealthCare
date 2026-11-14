CREATE TABLE schedules
(
    id         BIGSERIAL PRIMARY KEY,
    weekday    VARCHAR(50) NOT NULL,
    date       DATE        NOT NULL,
    start_time TIME        NOT NULL,
    end_time   TIME        NOT NULL,
    status     VARCHAR(50),
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    BIGINT      NOT NULL DEFAULT 0,
    doctor_id  BIGINT      NOT NULL,
    CONSTRAINT fk_schedule_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctors (id)
            ON DELETE CASCADE
);