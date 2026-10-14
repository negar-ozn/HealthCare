CREATE TABLE personnel
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(255) NOT NULL,
    last_name      VARCHAR(255) NOT NULL,
    user_id        BIGINT    NOT NULL,
    birthdate      DATE         NOT NULL,
    gender         VARCHAR(50)  NOT NULL,
    national_id    BIGINT       NOT NULL UNIQUE,
    phone          VARCHAR(20)  NOT NULL,
    email          VARCHAR(255)  NOT NULL UNIQUE,
    address        VARCHAR(500) NOT NULL,
    role           VARCHAR(50)  NOT NULL,
    personnel_code VARCHAR(100) NOT NULL,
    version        BIGINT       NOT NULL DEFAULT 0,
    department_id  BIGINT       NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_department
        FOREIGN KEY (department_id)
            REFERENCES departments (id)
            ON DELETE CASCADE
);