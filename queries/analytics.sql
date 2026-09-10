-- ====================================================================
-- BUSINESS REPORT #1: MASTER MEMBER BALANCE SHEET
-- ====================================================================
-- DESCRIPTION: 
-- Compiles a comprehensive corporate dashboard linking individual member
-- identity data directly to their active asset and liability buckets. 
-- Used by management for daily account auditing and profile tracking.
--
-- TARGET ARCHITECTURE:
--   * Relational Map: One-to-Many (One Member -> Multiple Financial Accounts)
--   * Integrity: Primary Key (members.member_id) binds to Foreign Key (accounts.member_id)
--   * Data Formatting: String Concatenation used to assemble individual text fields 
--     into a unified 'member_name' column with explicit spacing.
--
-- TABLES UTILIZED:
--   * public.members   (Parent Register - Name, ID)
--   * public.accounts  (Child Ledger - Account Type, Balance, Status)
--
-- EXECUTION SORTING: 
-- Organized strictly by 'member_id' in Ascending order for sequential audit sanity.
-- ====================================================================
SELECT
    m.member_id,
    (first_name || ' ' || last_name) AS member_name,
    account_type,
    balance,
    account_status
FROM
    members m
    INNER JOIN accounts a ON m.member_id = a.member_id
ORDER BY
    m.member_id;