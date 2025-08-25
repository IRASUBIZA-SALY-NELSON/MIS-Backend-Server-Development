-- Fix student_parents table to match StudentParent entity model
-- The entity extends BaseEntity so it needs an id column and other fields

-- Drop the existing table and recreate it with proper structure
DROP TABLE IF EXISTS student_parents CASCADE;

-- Create student_parents table with proper entity structure
CREATE TABLE student_parents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    parent_id UUID REFERENCES parents(id) ON DELETE CASCADE,
    guardian_id UUID REFERENCES guardians(id) ON DELETE CASCADE,
    relationship_type VARCHAR(50) NOT NULL DEFAULT 'PARENT',
    relationship_start_date DATE,
    relationship_end_date DATE,
    is_primary_contact BOOLEAN NOT NULL DEFAULT false,
    is_emergency_contact BOOLEAN NOT NULL DEFAULT false,
    is_authorized_pickup BOOLEAN NOT NULL DEFAULT false,
    is_financial_responsible BOOLEAN NOT NULL DEFAULT false,
    is_educational_responsible BOOLEAN NOT NULL DEFAULT false,
    contact_priority INTEGER,
    preferred_contact_method VARCHAR(50),
    preferred_contact_time VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT true,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_student_parent UNIQUE (student_id, parent_id),
    CONSTRAINT unique_student_guardian UNIQUE (student_id, guardian_id)
);
