-- 테스트 유저 3명 (중복 방지)
-- 비밀번호 '1234'의 BCrypt 해시값: $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMneWFJXumYe

-- 테스트 유저 1
INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저1', 'test1@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMneWFJXumYe', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test1@test.com');

-- 테스트 유저 2
INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저2', 'test2@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMneWFJXumYe', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test2@test.com');

-- 테스트 유저 3
INSERT INTO users (name, email, password, status, created_at)
SELECT '테스트유저3', 'test3@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMneWFJXumYe', 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'test3@test.com');