CREATE TABLE insurances
(
    id                  BIGSERIAL PRIMARY KEY,
    provider_name       VARCHAR(100) NOT NULL,
    coverage_percentage DECIMAL      NOT NULL,
    policy_number       BIGINT       Not Null,
    expiry_date         DATE         NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version             BIGINT       NOT NULL DEFAULT 0
);