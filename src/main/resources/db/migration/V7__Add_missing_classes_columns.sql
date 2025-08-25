-- Add missing columns to classes table
-- This migration adds the is_active column that the entity model expects

-- Add missing is_active column to classes table
ALTER TABLE classes 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT true;
