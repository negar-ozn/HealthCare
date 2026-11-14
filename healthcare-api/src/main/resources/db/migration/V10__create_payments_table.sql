CREATE TABLE payments
(
    id           BIGSERIAL PRIMARY KEY,
    amount       DECIMAL     NOT NULL,
    method       VARCHAR(50) NOT NULL,
    payment_date DATE        NOT NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      BIGINT      NOT NULL DEFAULT 0,
    invoice_id   BIGINT      NOT NULL,
    CONSTRAINT fk_payment_invoice
        FOREIGN KEY (invoice_id)
            REFERENCES invoices (id)
            ON DELETE CASCADE
);