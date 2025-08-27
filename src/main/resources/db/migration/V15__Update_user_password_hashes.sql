-- ===========================================
-- Update User Password Hashes
-- ===========================================
-- This migration fixes the password hashes for sample users to work with BCrypt strength 12

-- Update admin password hash
UPDATE users 
SET password_hash = '$2a$12$xXbP/rOcf7fVvRcRPa2jxeGBnE.3sqAz0pHyQeX2duRT4x4OfNTT6' 
WHERE email = 'admin@rca.ac.rw';

-- Update teacher password hash  
UPDATE users 
SET password_hash = '$2a$12$muGPhsSVqMGFC1yopIyRxe80qy9P.55/otrWisQfcdQ.8wxEGOS0q' 
WHERE email = 'teacher@rca.ac.rw';

-- Update student password hash
UPDATE users 
SET password_hash = '$2a$12$TTbE0oNGFUQ2l49ofpg5wuTyP4BArf40KSFb.x.pv4FOuIch5HDWy' 
WHERE email = 'student@rca.ac.rw';
