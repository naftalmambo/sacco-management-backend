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