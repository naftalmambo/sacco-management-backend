-- ====================================================================
-- TABLE: members
-- DESCRIPTION: Core membership registry for the Kenyan SACCO system.
--
-- DESIGN DECISIONS & ARCHITECTURAL HIGHLIGHTS:
-- 1. BIGSERIAL (bigint): Chosen for primary keys to support massive scalability 
--    (up to 9 quintillion rows) without risk of ID exhaustion.
-- 2. SASRA Compliance (Geographic Profiling): Separated location into country 
--    and county. Hardlocked country to 'Kenya' to meet localized regulatory standards.
-- 3. M-Pesa Ready Integration: Enforced a strict regex constraint on phone 
--    numbers (~ '^\+254\d{9}$') to guarantee E.164 international formatting 
--    required by mobile money transaction networks.
-- 4. Data Integrity & Audits: Named UNIQUE and CHECK constraints eliminate corrupt, 
--    duplicate, or anonymous records directly at the database engine layer.
-- ====================================================================
DROP TABLE IF EXISTS members CASCADE;

CREATE TABLE members (
    member_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    sex VARCHAR(10) NOT NULL CONSTRAINT chk_member_sex CHECK (sex IN ('Male', 'Female')),
    email_address VARCHAR(100) NOT NULL CONSTRAINT uq_member_email UNIQUE,
    phone_number VARCHAR(15) NOT NULL CONSTRAINT uq_member_phone UNIQUE CONSTRAINT chk_kenyan_phone CHECK (phone_number ~ '^\+254\d{9}$'),
    country VARCHAR(30) DEFAULT 'Kenya' NOT NULL CONSTRAINT chk_member_country CHECK (country = 'Kenya'),
    county VARCHAR(50) NOT NULL,
    joined_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);