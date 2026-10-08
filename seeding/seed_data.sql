TRUNCATE TABLE transactions,
loans,
accounts,
members RESTART IDENTITY CASCADE;

-- 1. SEED MEMBERS DATA
-- Includes 15 valid Kenyan profiles with explicit historical joining timelines.
INSERT INTO
    members (
        first_name,
        last_name,
        date_of_birth,
        sex,
        email_address,
        phone_number,
        national_id,
        country,
        county,
        joined_date
    )
VALUES
    (
        'Naftal',
        'Mambo',
        '1992-04-22',
        'Male',
        'naftal.mambo@saccotech.co.ke',
        '+254712345678',
        '30452387',
        'Kenya',
        'Nairobi',
        '2024-01-15 09:30:00+03'
    ),
    (
        'Amina',
        'Omondi',
        '1998-09-22',
        'Female',
        'amina.omondi@finmail.ke',
        '+254722987654',
        '28764508',
        'Kenya',
        'Mombasa',
        '2024-02-20 14:15:00+03'
    ),
    (
        'David',
        'Kiprono',
        '1990-11-05',
        'Male',
        'david.kip@domain.co.ke',
        '+254701443322',
        '29658756',
        'Kenya',
        'Kericho',
        '2024-03-05 11:00:00+03'
    ),
    (
        'Grace',
        'Wambui',
        '1987-01-30',
        'Female',
        'grace.w@wambuicredit.ke',
        '+254733556677',
        '36579546',
        'Kenya',
        'Kiambu',
        '2024-04-12 08:45:00+03'
    ),
    (
        'Michael',
        'Onyango',
        '1993-07-18',
        'Male',
        'm.onyango@luobase.ke',
        '+254711889900',
        '26986742',
        'Kenya',
        'Kisumu',
        '2024-05-19 16:20:00+03'
    ),
    (
        'Sarah',
        'Cherotich',
        '2001-05-14',
        'Female',
        'sarah.chero@post.ke',
        '+254710223344',
        '31348705',
        'Kenya',
        'Uasin Gishu',
        '2024-06-25 10:10:00+03'
    ),
    (
        'John',
        'Mutua',
        '1985-03-25',
        'Male',
        'j.mutua@machakosdev.ke',
        '+254799112233',
        '26879067',
        'Kenya',
        'Machakos',
        '2024-07-02 13:05:00+03'
    ),
    (
        'Fatma',
        'Ali',
        '1996-12-02',
        'Female',
        'fatma.ali@pwani.co.ke',
        '+254780556677',
        '25690865',
        'Kenya',
        'Kwale',
        '2024-08-14 09:00:00+03'
    ),
    (
        'Emmanuel',
        'Wekesa',
        '1992-08-09',
        'Male',
        'wekesa.e@bungomafarmers.ke',
        '+254715443322',
        '26780567',
        'Kenya',
        'Bungoma',
        '2024-09-05 15:40:00+03'
    ),
    (
        'Mary',
        'Atieno',
        '1994-10-11',
        'Female',
        'mary.atieno@outlook.ke',
        '+254720112233',
        '36780954',
        'Kenya',
        'Siaya',
        '2024-10-22 11:55:00+03'
    ),
    (
        'Samuel',
        'Kamau',
        '1989-06-27',
        'Male',
        'kamau.sam@nyerihub.ke',
        '+254724998877',
        '25768645',
        'Kenya',
        'Nyeri',
        '2024-11-30 08:15:00+03'
    ),
    (
        'Joy',
        'Chepngetich',
        '1999-02-15',
        'Female',
        'joy.chep@bometfinance.ke',
        '+254111554433',
        '39845672',
        'Kenya',
        'Bomet',
        '2024-12-12 14:25:00+03'
    ),
    (
        'Peter',
        'Njoroge',
        '1983-04-03',
        'Male',
        'njoroge.p@nakurubiz.ke',
        '+254705667788',
        '28763452',
        'Kenya',
        'Nakuru',
        '2025-01-08 10:35:00+03'
    ),
    (
        'Lucy',
        'Mwari',
        '1997-07-29',
        'Female',
        'lucy.mwari@meruagri.ke',
        '+254734889900',
        '37645321',
        'Kenya',
        'Meru',
        '2025-02-14 09:50:00+03'
    ),
    (
        'Brian',
        'Otieno',
        '2002-10-24',
        'Male',
        'brian.oti@kisumuhub.ke',
        '+254719223344',
        '26785423',
        'Kenya',
        'Kisumu',
        '2025-03-01 16:00:00+03'
    );

-- 2. SEED ACCOUNTS DATA
-- Establishes financial assets and credit tracking balances across all 15 member IDs with authentic 12-digit numbers starting with 128.
INSERT INTO
    accounts (
        member_id,
        account_number,
        -- 🌟 Aligned 12-Digit Realism Strings
        account_type,
        balance,
        currency,
        account_status,
        created_at
    )
VALUES
    -- Member 1 (Naftal Mambo) - Joined 2024-01-15
    (
        1,
        '128456123789',
        'SHARES',
        450000.00,
        'KES',
        'ACTIVE',
        '2024-03-09 16:00:00+03'
    ),
    (
        1,
        '128456123790',
        'SAVINGS',
        25400.50,
        'KES',
        'ACTIVE',
        '2024-03-15 10:00:00+03'
    ),
    (
        1,
        '128456123791',
        'LOAN',
        150000.00,
        'KES',
        'ACTIVE',
        '2024-06-10 14:00:00+03'
    ),
    -- Member 2 (Amina Omondi) - Joined 2024-02-20
    (
        2,
        '128789456123',
        'SHARES',
        150000.00,
        'KES',
        'ACTIVE',
        '2024-02-22 09:15:00+03'
    ),
    (
        2,
        '128789456124',
        'SAVINGS',
        12350.00,
        'KES',
        'ACTIVE',
        '2024-02-25 14:00:00+03'
    ),
    -- Member 3 (David Kiprono) - Joined 2024-03-05
    (
        3,
        '128112233445',
        'SHARES',
        85000.00,
        'KES',
        'ACTIVE',
        '2024-03-05 11:30:00+03'
    ),
    (
        3,
        '128112233446',
        'SAVINGS',
        4500.00,
        'KES',
        'ACTIVE',
        '2024-03-10 16:45:00+03'
    ),
    -- Member 4 (Grace Wambui) - Joined 2024-04-12
    (
        4,
        '128998877665',
        'SHARES',
        320000.00,
        'KES',
        'ACTIVE',
        '2024-04-15 08:30:00+03'
    ),
    (
        4,
        '128998877666',
        'LOAN',
        250000.00,
        'KES',
        'ACTIVE',
        '2024-08-20 11:30:00+03'
    ),
    -- Member 5 (Michael Onyango) - Joined 2024-05-19
    (
        5,
        '128554433221',
        'SHARES',
        55000.00,
        'KES',
        'ACTIVE',
        '2024-05-20 10:00:00+03'
    ),
    (
        5,
        '128554433222',
        'SAVINGS',
        7800.50,
        'KES',
        'FROZEN',
        '2024-05-25 11:15:00+03'
    ),
    -- Member 6 (Sarah Cherotich) - Joined 2024-06-25
    (
        6,
        '128667788990',
        'SHARES',
        110000.00,
        'KES',
        'ACTIVE',
        '2024-06-25 10:30:00+03'
    ),
    -- Member 7 (John Mutua) - Joined 2024-07-02
    (
        7,
        '128334455667',
        'SHARES',
        95000.00,
        'KES',
        'ACTIVE',
        '2024-07-05 14:20:00+03'
    ),
    (
        7,
        '128334455668',
        'SAVINGS',
        15000.00,
        'KES',
        'ACTIVE',
        '2024-07-12 09:00:00+03'
    ),
    -- Member 8 (Fatma Ali) - Joined 2024-08-14
    (
        8,
        '128001122334',
        'SHARES',
        200000.00,
        'KES',
        'ACTIVE',
        '2024-08-15 09:30:00+03'
    ),
    -- Member 9 (Emmanuel Wekesa) - Joined 2024-09-05
    (
        9,
        '128776655443',
        'SHARES',
        45000.00,
        'KES',
        'ACTIVE',
        '2024-09-06 11:00:00+03'
    ),
    -- Member 10 (Mary Atieno) - Joined 2024-10-22
    (
        10,
        '128223344556',
        'SHARES',
        130000.00,
        'KES',
        'ACTIVE',
        '2024-10-25 15:30:00+03'
    ),
    (
        10,
        '128223344557',
        'SAVINGS',
        3400.00,
        'KES',
        'ACTIVE',
        '2024-11-01 10:15:00+03'
    ),
    -- Member 11 (Samuel Kamau) - Joined 2024-11-30
    (
        11,
        '128889900112',
        'SHARES',
        75000.00,
        'KES',
        'ACTIVE',
        '2024-12-01 09:00:00+03'
    ),
    -- Member 12 (Joy Chepngetich) - Joined 2024-12-12
    (
        12,
        '128445566778',
        'SHARES',
        60000.00,
        'KES',
        'ACTIVE',
        '2024-12-15 14:00:00+03'
    ),
    (
        12,
        '128445566779',
        'SAVINGS',
        9200.00,
        'KES',
        'SUSPENDED',
        '2024-12-20 16:30:00+03'
    ),
    -- Member 13 (Peter Njoroge) - Joined 2025-01-08
    (
        13,
        '128009988776',
        'SHARES',
        500000.00,
        'KES',
        'ACTIVE',
        '2025-01-10 10:00:00+03'
    ),
    -- Member 14 (Lucy Mwari) - Joined 2025-02-14
    (
        14,
        '128113355779',
        'SHARES',
        115000.00,
        'KES',
        'ACTIVE',
        '2025-02-15 11:30:00+03'
    ),
    -- Member 15 (Brian Otieno) - Joined 2025-03-01
    (
        15,
        '128224466880',
        'SHARES',
        40000.00,
        'KES',
        'ACTIVE',
        '2025-03-02 09:45:00+03'
    );

-- 3. SEED TRANSACTIONS LEDGER DATA
-- Posts sequential historical ledger records grouped cleanly by Account ID using clear standard Shillings and Cents decimals.
INSERT INTO
    transactions (
        account_id,
        loan_id,
        transaction_type,
        amount,
        currency,
        created_at
    )
VALUES
    -- Member 1 (Naftal Mambo) - Account 1 (SHARES), Account 2 (SAVINGS), Account 3 (LOAN)
    (
        1,
        NULL,
        'DEPOSIT',
        200000.00,
        'KES',
        '2024-03-09 16:00:00+03'
    ),
    (
        1,
        NULL,
        'DEPOSIT',
        250000.00,
        'KES',
        '2024-04-10 11:15:00+03'
    ),
    (
        2,
        NULL,
        'DEPOSIT',
        30000.00,
        'KES',
        '2024-03-15 10:00:00+03'
    ),
    (
        2,
        NULL,
        'WITHDRAWAL',
        4599.50,
        'KES',
        '2024-05-12 14:20:00+03'
    ),
    (
        3,
        1,
        -- Linked directly to Member 1's Active Loan (Loan ID 1)
        'LOAN REPAYMENT',
        15000.00,
        'KES',
        '2024-07-10 11:00:00+03'
    ),
    -- Member 2 (Amina Omondi) - Account 4 (SHARES), Account 5 (SAVINGS)
    (
        4,
        NULL,
        'DEPOSIT',
        50000.00,
        'KES',
        '2024-02-22 09:15:00+03'
    ),
    (
        4,
        NULL,
        'DEPOSIT',
        100000.00,
        'KES',
        '2024-03-18 10:30:00+03'
    ),
    (
        5,
        NULL,
        'DEPOSIT',
        1500.00,
        'KES',
        '2024-02-25 14:00:00+03'
    ),
    (
        5,
        NULL,
        'WITHDRAWAL',
        2650.00,
        'KES',
        '2024-04-02 11:00:00+03'
    ),
    -- Member 3 (David Kiprono) - Account 6 (SHARES), Account 7 (SAVINGS)
    (
        6,
        NULL,
        'DEPOSIT',
        40000.00,
        'KES',
        '2024-03-05 11:30:00+03'
    ),
    (
        6,
        NULL,
        'DEPOSIT',
        45000.00,
        'KES',
        '2024-04-12 09:15:00+03'
    ),
    (
        7,
        NULL,
        'DEPOSIT',
        6000.00,
        'KES',
        '2024-03-10 16:45:00+03'
    ),
    (
        7,
        NULL,
        'WITHDRAWAL',
        1500.00,
        'KES',
        '2024-05-18 13:00:00+03'
    ),
    -- Member 4 (Grace Wambui) - Account 8 (SHARES), Account 9 (LOAN)
    (
        8,
        NULL,
        'DEPOSIT',
        120000.00,
        'KES',
        '2024-04-15 08:30:00+03'
    ),
    (
        8,
        NULL,
        'DEPOSIT',
        200000.00,
        'KES',
        '2024-06-01 10:00:00+03'
    ),
    (
        9,
        2,
        -- Linked directly to Member 4's Active Loan (Loan ID 2)
        'LOAN REPAYMENT',
        15000.00,
        'KES',
        '2024-12-18 13:16:00+03'
    ),
    (
        9,
        2,
        -- Linked directly to Member 4's Active Loan (Loan ID 2)
        'LOAN REPAYMENT',
        20000.00,
        'KES',
        '2025-01-18 10:00:00+03'
    ),
    -- Member 5 (Michael Onyango) - Account 10 (SHARES), Account 11 (SAVINGS)
    (
        10,
        NULL,
        'DEPOSIT',
        25000.00,
        'KES',
        '2024-05-20 10:00:00+03'
    ),
    (
        10,
        NULL,
        'DEPOSIT',
        30000.00,
        'KES',
        '2024-06-15 09:30:00+03'
    ),
    (
        11,
        NULL,
        'DEPOSIT',
        10000.00,
        'KES',
        '2024-05-25 11:15:00+03'
    ),
    (
        11,
        NULL,
        'WITHDRAWAL',
        2199.50,
        'KES',
        '2024-07-20 16:00:00+03'
    ),
    -- Member 6 (Sarah Cherotich) - Account 12 (SHARES)
    (
        12,
        NULL,
        'DEPOSIT',
        50000.00,
        'KES',
        '2024-06-25 10:30:00+03'
    ),
    (
        12,
        NULL,
        'DEPOSIT',
        60000.00,
        'KES',
        '2024-08-01 14:00:00+03'
    ),
    -- Member 7 (John Mutua) - Account 13 (SHARES), Account 14 (SAVINGS)
    (
        13,
        NULL,
        'DEPOSIT',
        45000.00,
        'KES',
        '2024-07-05 14:20:00+03'
    ),
    (
        13,
        NULL,
        'DEPOSIT',
        50000.00,
        'KES',
        '2024-09-10 11:15:00+03'
    ),
    (
        14,
        NULL,
        'DEPOSIT',
        20000.00,
        'KES',
        '2024-07-12 09:00:00+03'
    ),
    (
        14,
        NULL,
        'WITHDRAWAL',
        5000.00,
        'KES',
        '2024-10-05 15:30:00+03'
    ),
    -- Member 8 (Fatma Ali) - Account 15 (SHARES)
    (
        15,
        NULL,
        'DEPOSIT',
        100000.00,
        'KES',
        '2024-08-15 09:30:00+03'
    ),
    (
        15,
        NULL,
        'DEPOSIT',
        100000.00,
        'KES',
        '2024-11-20 13:45:00+03'
    ),
    -- Member 9 (Emmanuel Wekesa) - Account 16 (SHARES)
    (
        16,
        NULL,
        'DEPOSIT',
        20000.00,
        'KES',
        '2024-09-06 11:00:00+03'
    ),
    (
        16,
        NULL,
        'DEPOSIT',
        25000.00,
        'KES',
        '2024-12-01 10:15:00+03'
    ),
    -- Member 10 (Mary Atieno) - Account 17 (SHARES), Account 18 (SAVINGS)
    (
        17,
        NULL,
        'DEPOSIT',
        60000.00,
        'KES',
        '2024-10-25 15:30:00+03'
    ),
    (
        17,
        NULL,
        'DEPOSIT',
        70000.00,
        'KES',
        '2024-12-15 09:00:00+03'
    ),
    (
        18,
        NULL,
        'DEPOSIT',
        5000.00,
        'KES',
        '2024-11-01 10:15:00+03'
    ),
    (
        18,
        NULL,
        'WITHDRAWAL',
        1600.00,
        'KES',
        '2025-01-10 14:20:00+03'
    ),
    -- Member 11 (Samuel Kamau) - Account 19 (SHARES)
    (
        19,
        NULL,
        'DEPOSIT',
        35000.00,
        'KES',
        '2024-12-01 09:00:00+03'
    ),
    (
        19,
        NULL,
        'DEPOSIT',
        40000.00,
        'KES',
        '2025-01-15 11:00:00+03'
    ),
    -- Member 12 (Joy Chepngetich) - Account 20 (SHARES), Account 21 (SAVINGS)
    (
        20,
        NULL,
        'DEPOSIT',
        30000.00,
        'KES',
        '2024-12-15 14:00:00+03'
    ),
    (
        20,
        NULL,
        'DEPOSIT',
        30000.00,
        'KES',
        '2025-01-20 09:45:00+03'
    ),
    (
        21,
        NULL,
        'DEPOSIT',
        10000.00,
        'KES',
        '2024-12-20 16:30:00+03'
    ),
    (
        21,
        NULL,
        'WITHDRAWAL',
        8000.00,
        'KES',
        '2025-02-05 13:15:00+03'
    ),
    -- Member 13 (Peter Njoroge) - Account 22 (SHARES)
    (
        22,
        NULL,
        'DEPOSIT',
        250000.00,
        'KES',
        '2025-01-10 10:00:00+03'
    ),
    (
        22,
        NULL,
        'DEPOSIT',
        250000.00,
        'KES',
        '2025-02-28 16:30:00+03'
    ),
    -- Member 14 (Lucy Mwari) - Account 23 (SHARES)
    (
        23,
        NULL,
        'DEPOSIT',
        55000.00,
        'KES',
        '2025-02-15 11:30:00+03'
    ),
    (
        23,
        NULL,
        'DEPOSIT',
        60000.00,
        'KES',
        '2025-03-05 10:00:00+03'
    ),
    -- Member 15 (Brian Otieno) - Account 24 (SHARES)
    (
        24,
        NULL,
        'DEPOSIT',
        20000.00,
        'KES',
        '2025-03-02 09:45:00+03'
    ),
    (
        24,
        NULL,
        'DEPOSIT',
        20000.00,
        'KES',
        '2025-03-08 14:00:00+03'
    );

-- 4. SEED LOANS REGISTER DATA
-- Records historical credit facility approvals with precise remaining interest balances calculated AFTER ledger payments.
INSERT INTO
    loans (
        member_id,
        principal_amount,
        interest_rate,
        interest_owed,
        loan_status,
        currency,
        applied_at,
        disbursed_at
    )
VALUES
    -- Member 1 (Naftal Mambo) - Paid 15,000 KSh against initial 19,800 interest pool
    (
        1,
        165000.00,
        12.00,
        4800.00,
        -- 🌟 19,800 - 15,000 = 4,800.00 KSh remaining interest owed
        'ACTIVE',
        'KES',
        '2024-03-20 09:00:00+03',
        '2024-06-10 14:00:00+03'
    ),
    -- Member 4 (Grace Wambui) - Paid a combined 35,000 KSh (15,000 + 20,000) against initial 41,325 interest pool
    (
        4,
        285000.00,
        14.50,
        6325.00,
        -- 41,325 - 35,000 = 6,325.00 KSh remaining interest owed
        'ACTIVE',
        'KES',
        '2024-08-19 15:45:00+03',
        '2024-08-20 11:30:00+03'
    ),
    -- Member 7 (John Mutua) - Pending Approval (No payments made)
    (
        7,
        50000.00,
        13.00,
        0.00,
        'PENDING APPROVAL',
        'KES',
        '2024-11-05 10:15:00+03',
        NULL
    ),
    -- Member 10 (Mary Atieno) - Pending Approval (No payments made)
    (
        10,
        120000.00,
        13.50,
        0.00,
        'PENDING APPROVAL',
        'KES',
        '2025-01-20 11:00:00+03',
        NULL
    );