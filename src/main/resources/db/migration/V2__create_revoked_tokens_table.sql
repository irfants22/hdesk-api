CREATE TABLE revoked_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token_id VARCHAR(64) NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_revoked_tokens_token_id UNIQUE (token_id)
);
CREATE INDEX idx_revoked_tokens_expired_at ON revoked_tokens (expired_at);