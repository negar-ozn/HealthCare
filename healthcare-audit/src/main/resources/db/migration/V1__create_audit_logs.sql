CREATE TABLE audit_logs
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT,
    username          VARCHAR(100) NOT NULL,
    action            VARCHAR(50)  NOT NULL,
    entity_name       VARCHAR(100),
    email             VARCHAR(50),
    entity_id         BIGINT,
    timestamp         TIMESTAMPTZ  NOT NULL,
    ip_address        VARCHAR(45),
    status            VARCHAR(20)  NOT NULL,
    details           TEXT,
    verification_link VARCHAR(500) UNIQUE,
    version           BIGINT       NOT NULL DEFAULT 0
);

