-- 1. SEED MEMBERS DATA
-- Includes 15 valid Kenyan profiles with explicit historical joining timelines.
TRUNCATE TABLE transactions,
loans,
accounts,
members RESTART IDENTITY CASCADE;

INSERT INTO
    members (
        first_name,
        last_name,
        date_of_birth,
        sex,
        email_address,
        phone_number,
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
        'naftal.mwangi@saccotech.co.ke',
        '+254712345678',
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
        '+254110223344',
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
        'Kenya',
        'Kisumu',
        '2025-03-01 16:00:00+03'
    );

-- 2. SEED ACCOUNTS DATA
-- Establishes financial assets and credit tracking balances across all 15 member IDs.
INSERT INTO
    accounts (
        member_id,
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
        'SHARES',
        450000.00,
        'KES',
        'ACTIVE',
        '2024-03-09 16:00:00+03'
    ),
    (
        1,
        'SAVINGS',
        25400.50,
        'KES',
        'ACTIVE',
        '2024-03-15 10:00:00+03'
    ),
    (
        1,
        'LOAN',
        150000.00,
        'KES',
        'ACTIVE',
        '2024-06-10 14:00:00+03'
    ),
    -- Outstanding credit debt balance
    -- Member 2 (Amina Omondi) - Joined 2024-02-20
    (
        2,
        'SHARES',
        150000.00,
        'KES',
        'ACTIVE',
        '2024-02-22 09:15:00+03'
    ),
    (
        2,
        'SAVINGS',
        12350.00,
        'KES',
        'ACTIVE',
        '2024-02-25 14:00:00+03'
    ),
    -- Member 3 (David Kiprono) - Joined 2024-03-05
    (
        3,
        'SHARES',
        85000.00,
        'KES',
        'ACTIVE',
        '2024-03-05 11:30:00+03'
    ),
    (
        3,
        'SAVINGS',
        4500.00,
        'KES',
        'ACTIVE',
        '2024-03-10 16:45:00+03'
    ),
    -- Member 4 (Grace Wambui) - Joined 2024-04-12
    (
        4,
        'SHARES',
        320000.00,
        'KES',
        'ACTIVE',
        '2024-04-15 08:30:00+03'
    ),
    (
        4,
        'LOAN',
        250000.00,
        'KES',
        'ACTIVE',
        '2024-08-20 11:30:00+03'
    ),
    -- Outstanding credit debt balance
    -- Member 5 (Michael Onyango) - Joined 2024-05-19
    (
        5,
        'SHARES',
        55000.00,
        'KES',
        'ACTIVE',
        '2024-05-20 10:00:00+03'
    ),
    (
        5,
        'SAVINGS',
        7800.50,
        'KES',
        'FROZEN',
        '2024-05-25 11:15:00+03'
    ),
    -- Frozen account test matrix
    -- Member 6 (Sarah Cherotich) - Joined 2024-06-25
    (
        6,
        'SHARES',
        110000.00,
        'KES',
        'ACTIVE',
        '2024-06-25 10:30:00+03'
    ),
    -- Member 7 (John Mutua) - Joined 2024-07-02
    (
        7,
        'SHARES',
        95000.00,
        'KES',
        'ACTIVE',
        '2024-07-05 14:20:00+03'
    ),
    (
        7,
        'SAVINGS',
        15000.00,
        'KES',
        'ACTIVE',
        '2024-07-12 09:00:00+03'
    ),
    -- Member 8 (Fatma Ali) - Joined 2024-08-14
    (
        8,
        'SHARES',
        200000.00,
        'KES',
        'ACTIVE',
        '2024-08-15 09:30:00+03'
    ),
    -- Member 9 (Emmanuel Wekesa) - Joined 2024-09-05
    (
        9,
        'SHARES',
        45000.00,
        'KES',
        'ACTIVE',
        '2024-09-06 11:00:00+03'
    ),
    -- Member 10 (Mary Atieno) - Joined 2024-10-22
    (
        10,
        'SHARES',
        130000.00,
        'KES',
        'ACTIVE',
        '2024-10-25 15:30:00+03'
    ),
    (
        10,
        'SAVINGS',
        3400.00,
        'KES',
        'ACTIVE',
        '2024-11-01 10:15:00+03'
    ),
    -- Member 11 (Samuel Kamau) - Joined 2024-11-30
    (
        11,
        'SHARES',
        75000.00,
        'KES',
        'ACTIVE',
        '2024-12-01 09:00:00+03'
    ),
    -- Member 12 (Joy Chepngetich) - Joined 2024-12-12
    (
        12,
        'SHARES',
        60000.00,
        'KES',
        'ACTIVE',
        '2024-12-15 14:00:00+03'
    ),
    (
        12,
        'SAVINGS',
        9200.00,
        'KES',
        'SUSPENDED',
        '2024-12-20 16:30:00+03'
    ),
    -- Suspended account test matrix
    -- Member 13 (Peter Njoroge) - Joined 2025-01-08
    (
        13,
        'SHARES',
        500000.00,
        'KES',
        'ACTIVE',
        '2025-01-10 10:00:00+03'
    ),
    -- Member 14 (Lucy Mwari) - Joined 2025-02-14
    (
        14,
        'SHARES',
        115000.00,
        'KES',
        'ACTIVE',
        '2025-02-15 11:30:00+03'
    ),
    -- Member 15 (Brian Otieno) - Joined 2025-03-01
    (
        15,
        'SHARES',
        40000.00,
        'KES',
        'ACTIVE',
        '2025-03-02 09:45:00+03'
    );