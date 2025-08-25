-- Fix teacher table by adding missing boolean columns
-- This migration adds the boolean columns that the entity model expects

-- Add missing boolean columns with default values and NOT NULL constraints
ALTER TABLE teachers 
ADD COLUMN IF NOT EXISTS is_full_time BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN IF NOT EXISTS is_head_of_department BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_mentor BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_principal BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_advisor BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_coordinator BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS is_dean BOOLEAN NOT NULL DEFAULT false;

-- Add other missing columns that might be needed
ALTER TABLE teachers 
ADD COLUMN IF NOT EXISTS current_classes INTEGER DEFAULT 0,
ADD COLUMN IF NOT EXISTS current_students INTEGER DEFAULT 0;
