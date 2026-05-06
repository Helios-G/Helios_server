-- 테스트 유저 3명 (중복 방지)
INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저1', 'test1@test.com', '1234', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test1@test.com');

INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저2', 'test2@test.com', '1234', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test2@test.com');

INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저3', 'test3@test.com', '1234', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test3@test.com');