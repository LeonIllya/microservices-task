CREATE TABLE processing_logs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    input_text VARCHAR(500) NOT NULL,
    output_text VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_processing_logs_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
