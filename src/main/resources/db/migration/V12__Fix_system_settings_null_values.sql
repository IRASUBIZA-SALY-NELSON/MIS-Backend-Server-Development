-- Fix system_settings table null values before adding NOT NULL constraints
-- This migration handles existing null values in boolean and enum columns

-- First, add columns without NOT NULL constraint if they don't exist
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS data_type VARCHAR(50);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS is_encrypted BOOLEAN;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS is_editable BOOLEAN;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS is_visible BOOLEAN;

-- Update null values with appropriate defaults
UPDATE system_settings SET data_type = 'STRING' WHERE data_type IS NULL;
UPDATE system_settings SET is_encrypted = false WHERE is_encrypted IS NULL;
UPDATE system_settings SET is_editable = true WHERE is_editable IS NULL;
UPDATE system_settings SET is_visible = true WHERE is_visible IS NULL;

-- Add NOT NULL constraints and check constraints
ALTER TABLE system_settings ALTER COLUMN data_type SET NOT NULL;
ALTER TABLE system_settings ALTER COLUMN is_encrypted SET NOT NULL;
ALTER TABLE system_settings ALTER COLUMN is_editable SET NOT NULL;
ALTER TABLE system_settings ALTER COLUMN is_visible SET NOT NULL;

-- Add check constraint for data_type enum values
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'chk_system_settings_data_type') THEN
        ALTER TABLE system_settings ADD CONSTRAINT chk_system_settings_data_type 
            CHECK (data_type IN ('STRING','INTEGER','DECIMAL','BOOLEAN','DATE','DATETIME','JSON','XML','BINARY'));
    END IF;
END $$;

-- Add other missing columns if they don't exist
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS category VARCHAR(50);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS validation_regex VARCHAR(500);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS default_value TEXT;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS min_value VARCHAR(255);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS max_value VARCHAR(255);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS allowed_values TEXT;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS last_modified_by VARCHAR(255);
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS last_modified_at TIMESTAMP;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS version INTEGER;
ALTER TABLE system_settings ADD COLUMN IF NOT EXISTS metadata JSONB;

-- Set default values for category and version
UPDATE system_settings SET category = 'GENERAL' WHERE category IS NULL;
UPDATE system_settings SET version = 1 WHERE version IS NULL;

-- Update any existing invalid category values to GENERAL
UPDATE system_settings SET category = 'GENERAL' 
WHERE category NOT IN ('GENERAL','SECURITY','EMAIL','NOTIFICATION','ACADEMIC','FINANCIAL','INTEGRATION','UI','REPORTING','MAINTENANCE');

-- Add NOT NULL constraint for category if it doesn't already exist
DO $$
BEGIN
    BEGIN
        ALTER TABLE system_settings ALTER COLUMN category SET NOT NULL;
    EXCEPTION
        WHEN others THEN
            -- Column already has NOT NULL constraint
            NULL;
    END;
END $$;

-- Add check constraint for category enum values
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'chk_system_settings_category') THEN
        ALTER TABLE system_settings ADD CONSTRAINT chk_system_settings_category 
            CHECK (category IN ('GENERAL','SECURITY','EMAIL','NOTIFICATION','ACADEMIC','FINANCIAL','INTEGRATION','UI','REPORTING','MAINTENANCE'));
    END IF;
END $$;
