INSERT INTO statuses (name, is_closed)
VALUES ('OPEN', false),
    ('IN PROGRESS', false),
    ('RESOLVED', false),
    ('CLOSED', true) ON CONFLICT (name) DO NOTHING;