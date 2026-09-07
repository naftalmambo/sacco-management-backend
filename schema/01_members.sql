CREATE TABLE members (
    member_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    sex VARCHAR(10) NOT NULL CONSTRAINT chk_member_sex CHECK (sex IN ('Male', 'Female')),
    email_address VARCHAR(100) NOT NULL CONSTRAINT uq_member_email UNIQUE,
    phone_number VARCHAR(30) NOT NULL CONSTRAINT uq_member_phone UNIQUE,
    joined_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(100) NOT NULL
);