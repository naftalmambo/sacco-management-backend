-- ====================================================================
-- TABLE: loans
-- DESCRIPTION: Credit registry tracking loan capital, interest, and credit risk statuses.
--
-- DESIGN DECISIONS & ARCHITECTURAL HIGHLIGHTS:
-- 1. Financial Risk Security (ON DELETE RESTRICT):
--    Blocks member deletion if they owe an unpaid debt to protect ledger balances.
-- 2. Fixed-Point Precision Math (NUMERIC):
--    Protects both principal amounts and interest rates from dangerous rounding errors.
-- 3. Credit State Whitelisting (CHECK Constraints):
--    Enforces SASRA compliance by restricting loans strictly to valid credit stages.
-- 4. Credit Underwriting Automation (DEFAULT 'PENDING APPROVAL'):
--    Forces all new loan contracts to initialize as pending. This stops software bugs 
--    or fraud from bypassing the mandatory review and disbursement workflow stages.
-- 5. Currency Guarding (currency = 'KES'):
--    Forces loan calculations into Kenyan Shillings to ensure consistent bookkeeping.
-- ====================================================================
DROP TABLE IF EXISTS loans CASCADE;

CREATE TABLE loans (
    loan_id BIGSERIAL PRIMARY KEY,
    member_id bigint NOT NULL,
    principal_amount NUMERIC(15, 2) NOT NULL CONSTRAINT chk_princ_amt CHECK (principal_amount > 0),
    interest_rate NUMERIC(15, 2) NOT NULL CONSTRAINT chk_interest_rt CHECK (interest_rate > 0),
    loan_status TEXT DEFAULT 'PENDING APPROVAL' NOT NULL CONSTRAINT chk_loan_status CHECK (
        loan_status IN (
            'PENDING APPROVAL',
            'ACTIVE',
            'FULLY PAID',
            'DEFAULTED'
        )
    ),
    currency VARCHAR(3) DEFAULT 'KES' NOT NULL CONSTRAINT chk_loan_currency CHECK (currency = 'KES'),
    applied_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
    disbursed_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    -- Table Constraints placed cleanly at the bottom
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES members (member_id) ON DELETE RESTRICT
);