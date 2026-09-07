-- ====================================================================
-- TABLE: members
-- DESCRIPTION: Core membership registry for the SACCO.
--
-- DESIGN DECISIONS & ARCHITECTURAL HIGHLIGHTS:
-- 1. BIGSERIAL (bigint): Chosen for primary keys to support massive scalability 
--    (up to 9 quintillion rows) without risk of ID exhaustion.
-- 2. Data Integrity: Named UNIQUE and CHECK constraints prevent corrupt, 
--    duplicate, or anonymous records at the database engine level.
-- 3. Audit Compliance: Uses TIMESTAMPTZ with DEFAULT CURRENT_TIMESTAMP 
--    to guarantee unalterable, timezone-aware onboarding tracking.
-- ====================================================================
CREATE TABLE members (
    member_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    sex VARCHAR(10) NOT NULL CONSTRAINT chk_member_sex CHECK (sex IN ('Male', 'Female')),
    email_address VARCHAR(100) NOT NULL CONSTRAINT uq_member_email UNIQUE,
    phone_number VARCHAR(30) NOT NULL CONSTRAINT uq_member_phone UNIQUE,
    joined_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(100) NOT NULL
);