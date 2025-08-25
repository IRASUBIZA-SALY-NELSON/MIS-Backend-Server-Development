-- Sample data for RCA MIS Database
-- Run this script after the backend starts successfully to populate tables with sample data

-- Insert sample academic years
INSERT INTO academic_years (name, start_date, end_date, is_current, is_active, created_at, updated_at) 
VALUES 
('2024-2025', '2024-09-01', '2025-08-31', true, true, NOW(), NOW()),
('2023-2024', '2023-09-01', '2024-08-31', false, false, NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- Insert sample terms (using academic year IDs from above)
INSERT INTO terms (academic_year_id, name, start_date, end_date, is_current, is_active, created_at, updated_at) 
SELECT ay.id, 'Term 1', '2024-09-01', '2024-12-15', true, true, NOW(), NOW()
FROM academic_years ay WHERE ay.name = '2024-2025'
ON CONFLICT DO NOTHING;

INSERT INTO terms (academic_year_id, name, start_date, end_date, is_current, is_active, created_at, updated_at) 
SELECT ay.id, 'Term 2', '2025-01-15', '2025-04-15', false, true, NOW(), NOW()
FROM academic_years ay WHERE ay.name = '2024-2025'
ON CONFLICT DO NOTHING;

-- Insert sample classes
INSERT INTO classes (name, description, academic_year_id, is_active, created_at, updated_at) 
SELECT 'Year 1A', 'Year 1 Section A', ay.id, true, NOW(), NOW()
FROM academic_years ay WHERE ay.name = '2024-2025'
ON CONFLICT DO NOTHING;

INSERT INTO classes (name, description, academic_year_id, is_active, created_at, updated_at) 
SELECT 'Year 1B', 'Year 1 Section B', ay.id, true, NOW(), NOW()
FROM academic_years ay WHERE ay.name = '2024-2025'
ON CONFLICT DO NOTHING;

-- Insert sample subjects
INSERT INTO subjects (name, code, description, credits, is_active, created_at, updated_at) 
VALUES 
('Mathematics', 'MATH101', 'Basic Mathematics', 3, true, NOW(), NOW()),
('English Language', 'ENG101', 'English Language and Literature', 3, true, NOW(), NOW()),
('Computer Science', 'CS101', 'Introduction to Computer Science', 4, true, NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- Insert sample users (password is 'password' hashed with bcrypt)
INSERT INTO users (email, password_hash, status, created_at, updated_at) 
VALUES 
('admin@rca.ac.rw', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ACTIVE', NOW(), NOW()),
('teacher1@rca.ac.rw', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ACTIVE', NOW(), NOW()),
('student1@rca.ac.rw', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ACTIVE', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Insert sample user profiles
INSERT INTO user_profiles (user_id, first_name, last_name, phone, address, date_of_birth, gender, created_at, updated_at) 
SELECT u.id, 'System', 'Administrator', '+250788123456', 'Kigali, Rwanda', '1985-01-15', 'MALE', NOW(), NOW()
FROM users u WHERE u.email = 'admin@rca.ac.rw'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_profiles (user_id, first_name, last_name, phone, address, date_of_birth, gender, created_at, updated_at) 
SELECT u.id, 'John', 'Uwimana', '+250788234567', 'Kigali, Rwanda', '1980-05-20', 'MALE', NOW(), NOW()
FROM users u WHERE u.email = 'teacher1@rca.ac.rw'
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_profiles (user_id, first_name, last_name, phone, address, date_of_birth, gender, created_at, updated_at) 
SELECT u.id, 'Alice', 'Mukamana', '+250788345678', 'Kigali, Rwanda', '2005-03-10', 'FEMALE', NOW(), NOW()
FROM users u WHERE u.email = 'student1@rca.ac.rw'
ON CONFLICT (user_id) DO NOTHING;

-- Insert user roles (assuming roles table has been populated)
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, 1 FROM users u WHERE u.email = 'admin@rca.ac.rw'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, 2 FROM users u WHERE u.email = 'teacher1@rca.ac.rw'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, 3 FROM users u WHERE u.email = 'student1@rca.ac.rw'
ON CONFLICT DO NOTHING;

-- Insert sample announcements
INSERT INTO announcements (title, content, author_id, target_audience, priority, is_active, publish_date, expiry_date, created_at, updated_at) 
SELECT 'Welcome to New Academic Year', 'Welcome to the 2024-2025 academic year. Classes begin on September 1st.', u.id, 'ALL', 'HIGH', true, NOW(), '2024-12-31', NOW(), NOW()
FROM users u WHERE u.email = 'admin@rca.ac.rw'
ON CONFLICT DO NOTHING;

INSERT INTO announcements (title, content, author_id, target_audience, priority, is_active, publish_date, expiry_date, created_at, updated_at) 
SELECT 'Parent-Teacher Meeting', 'Parent-teacher meetings scheduled for next week. Please check your schedule.', u.id, 'PARENTS', 'MEDIUM', true, NOW(), '2024-10-15', NOW(), NOW()
FROM users u WHERE u.email = 'teacher1@rca.ac.rw'
ON CONFLICT DO NOTHING;
