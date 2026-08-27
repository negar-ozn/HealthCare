CREATE TABLE notifications
(
    id                  BIGSERIAL PRIMARY KEY,
    version             BIGINT       NOT NULL DEFAULT 0,
    recipient           VARCHAR(255) NOT NULL,
    subject             VARCHAR(255) NOT NULL,
    body                TEXT,
    type                VARCHAR(50)  NOT NULL,
    status              VARCHAR(20)  NOT NULL,
    notification_source VARCHAR(50)  NOT NULL,
    correlation_id      VARCHAR(50)  NOT NULL,
    created_at          TIMESTAMP    NOT NULL,
    sent_at             TIMESTAMP,
    error_message       TEXT
);