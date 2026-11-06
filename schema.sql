--
-- PostgreSQL database dump
--

\restrict MV3c5fBXbdQUMdOATl42Gmbo3QK1wniraoKJWc63I0JyMflWAxTa2GLP8F5AEt0

-- Dumped from database version 14.24 (Ubuntu 14.24-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.24 (Ubuntu 14.24-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: accounts; Type: TABLE; Schema: public; Owner: sacco_admin
--

CREATE TABLE public.accounts (
    account_id bigint NOT NULL,
    member_id bigint NOT NULL,
    account_number character varying(30) NOT NULL,
    account_type text NOT NULL,
    balance numeric(15,2) DEFAULT 0.00 NOT NULL,
    currency character varying(3) DEFAULT 'KES'::character varying NOT NULL,
    account_status character varying(15) DEFAULT 'ACTIVE'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_acc_type CHECK ((account_type = ANY (ARRAY['SAVINGS'::text, 'SHARES'::text, 'LOAN'::text]))),
    CONSTRAINT chk_account_balance CHECK ((balance >= (0)::numeric)),
    CONSTRAINT chk_account_currency CHECK (((currency)::text = 'KES'::text)),
    CONSTRAINT chk_account_status CHECK (((account_status)::text = ANY (ARRAY[('ACTIVE'::character varying)::text, ('FROZEN'::character varying)::text, ('SUSPENDED'::character varying)::text])))
);


ALTER TABLE public.accounts OWNER TO sacco_admin;

--
-- Name: accounts_account_id_seq; Type: SEQUENCE; Schema: public; Owner: sacco_admin
--

CREATE SEQUENCE public.accounts_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.accounts_account_id_seq OWNER TO sacco_admin;

--
-- Name: accounts_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sacco_admin
--

ALTER SEQUENCE public.accounts_account_id_seq OWNED BY public.accounts.account_id;


--
-- Name: loans; Type: TABLE; Schema: public; Owner: sacco_admin
--

CREATE TABLE public.loans (
    loan_id bigint NOT NULL,
    member_id bigint NOT NULL,
    principal_amount numeric(15,2) NOT NULL,
    interest_rate numeric(15,2) NOT NULL,
    interest_owed bigint DEFAULT 0 NOT NULL,
    loan_status text DEFAULT 'PENDING APPROVAL'::text NOT NULL,
    currency character varying(3) DEFAULT 'KES'::character varying NOT NULL,
    applied_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    disbursed_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_interest_rt CHECK ((interest_rate > (0)::numeric)),
    CONSTRAINT chk_loan_currency CHECK (((currency)::text = 'KES'::text)),
    CONSTRAINT chk_loan_status CHECK ((loan_status = ANY (ARRAY['PENDING APPROVAL'::text, 'ACTIVE'::text, 'FULLY PAID'::text, 'DEFAULTED'::text]))),
    CONSTRAINT chk_princ_amt CHECK ((principal_amount > (0)::numeric))
);


ALTER TABLE public.loans OWNER TO sacco_admin;

--
-- Name: loans_loan_id_seq; Type: SEQUENCE; Schema: public; Owner: sacco_admin
--

CREATE SEQUENCE public.loans_loan_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.loans_loan_id_seq OWNER TO sacco_admin;

--
-- Name: loans_loan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sacco_admin
--

ALTER SEQUENCE public.loans_loan_id_seq OWNED BY public.loans.loan_id;


--
-- Name: members; Type: TABLE; Schema: public; Owner: sacco_admin
--

CREATE TABLE public.members (
    member_id bigint NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    date_of_birth date NOT NULL,
    sex character varying(10) NOT NULL,
    email_address character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    national_id character varying(20) NOT NULL,
    country character varying(30) DEFAULT 'Kenya'::character varying NOT NULL,
    county character varying(50) NOT NULL,
    joined_date timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_kenyan_phone CHECK (((phone_number)::text ~ '^\+254\d{9}$'::text)),
    CONSTRAINT chk_member_country CHECK (((country)::text = 'Kenya'::text)),
    CONSTRAINT chk_member_sex CHECK (((sex)::text = ANY (ARRAY[('Male'::character varying)::text, ('Female'::character varying)::text])))
);


ALTER TABLE public.members OWNER TO sacco_admin;

--
-- Name: members_member_id_seq; Type: SEQUENCE; Schema: public; Owner: sacco_admin
--

CREATE SEQUENCE public.members_member_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.members_member_id_seq OWNER TO sacco_admin;

--
-- Name: members_member_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sacco_admin
--

ALTER SEQUENCE public.members_member_id_seq OWNED BY public.members.member_id;


--
-- Name: transactions; Type: TABLE; Schema: public; Owner: sacco_admin
--

CREATE TABLE public.transactions (
    transaction_id bigint NOT NULL,
    account_id bigint NOT NULL,
    loan_id bigint,
    transaction_type character varying(20) NOT NULL,
    amount numeric(15,2) NOT NULL,
    currency character varying(3) DEFAULT 'KES'::character varying NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_trans_amount CHECK ((amount > (0)::numeric)),
    CONSTRAINT chk_trans_currency CHECK (((currency)::text = 'KES'::text)),
    CONSTRAINT chk_trans_type CHECK (((transaction_type)::text = ANY ((ARRAY['DEPOSIT'::character varying, 'WITHDRAWAL'::character varying, 'LOAN_REPAYMENT'::character varying, 'LOAN REPAYMENT'::character varying, 'LOAN_DISBURSEMENT'::character varying, 'LOAN DISBURSEMENT'::character varying])::text[])))
);


ALTER TABLE public.transactions OWNER TO sacco_admin;

--
-- Name: transactions_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: sacco_admin
--

CREATE SEQUENCE public.transactions_transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transactions_transaction_id_seq OWNER TO sacco_admin;

--
-- Name: transactions_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sacco_admin
--

ALTER SEQUENCE public.transactions_transaction_id_seq OWNED BY public.transactions.transaction_id;


--
-- Name: accounts account_id; Type: DEFAULT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.accounts ALTER COLUMN account_id SET DEFAULT nextval('public.accounts_account_id_seq'::regclass);


--
-- Name: loans loan_id; Type: DEFAULT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.loans ALTER COLUMN loan_id SET DEFAULT nextval('public.loans_loan_id_seq'::regclass);


--
-- Name: members member_id; Type: DEFAULT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.members ALTER COLUMN member_id SET DEFAULT nextval('public.members_member_id_seq'::regclass);


--
-- Name: transactions transaction_id; Type: DEFAULT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.transactions ALTER COLUMN transaction_id SET DEFAULT nextval('public.transactions_transaction_id_seq'::regclass);


--
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (account_id);


--
-- Name: loans loans_pkey; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.loans
    ADD CONSTRAINT loans_pkey PRIMARY KEY (loan_id);


--
-- Name: members members_pkey; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_pkey PRIMARY KEY (member_id);


--
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id);


--
-- Name: accounts uq_account_number; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT uq_account_number UNIQUE (account_number);


--
-- Name: members uq_member_email; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT uq_member_email UNIQUE (email_address);


--
-- Name: members uq_member_national_id; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT uq_member_national_id UNIQUE (national_id);


--
-- Name: members uq_member_phone; Type: CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT uq_member_phone UNIQUE (phone_number);


--
-- Name: accounts fk_account_member; Type: FK CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT fk_account_member FOREIGN KEY (member_id) REFERENCES public.members(member_id) ON DELETE RESTRICT;


--
-- Name: loans fk_loans_member; Type: FK CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.loans
    ADD CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES public.members(member_id) ON DELETE RESTRICT;


--
-- Name: transactions fk_transaction_account; Type: FK CONSTRAINT; Schema: public; Owner: sacco_admin
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES public.accounts(account_id) ON DELETE RESTRICT;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO sacco_admin;


--
-- PostgreSQL database dump complete
--

\unrestrict MV3c5fBXbdQUMdOATl42Gmbo3QK1wniraoKJWc63I0JyMflWAxTa2GLP8F5AEt0

