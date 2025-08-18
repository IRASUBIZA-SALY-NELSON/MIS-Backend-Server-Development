-- ===========================================
-- Fix Foreign Key References
-- Version: 3.0.0
-- Description: Fixes foreign key reference issues in the schema
-- ===========================================

-- First, add missing columns if they don't exist
ALTER TABLE assessments ADD COLUMN IF NOT EXISTS teacher_id UUID;

-- Drop existing foreign key constraints that reference the wrong tables
ALTER TABLE classes DROP CONSTRAINT IF EXISTS classes_class_teacher_id_fkey;
ALTER TABLE class_subjects DROP CONSTRAINT IF EXISTS class_subjects_teacher_id_fkey;
ALTER TABLE timetable_slots DROP CONSTRAINT IF EXISTS timetable_slots_teacher_id_fkey;
ALTER TABLE student_marks DROP CONSTRAINT IF EXISTS student_marks_graded_by_fkey;
ALTER TABLE assessments DROP CONSTRAINT IF EXISTS assessments_teacher_id_fkey;
ALTER TABLE timetable_exceptions DROP CONSTRAINT IF EXISTS timetable_exceptions_substitute_teacher_id_fkey;

-- Add correct foreign key constraints
ALTER TABLE classes ADD CONSTRAINT classes_class_teacher_id_fkey 
    FOREIGN KEY (class_teacher_id) REFERENCES teachers(id);

ALTER TABLE class_subjects ADD CONSTRAINT class_subjects_teacher_id_fkey 
    FOREIGN KEY (teacher_id) REFERENCES teachers(id);

ALTER TABLE timetable_slots ADD CONSTRAINT timetable_slots_teacher_id_fkey 
    FOREIGN KEY (teacher_id) REFERENCES teachers(id);

ALTER TABLE student_marks ADD CONSTRAINT student_marks_graded_by_fkey 
    FOREIGN KEY (graded_by) REFERENCES teachers(id);

ALTER TABLE assessments ADD CONSTRAINT assessments_teacher_id_fkey 
    FOREIGN KEY (teacher_id) REFERENCES teachers(id);

ALTER TABLE timetable_exceptions ADD CONSTRAINT timetable_exceptions_substitute_teacher_id_fkey 
    FOREIGN KEY (substitute_teacher_id) REFERENCES teachers(id);
