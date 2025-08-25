-- Add missing columns to parents table for Parent entity model
-- This migration adds all the boolean columns that the Parent entity expects

-- Add missing boolean columns to parents table
ALTER TABLE parents 
ADD COLUMN IF NOT EXISTS is_primary_contact BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_emergency_contact BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_authorized_pickup BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_financial_guardian BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_educational_guardian BOOLEAN NOT NULL DEFAULT false;

-- Add other missing columns for Parent entity
ALTER TABLE parents 
ADD COLUMN IF NOT EXISTS work_phone VARCHAR(20),
ADD COLUMN IF NOT EXISTS emergency_contact_relationship VARCHAR(50),
ADD COLUMN IF NOT EXISTS emergency_contact_name VARCHAR(100),
ADD COLUMN IF NOT EXISTS preferred_contact_method VARCHAR(50),
ADD COLUMN IF NOT EXISTS preferred_contact_time VARCHAR(50),
ADD COLUMN IF NOT EXISTS notes TEXT;
