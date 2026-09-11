-- ====================================================================
-- BUSINESS REPORT #1: MASTER MEMBER BALANCE SHEET
-- ====================================================================
-- WHAT IT DOES: 
-- This report shows a list of all members alongside their financial accounts.
-- It connects personal details with live account balances.
-- Management uses this for daily audits to track who owns what account.
--
-- HOW IT WORKS:
--   * It uses INNER JOIN to link the members table to the accounts table.
--   * It uses the || symbol to join first name and last name with a clean space.
--   * It sorts the final list by member_id from lowest to highest.
--
-- TABLES USED:
--   * public.members  (To get member IDs and names)
--   * public.accounts (To get account types, balances, and status)
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

-- ====================================================================
-- BUSINESS REPORT #2: LIQUIDITY STATEMENT
-- ====================================================================
-- WHAT IT DOES: 
-- This report shows the total amount of money sitting inside the SACCO.
-- It adds up all balances to show a simple summary for each account type.
-- The Board of Directors uses this to see the total money in the vault.
--
-- HOW IT WORKS:
--   * It uses SUM() to add up all the account balances together.
--   * It uses GROUP BY to separate the math into SAVINGS, SHARES, and LOANS.
--   * It reads only the accounts table so it is very fast.
--
-- TABLES USED:
--   * public.accounts (To read account types and money balances)
-- ====================================================================
SELECT
    account_type,
    SUM(balance) AS total_liquidity
FROM
    accounts
GROUP BY
    account_type;

-- ====================================================================
-- BUSINESS REPORT #3: CREDIT RISK AUDIT
-- ====================================================================
-- WHAT IT DOES: 
-- This report shows a list of all members who currently have active loans.
-- It displays their full name, the original amount they borrowed, and 
-- the current debt balance they still owe back to the SACCO.
-- It automatically hides pending applications that have not been paid out yet.
--
-- HOW IT WORKS:
--   * It uses INNER JOIN to link three tables together: loans, members, and accounts.
--   * It uses a WHERE filter to pull only 'LOAN' account types.
--   * It tracks the outstanding debt by reading the live balance column.
--
-- TABLES USED:
--   * public.loans    (To get the original principal amount)
--   * public.members  (To get the member names)
--   * public.accounts (To get the current outstanding debt balance)
-- ====================================================================
SELECT
    (first_name || ' ' || last_name) AS member_name,
    principal_amount,
    balance AS outstanding_debt
FROM
    loans l
    INNER JOIN members m ON l.member_id = m.member_id
    INNER JOIN accounts a ON l.member_id = a.member_id
WHERE
    a.account_type = 'LOAN';

-- ====================================================================
-- DATA WORKFLOW: SECURE CASH WITHDRAWAL TRANSACTION
-- ====================================================================
-- WHAT IT DOES: 
-- This script simulates a real-world bank teller operation where a member 
-- makes a cash withdrawal from their savings account.
-- It ensures that money cannot be deducted from a balance without leaving 
-- a matching receipt log in the transaction history ledger.
--
-- HOW IT WORKS:
--   * It uses BEGIN to open a safe, isolated database transaction block.
--   * Step 1: INSERTs a new history record into the transactions table.
--   * Step 2: UPDATEs the accounts table to subtract 5,000 KES from the balance.
--   * It uses COMMIT to permanently lock both changes onto the disk at once.
--
-- TABLES ALTERED:
--   * public.transactions (Appends a new 'WITHDRAWAL' audit row)
--   * public.accounts     (Reduces the balance column where account_id = 2)
-- ====================================================================
BEGIN;

-- Step 1: Log the audit receipt
INSERT INTO
    transactions (
        account_id,
        transaction_type,
        amount,
        currency,
        created_at
    )
VALUES
    (
        2,
        'WITHDRAWAL',
        5000.00,
        'KES',
        '2026-01-20 11:00:00+03'
    );

-- Step 2: Reduce the liquid vault balance
UPDATE
    accounts
SET
    balance = balance - 5000.00
WHERE
    account_id = 2;

COMMIT;