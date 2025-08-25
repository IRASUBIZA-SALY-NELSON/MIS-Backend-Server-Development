-- Fix system_settings table category constraint issues
-- This migration handles existing invalid category values

-- Update any existing invalid category values to GENERAL
UPDATE system_settings SET category = 'GENERAL' 
WHERE category IS NOT NULL AND category NOT IN ('GENERAL','SECURITY','EMAIL','NOTIFICATION','ACADEMIC','FINANCIAL','INTEGRATION','UI','REPORTING','MAINTENANCE');

-- Set default values for any remaining null categories
UPDATE system_settings SET category = 'GENERAL' WHERE category IS NULL;

-- Add NOT NULL constraint for category if it doesn't already exist
DO $$
BEGIN
    BEGIN
        ALTER TABLE system_settings ALTER COLUMN category SET NOT NULL;
    EXCEPTION
        WHEN others THEN
            -- Column already has NOT NULL constraint or other issue
            NULL;
    END;
END $$;

-- Add check constraint for category enum values if it doesn't exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'chk_system_settings_category') THEN
        ALTER TABLE system_settings ADD CONSTRAINT chk_system_settings_category 
            CHECK (category IN ('GENERAL','SECURITY','EMAIL','NOTIFICATION','ACADEMIC','FINANCIAL','INTEGRATION','UI','REPORTING','MAINTENANCE'));
    END IF;
END $$;
