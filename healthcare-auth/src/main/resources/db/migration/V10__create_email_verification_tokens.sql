CREATE TABLE email_verification_tokens
(

    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    CONSTRAINT fk_email_verification_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE

);