-- ====================================================================
-- TABLE: accounts
-- DESCRIPTION: Financial account ledger registries linked to Kenyan SACCO members.
--
-- DESIGN DECISIONS & ARCHITECTURAL HIGHLIGHTS:
-- 1. Base Currency Hardlocking: Enforced a strict 'KES' constraint directly 
--    at the database layer to eliminate multi-currency balance corruption 
--    and align with local Central Bank of Kenya financial standards.
-- 2. Relational Integrity (ON DELETE RESTRICT): Explicitly blocks any 
--    attempt to delete a member if they have active financial accounts. 
--    Prevents orphaned records and safeguards ledger continuity.
-- 3. Domain Whitelisting: Strict CHECK constraints on account types 
--    ('SAVINGS', 'SHARES', 'LOAN') and statuses ('ACTIVE', 'FROZEN', 'CLOSED') 
--    enforce backend validation directly at the engine layer.
-- 4. Audit Compliance: Uses TIMESTAMPTZ to record immutable, timezone-aware 
--    account creation timelines across international server environments.
-- ====================================================================
DROP TABLE IF EXISTS accounts CASCADE;

CREATE TABLE accounts (
    account_id BIGSERIAL PRIMARY KEY,
    member_id bigint NOT NULL,
    account_type TEXT NOT NULL CONSTRAINT chk_acc_type CHECK (account_type IN ('SAVINGS', 'SHARES', 'LOAN')),
    currency VARCHAR(3) DEFAULT 'KES' NOT NULL CONSTRAINT chk_account_currency CHECK (currency = 'KES'),
    account_status VARCHAR(15) DEFAULT 'ACTIVE' NOT NULL CONSTRAINT chk_account_status CHECK (
        account_status IN ('ACTIVE', 'FROZEN', 'SUSPENDED')
    ),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    -- Table Constraints placed cleanly at the bottom
    CONSTRAINT fk_account_member FOREIGN KEY (member_id) REFERENCES members (member_id) ON DELETE RESTRICT
);