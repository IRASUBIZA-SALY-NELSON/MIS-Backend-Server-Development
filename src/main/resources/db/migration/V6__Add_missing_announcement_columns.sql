-- Add missing columns to announcements table
-- This migration adds the priority column that the entity model expects

-- Add missing priority column
ALTER TABLE announcements 
ADD COLUMN IF NOT EXISTS priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM';

-- Add any other missing columns that might be needed
ALTER TABLE announcements 
ADD COLUMN IF NOT EXISTS is_urgent BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS attachment_url VARCHAR(500),
ADD COLUMN IF NOT EXISTS read_count INTEGER DEFAULT 0;
