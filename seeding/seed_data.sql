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