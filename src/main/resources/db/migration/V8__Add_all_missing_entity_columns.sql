-- Comprehensive migration to add all missing columns for entity models
-- This migration adds missing columns that exist in entity models but not in the database schema

-- Add missing columns to grade_scales table
ALTER TABLE grade_scales 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS is_passing BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS color_code VARCHAR(7),
ADD COLUMN IF NOT EXISTS remarks TEXT;

-- Add missing columns to subjects table (if not already added in V4)
ALTER TABLE subjects 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS category VARCHAR(50),
ADD COLUMN IF NOT EXISTS code VARCHAR(20);

-- Add missing columns to students table
ALTER TABLE students 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS blood_group VARCHAR(10),
ADD COLUMN IF NOT EXISTS medical_conditions TEXT,
ADD COLUMN IF NOT EXISTS emergency_contact_name VARCHAR(100),
ADD COLUMN IF NOT EXISTS emergency_contact_phone VARCHAR(20),
ADD COLUMN IF NOT EXISTS emergency_contact_relationship VARCHAR(50);

-- Add missing columns to academic_years table (if not already added in V4)
ALTER TABLE academic_years 
ADD COLUMN IF NOT EXISTS academic_calendar TEXT,
ADD COLUMN IF NOT EXISTS holidays TEXT,
ADD COLUMN IF NOT EXISTS exam_schedule TEXT;

-- Add missing columns to terms table (if not already added in V4)
ALTER TABLE terms 
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'PENDING';

-- Add missing columns to user_profiles table
ALTER TABLE user_profiles 
ADD COLUMN IF NOT EXISTS nationality VARCHAR(50),
ADD COLUMN IF NOT EXISTS id_number VARCHAR(50),
ADD COLUMN IF NOT EXISTS emergency_contact VARCHAR(20);

-- Add missing columns to parents table
ALTER TABLE parents 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS income_level VARCHAR(50),
ADD COLUMN IF NOT EXISTS education_level VARCHAR(50);

-- Add missing columns to users table
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS last_login TIMESTAMP,
ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER DEFAULT 0,
ADD COLUMN IF NOT EXISTS account_locked_until TIMESTAMP;
