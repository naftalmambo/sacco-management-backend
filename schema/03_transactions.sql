-- ====================================================================
-- TABLE: transactions
-- DESCRIPTION: Permanent history ledger tracking all SACCO account cash movements.
--
-- DESIGN DECISIONS & ARCHITECTURAL HIGHLIGHTS:
-- 1. Immutable Ledger Pattern (ON DELETE RESTRICT):
--    Blocks anyone from deleting an account if it has money history tied to it.
-- 2. Fixed-Point Numeric Precision (NUMERIC(15, 2)):
--    Used instead of FLOAT to stop the computer from making rounding mistakes.
-- 3. Domain Whitelisting (CHECK Constraints):
--    Restricts actions strictly to 'DEPOSIT', 'WITHDRAWAL', or 'LOAN REPAYMENT'.
-- 4. Currency Isolation (currency = 'KES'):
--    Forces every record to be marked as Kenyan Shillings to keep history clean.
-- ====================================================================
DROP TABLE IF EXISTS transactions CASCADE;

CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    account_id bigint NOT NULL,
    transaction_type VARCHAR(20) NOT NULL CONSTRAINT chk_trans_type CHECK (
        transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'LOAN REPAYMENT')
    ),
    amount NUMERIC(15, 2) NOT NULL CONSTRAINT chk_trans_amount CHECK (amount > 0),
    currency VARCHAR(3) DEFAULT 'KES' NOT NULL CONSTRAINT chk_trans_currency CHECK (currency = 'KES'),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    -- Table Constraints placed cleanly at the bottom
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts (account_id) ON DELETE RESTRICT
);