CREATE TABLE users
(

    id                    BIGSERIAL PRIMARY KEY,
    username              VARCHAR(50)  NOT NULL UNIQUE,
    password              VARCHAR(100) NOT NULL,
    email                 VARCHAR(100) NOT NULL UNIQUE,
    is_enabled            BOOLEAN      NOT NULL DEFAULT TRUE,
    account_locked        BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts INTEGER      NOT NULL DEFAULT 0,
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lock_time             TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    version               BIGINT       NOT NULL DEFAULT 0
);