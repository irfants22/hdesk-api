INSERT INTO priorities (name, level)
VALUES ('LOW', 1),
    ('MEDIUM', 2),
    ('HIGH', 3),
    ('CRITICAL', 4) ON CONFLICT (name) DO NOTHING;