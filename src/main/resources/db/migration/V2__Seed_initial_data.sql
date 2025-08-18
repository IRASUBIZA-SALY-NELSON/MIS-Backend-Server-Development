-- ===========================================
-- RCA MIS - Initial Data Seeding
-- Version: 1.0.0
-- Description: Seeds the database with initial data
-- ===========================================

-- ===========================================
-- Insert Default Roles
-- ===========================================

INSERT INTO roles (id, name, description, permissions) VALUES
(uuid_generate_v4(), 'SUPER_ADMIN', 'Super Administrator with full system access', '["*"]'::jsonb),
(uuid_generate_v4(), 'ADMIN', 'Administrator with management access', '["users:read", "users:write", "students:read", "students:write", "teachers:read", "teachers:write", "classes:read", "classes:write", "subjects:read", "subjects:write", "assessments:read", "assessments:write", "reports:read", "reports:write"]'::jsonb),
(uuid_generate_v4(), 'TEACHER', 'Teacher with class and student management access', '["students:read", "attendance:read", "attendance:write", "assessments:read", "assessments:write", "marks:read", "marks:write", "timetable:read"]'::jsonb),
(uuid_generate_v4(), 'STUDENT', 'Student with limited access to their own data', '["profile:read", "profile:write", "marks:read", "attendance:read", "timetable:read", "projects:read", "projects:write"]'::jsonb),
(uuid_generate_v4(), 'PARENT', 'Parent with access to their child''s information', '["child:read", "attendance:read", "marks:read", "timetable:read", "reports:read"]'::jsonb),
(uuid_generate_v4(), 'GUARDIAN', 'Guardian with access to their ward''s information', '["ward:read", "attendance:read", "marks:read", "timetable:read", "reports:read"]'::jsonb);

-- ===========================================
-- Insert Default Academic Years
-- ===========================================

INSERT INTO academic_years (id, name, start_date, end_date, is_active, status) VALUES
(
    uuid_generate_v4(),
    '2024-2025',
    '2024-09-01',
    '2025-06-30',
    true,
    'ACTIVE'
),
(
    uuid_generate_v4(),
    '2025-2026',
    '2025-09-01',
    '2026-06-30',
    false,
    'PLANNED'
);

-- ===========================================
-- Insert Default Terms
-- ===========================================

-- Get the active academic year ID
DO $$
DECLARE
    active_year_id UUID;
BEGIN
    SELECT id INTO active_year_id FROM academic_years WHERE is_active = true LIMIT 1;
    
    IF active_year_id IS NOT NULL THEN
        INSERT INTO terms (academic_year_id, name, start_date, end_date, is_active) VALUES
        (active_year_id, 'Term 1', '2024-09-01', '2024-12-20', true),
        (active_year_id, 'Term 2', '2025-01-06', '2025-04-04', false),
        (active_year_id, 'Term 3', '2025-04-21', '2025-06-30', false);
    END IF;
END $$;

-- ===========================================
-- Insert Default Subjects
-- ===========================================

INSERT INTO subjects (name, code, category, description, credits) VALUES
('Mathematics', 'MATH101', 'Core', 'Fundamental mathematics including algebra, geometry, and calculus', 4),
('English Language', 'ENG101', 'Core', 'English language and literature studies', 3),
('Computer Science', 'CS101', 'Core', 'Introduction to computer programming and software development', 4),
('Physics', 'PHY101', 'Science', 'Fundamental physics principles and applications', 4),
('Chemistry', 'CHEM101', 'Science', 'Chemical principles and laboratory techniques', 4),
('Biology', 'BIO101', 'Science', 'Biological systems and life processes', 4),
('History', 'HIST101', 'Humanities', 'World history and historical analysis', 3),
('Geography', 'GEO101', 'Humanities', 'Physical and human geography', 3),
('Economics', 'ECON101', 'Social Sciences', 'Economic principles and market systems', 3),
('Business Studies', 'BUS101', 'Social Sciences', 'Business fundamentals and entrepreneurship', 3),
('Art and Design', 'ART101', 'Creative Arts', 'Visual arts and design principles', 2),
('Physical Education', 'PE101', 'Physical', 'Physical fitness and sports activities', 2),
('French', 'FREN101', 'Languages', 'French language and culture', 3),
('Kinyarwanda', 'KIN101', 'Languages', 'Rwandan language and culture', 2);

-- ===========================================
-- Insert Default Classes
-- ===========================================

-- Get the active academic year ID
DO $$
DECLARE
    active_year_id UUID;
BEGIN
    SELECT id INTO active_year_id FROM academic_years WHERE is_active = true LIMIT 1;
    
    IF active_year_id IS NOT NULL THEN
        INSERT INTO classes (name, level, academic_year_id, max_students) VALUES
        ('Form 1A', 'Form 1', active_year_id, 35),
        ('Form 1B', 'Form 1', active_year_id, 35),
        ('Form 2A', 'Form 2', active_year_id, 35),
        ('Form 2B', 'Form 2', active_year_id, 35),
        ('Form 3A', 'Form 3', active_year_id, 35),
        ('Form 3B', 'Form 3', active_year_id, 35),
        ('Form 4A', 'Form 4', active_year_id, 35),
        ('Form 4B', 'Form 4', active_year_id, 35),
        ('Form 5A', 'Form 5', active_year_id, 35),
        ('Form 5B', 'Form 5', active_year_id, 35),
        ('Form 6A', 'Form 6', active_year_id, 35),
        ('Form 6B', 'Form 6', active_year_id, 35);
    END IF;
END $$;

-- ===========================================
-- Insert Default Grade Scales
-- ===========================================

INSERT INTO grade_scales (name, min_percentage, max_percentage, grade, description) VALUES
('A+', 90.00, 100.00, 'A+', 'Excellent - Outstanding performance'),
('A', 80.00, 89.99, 'A', 'Very Good - Above average performance'),
('B+', 70.00, 79.99, 'B+', 'Good - Satisfactory performance'),
('B', 60.00, 69.99, 'B', 'Average - Acceptable performance'),
('C+', 50.00, 59.99, 'C+', 'Below Average - Needs improvement'),
('C', 40.00, 49.99, 'C', 'Poor - Below acceptable standard'),
('D', 30.00, 39.99, 'D', 'Very Poor - Significantly below standard'),
('F', 0.00, 29.99, 'F', 'Fail - Unacceptable performance');

-- ===========================================
-- Insert Default System Settings
-- ===========================================

INSERT INTO system_settings (setting_key, setting_value, description, category) VALUES
('school.name', 'Rwanda Coding Academy', 'Name of the educational institution', 'General'),
('school.address', 'Kigali, Rwanda', 'Address of the school', 'General'),
('school.phone', '+250 123 456 789', 'Contact phone number', 'General'),
('school.email', 'info@rca.ac.rw', 'Contact email address', 'General'),
('school.website', 'https://www.rca.ac.rw', 'School website URL', 'General'),
('academic.year.start', 'September', 'Month when academic year starts', 'Academic'),
('academic.year.end', 'June', 'Month when academic year ends', 'Academic'),
('assessment.passing.percentage', '40', 'Minimum percentage to pass assessments', 'Academic'),
('attendance.minimum.percentage', '80', 'Minimum attendance percentage required', 'Academic'),
('max.students.per.class', '35', 'Maximum number of students allowed per class', 'Academic'),
('file.upload.max.size', '10485760', 'Maximum file upload size in bytes (10MB)', 'System'),
('session.timeout.minutes', '30', 'User session timeout in minutes', 'Security'),
('password.min.length', '8', 'Minimum password length requirement', 'Security'),
('login.max.attempts', '5', 'Maximum login attempts before account lockout', 'Security'),
('email.notifications.enabled', 'true', 'Enable email notifications', 'Communication'),
('sms.notifications.enabled', 'false', 'Enable SMS notifications', 'Communication'),
('maintenance.mode', 'false', 'Enable maintenance mode', 'System'),
('backup.frequency.hours', '24', 'Database backup frequency in hours', 'System'),
('log.retention.days', '90', 'Number of days to retain log files', 'System');

-- ===========================================
-- Insert Sample Super Admin User
-- ===========================================

-- Create super admin user (password: Admin@123)
INSERT INTO users (id, email, password_hash, status) VALUES
(
    uuid_generate_v4(),
    'admin@rca.ac.rw',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz6K', -- Admin@123
    'ACTIVE'
);

-- Get the super admin user ID and role ID
DO $$
DECLARE
    admin_user_id UUID;
    super_admin_role_id UUID;
BEGIN
    SELECT id INTO admin_user_id FROM users WHERE email = 'admin@rca.ac.rw';
    SELECT id INTO super_admin_role_id FROM roles WHERE name = 'SUPER_ADMIN';
    
    IF admin_user_id IS NOT NULL AND super_admin_role_id IS NOT NULL THEN
        -- Assign super admin role
        INSERT INTO user_roles (user_id, role_id) VALUES (admin_user_id, super_admin_role_id);
        
        -- Create admin profile
        INSERT INTO user_profiles (user_id, first_name, last_name, phone, gender) VALUES
        (admin_user_id, 'System', 'Administrator', '+250 123 456 789', 'Other');
    END IF;
END $$;

-- ===========================================
-- Insert Sample Teacher User
-- ===========================================

-- Create sample teacher user (password: Teacher@123)
INSERT INTO users (id, email, password_hash, status) VALUES
(
    uuid_generate_v4(),
    'teacher@rca.ac.rw',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz6K', -- Teacher@123
    'ACTIVE'
);

-- Get the teacher user ID and role ID
DO $$
DECLARE
    teacher_user_id UUID;
    teacher_role_id UUID;
BEGIN
    SELECT id INTO teacher_user_id FROM users WHERE email = 'teacher@rca.ac.rw';
    SELECT id INTO teacher_role_id FROM roles WHERE name = 'TEACHER';
    
    IF teacher_user_id IS NOT NULL AND teacher_role_id IS NOT NULL THEN
        -- Assign teacher role
        INSERT INTO user_roles (user_id, role_id) VALUES (teacher_user_id, teacher_role_id);
        
        -- Create teacher profile
        INSERT INTO user_profiles (user_id, first_name, last_name, phone, gender) VALUES
        (teacher_user_id, 'John', 'Doe', '+250 987 654 321', 'Male');
        
        -- Create teacher record
        INSERT INTO teachers (user_id, employee_id, qualification, experience_years, specialization, hire_date) VALUES
        (teacher_user_id, 'EMP001', 'Master of Computer Science', 5, 'Computer Science', '2020-09-01');
    END IF;
END $$;

-- ===========================================
-- Insert Sample Student User
-- ===========================================

-- Create sample student user (password: Student@123)
INSERT INTO users (id, email, password_hash, status) VALUES
(
    uuid_generate_v4(),
    'student@rca.ac.rw',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz6K', -- Student@123
    'ACTIVE'
);

-- Get the student user ID and role ID
DO $$
DECLARE
    student_user_id UUID;
    student_role_id UUID;
    sample_class_id UUID;
BEGIN
    SELECT id INTO student_user_id FROM users WHERE email = 'student@rca.ac.rw';
    SELECT id INTO student_role_id FROM roles WHERE name = 'STUDENT';
    SELECT id INTO sample_class_id FROM classes LIMIT 1;
    
    IF student_user_id IS NOT NULL AND student_role_id IS NOT NULL THEN
        -- Assign student role
        INSERT INTO user_roles (user_id, role_id) VALUES (student_user_id, student_role_id);
        
        -- Create student profile
        INSERT INTO user_profiles (user_id, first_name, last_name, phone, gender, date_of_birth) VALUES
        (student_user_id, 'Jane', 'Smith', '+250 555 123 456', 'Female', '2006-03-15');
        
        -- Create student record
        INSERT INTO students (user_id, student_code, class_id, admission_date) VALUES
        (student_user_id, 'STU001', sample_class_id, '2024-09-01');
    END IF;
END $$;

-- ===========================================
-- Insert Sample Parent User
-- ===========================================

-- Create sample parent user (password: Parent@123)
INSERT INTO users (id, email, password_hash, status) VALUES
(
    uuid_generate_v4(),
    'parent@rca.ac.rw',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz6K', -- Parent@123
    'ACTIVE'
);

-- Get the parent user ID and role ID
DO $$
DECLARE
    parent_user_id UUID;
    parent_role_id UUID;
    student_user_id UUID;
BEGIN
    SELECT id INTO parent_user_id FROM users WHERE email = 'parent@rca.ac.rw';
    SELECT id INTO parent_role_id FROM roles WHERE name = 'PARENT';
    SELECT id INTO student_user_id FROM users WHERE email = 'student@rca.ac.rw';
    
    IF parent_user_id IS NOT NULL AND parent_role_id IS NOT NULL THEN
        -- Assign parent role
        INSERT INTO user_roles (user_id, role_id) VALUES (parent_user_id, parent_role_id);
        
        -- Create parent profile
        INSERT INTO user_profiles (user_id, first_name, last_name, phone, gender) VALUES
        (parent_user_id, 'Robert', 'Smith', '+250 777 888 999', 'Male');
        
        -- Create parent record
        INSERT INTO parents (user_id, relationship, occupation, employer, emergency_contact) VALUES
        (parent_user_id, 'Father', 'Engineer', 'Tech Corp', '+250 777 888 999');
        
        -- Link parent to student if both exist
        IF student_user_id IS NOT NULL THEN
            INSERT INTO student_parents (student_id, parent_id, relationship_type) VALUES
            ((SELECT id FROM students WHERE user_id = student_user_id), (SELECT id FROM parents WHERE user_id = parent_user_id), 'Father');
        END IF;
    END IF;
END $$;

-- ===========================================
-- Insert Sample Announcements
-- ===========================================

INSERT INTO announcements (title, content, target_audience, start_date, end_date, created_by) VALUES
(
    'Welcome to the New Academic Year 2024-2025',
    'Welcome back to Rwanda Coding Academy! We are excited to start another academic year filled with learning and growth. Please check your timetables and ensure all required materials are ready.',
    'ALL',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '30 days',
    (SELECT id FROM users WHERE email = 'admin@rca.ac.rw')
),
(
    'Parent-Teacher Meeting Schedule',
    'Parent-teacher meetings will be held on the first Friday of each month. Please check the school calendar for specific dates and times.',
    'PARENTS,TEACHERS',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '90 days',
    (SELECT id FROM users WHERE email = 'admin@rca.ac.rw')
),
(
    'Computer Science Club Registration',
    'Registration for the Computer Science Club is now open! Students interested in programming, robotics, and technology are encouraged to join. Meetings will be held every Wednesday after school.',
    'STUDENTS',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '14 days',
    (SELECT id FROM users WHERE email = 'teacher@rca.ac.rw')
);

-- ===========================================
-- Insert Sample Messages
-- ===========================================

-- Get user IDs for sample messages
DO $$
DECLARE
    admin_user_id UUID;
    teacher_user_id UUID;
    student_user_id UUID;
    parent_user_id UUID;
BEGIN
    SELECT id INTO admin_user_id FROM users WHERE email = 'admin@rca.ac.rw';
    SELECT id INTO teacher_user_id FROM users WHERE email = 'teacher@rca.ac.rw';
    SELECT id INTO student_user_id FROM users WHERE email = 'student@rca.ac.rw';
    SELECT id INTO parent_user_id FROM users WHERE email = 'parent@rca.ac.rw';
    
    IF admin_user_id IS NOT NULL AND teacher_user_id IS NOT NULL THEN
        INSERT INTO messages (sender_id, recipient_id, subject, content, message_type, priority) VALUES
        (admin_user_id, teacher_user_id, 'Welcome to RCA', 'Welcome to our teaching team! We are excited to have you on board.', 'GENERAL', 'NORMAL'),
        (teacher_user_id, admin_user_id, 'Class Schedule Request', 'I would like to request a review of my class schedule for the upcoming term.', 'REQUEST', 'NORMAL');
    END IF;
    
    IF teacher_user_id IS NOT NULL AND student_user_id IS NOT NULL THEN
        INSERT INTO messages (sender_id, recipient_id, subject, content, message_type, priority) VALUES
        (teacher_user_id, student_user_id, 'Assignment Reminder', 'Please remember to submit your programming assignment by Friday.', 'REMINDER', 'HIGH');
    END IF;
    
    IF parent_user_id IS NOT NULL AND admin_user_id IS NOT NULL THEN
        INSERT INTO messages (sender_id, recipient_id, subject, content, message_type, priority) VALUES
        (parent_user_id, admin_user_id, 'Transportation Inquiry', 'I would like to inquire about the school transportation service for my child.', 'INQUIRY', 'NORMAL');
    END IF;
END $$;
