# 🏦 Core Banking Ledger Engine

A multi-tier financial backend engineered in Java and PostgreSQL to automate ledger tracking, account administration, and transactional data integrity.

---

### 🖥️ Overview & Problem Solved

Traditional financial tracking methods like manual ledger logs or basic spreadsheets introduce massive risks, including floating-point calculation rounding drift, human entry errors, and data auditing lags.

This core engine scales a relational database architecture into a structured Java application layer. By introducing strict programmatic input validation alongside robust database table constraints, the system guarantees that all financial records are checked, formatted, and verified before hitting persistent disk storage.

---

## 🛠️ Tech Stack & Development Environment

- **Languages & Core:** Java, SQL
- **Database Engine:** PostgreSQL
- **Connectivity:** JDBC Driver Bridges
- **Environment & Tooling:** Linux Ubuntu, Maven Build Engine, VS Code, Git/GitHub, Terminal

---

## 🛠️ Active Development Status & Roadmap

This backend system is actively being developed and scaled using a structured, multi-phase engineering approach:

- **Phase 1: Relational Data Architecture (Completed):** Designed core database schemas, constraints, domain rules, and B-Tree indexing layers in PostgreSQL.
- **Phase 2: Core Java Application Layer (Current Focus):** Building out object-oriented domain models, JDBC connection, and repository logic classes.
- **Phase 3: Service Migration (Upcoming):** Transitioning core operations into the Spring Boot framework to build scalable enterprise REST APIs.

---

## 🗂️ Project Directory Structure

```text
~/backend-projects/sacco-management-backend/
├── pom.xml                     # Maven project configuration file
├── README.md                   # Project documentation
├── LICENSE                     # Open-source MIT License terms
├── hs_err_pid3842.log          # JVM crash error log
├── images/                     # Database layout and report screenshots
│   ├── balancesheetquery.png
│   ├── membertable.png
│   └── tables.png
├── schema/                     # Database table structures
│   ├── 01_members.sql
│   ├── 02_accounts.sql
│   ├── 03_transactions.sql
│   ├── 04_loans.sql
│   └── 05_index.sql
├── src/
│   ├── main/java/com/sacco/
│   │   ├── Main.java           # Application entry point
│   │   ├── database/           # Handles JDBC configurations and connections
│   │   │   └── DatabaseManager.java
│   │   ├── domain/             # Core domain models
│   │   │   ├── LedgerTransaction.java
│   │   │   ├── Loan.java
│   │   │   ├── SaccoAccount.java
│   │   │   └── SaccoMember.java
│   │   └── repository/         # Query mapping interfaces
│   │       ├── AccountRepository.java
│   │       ├── LoanRepository.java
│   │       ├── MemberRepository.java
│   │       └── TransactionRepository.java
│   └── test/java/com/sacco/
│       └── PlacehoderTest.java # Unit testing layer placeholder
├── seeding/                    # Initial database seed records
│   └── seed_data.sql
└── queries/                    # Analytical SQL scripts
    └── analytics.sql
```

---
