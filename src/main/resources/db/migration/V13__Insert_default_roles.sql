-- Insert default roles for the system
-- This migration creates the basic roles needed for user registration

-- Create roles table if it doesn't exist (should already exist from initial schema)
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    permissions TEXT
);

-- Insert default roles
INSERT INTO roles (name, description, permissions) VALUES
('ADMIN', 'System Administrator with full access', '["*"]'),
('TEACHER', 'Teacher with academic management permissions', '["academic:read", "academic:write", "student:read", "grade:write", "attendance:write"]'),
('STUDENT', 'Student with limited access to their own data', '["profile:read", "grade:read", "attendance:read", "announcement:read"]'),
('PARENT', 'Parent with access to their children data', '["student:read", "grade:read", "attendance:read", "announcement:read", "parent:write"]')
ON CONFLICT (name) DO NOTHING;
