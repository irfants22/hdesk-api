INSERT INTO roles (name)
VALUES ('ADMIN'),
    ('STAFF'),
    ('USER') ON CONFLICT (name) DO NOTHING;