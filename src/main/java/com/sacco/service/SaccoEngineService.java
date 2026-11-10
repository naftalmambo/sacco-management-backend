package com.sacco.service;

import com.sacco.domain.*;
import com.sacco.repository.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaccoEngineService {

    private MemberRepository memberRepo;
    private AccountRepository accountRepo;
    private TransactionRepository transactionRepo;
    private LoanRepository loanRepo;

    public SaccoEngineService() {
        this.memberRepo = null;
        this.accountRepo = null;
        this.loanRepo = null;
        this.transactionRepo = null;
    }

    public void runMemberAuditReport(long memberId) {
        SaccoMember member = (memberRepo != null) ? memberRepo.findById(memberId).orElse(null) : null;
        if (member == null) {
            System.out.println("CRITICAL ERROR: Member record with ID " + memberId + " missing from disk.");
            return;
        }

        System.out.println("\n[MEMBER IDENTITY DUMP]");
        System.out.println(member);

        System.out.println("\n[FINANCIAL ACCOUNT MANAGEMENT SYSTEM]");
        List<SaccoAccount> activeAccounts = (accountRepo != null) ? accountRepo.findAllByMemberId(memberId)
                : new ArrayList<>();

        if (activeAccounts.isEmpty()) {
            System.out.println("No active financial accounts registered for this member.");
        } else {
            for (SaccoAccount account : activeAccounts) {
                System.out.println(account);

                List<LedgerTransaction> recentLedgerEntries = (transactionRepo != null)
                        ? transactionRepo.findTop10ByAccountIdOrderByCreatedAtDesc(account.getAccountId())
                        : new java.util.ArrayList<>();

                for (LedgerTransaction entry : recentLedgerEntries) {
                    System.out.println("   └─ " + entry);
                }
            }
        }

        System.out.println("\n[CREDIT REGISTRY ANALYSIS]");
        List<Loan> memberLoans = (loanRepo != null) ? loanRepo.findAllByMemberId(memberId) : new ArrayList<>();

        if (memberLoans.isEmpty()) {
            System.out.println("No credit facility logs found for this member.");
        } else {
            for (Loan creditRecord : memberLoans) {
                System.out.println(creditRecord);
            }
        }
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void processCashWithdrawal(long accountId, long amountInCents) {
        if (amountInCents <= 0) {
            System.out.println("TRANSACTION BLOCKED: Withdrawal amount must be greater than zero.");
            return;
        }

        SaccoAccount account = (accountRepo != null) ? accountRepo.findById(accountId).orElse(null) : null;

        if (account == null) {
            throw new RuntimeException(
                    "Security Alert: Target Account ID " + accountId + " does not exist on disk.");
        }
        if ("SHARES".equalsIgnoreCase(account.getAccountType())
                || "LOAN".equalsIgnoreCase(account.getAccountType())) {
            throw new RuntimeException(
                    "Transaction Violation: Direct cash withdrawals are strictly restricted to SAVINGS accounts.");
        }

        long currentBalance = account.getBalanceInCents();
        long newBalance = currentBalance - amountInCents;

        long minimumFloorLimit = 100000L;

        if (newBalance < minimumFloorLimit) {
            throw new RuntimeException(
                    "Insufficient Liquidity: Request denied. Savings account must maintain a baseline floor of 1,000.00 KSh.");
        }

        account.setBalanceInCents(newBalance);
        accountRepo.save(account);

        if (transactionRepo != null) {
            LedgerTransaction tx = new LedgerTransaction(accountId, "WITHDRAWAL", amountInCents);
            transactionRepo.save(tx);

        }
        System.out.println("TRANSACTION SUCCESSFUL: " + (amountInCents / 100.0)
                + " KSh withdrawn from Account ID " + accountId);
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void processCashDeposit(long accountId, long amountInCents) {
        if (amountInCents <= 0) {
            System.out.println("TRANSACTION BLOCKED: Deposit amount must be greater than zero.");
            return;
        }

        SaccoAccount account = (accountRepo != null) ? accountRepo.findById(accountId).orElse(null) : null;

        if (account == null) {
            throw new RuntimeException(
                    "Security Alert: Target Account ID " + accountId + " does not exist on disk.");
        }

        if (!"SAVINGS".equalsIgnoreCase(account.getAccountType())) {
            throw new RuntimeException(
                    "Transaction Violation: Counter cash deposits are strictly restricted to SAVINGS accounts.");
        }

        if (!"ACTIVE".equalsIgnoreCase(account.getAccountStatus())) {
            throw new RuntimeException(
                    "Account Deactivated: Transaction blocked. Target account status is: "
                            + account.getAccountStatus());
        }

        long currentBalance = account.getBalanceInCents();
        long newBalance = currentBalance + amountInCents;

        account.setBalanceInCents(newBalance);
        accountRepo.save(account);

        if (transactionRepo != null) {

            LedgerTransaction tx = new LedgerTransaction(accountId, "DEPOSIT", amountInCents);
            transactionRepo.save(tx);
        }

        System.out.println("TRANSACTION SUCCESSFUL: " + (amountInCents / 100.0)
                + " KSh deposited to Account ID " + accountId);
    }

    public void processLoanDisbursement(long loanId) {
        try (Connection conn = com.sacco.database.DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try {
                Loan loan = null;
                String lookupLoanSql = "SELECT * FROM loans WHERE loan_id = ?;";

                try (PreparedStatement stmt = conn.prepareStatement(lookupLoanSql)) {
                    stmt.setLong(1, loanId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            java.math.BigDecimal dbPrincipal = rs.getBigDecimal("principal_amount");
                            long principalInCents = dbPrincipal.movePointRight(2).longValue();
                            java.math.BigDecimal dbInterest = rs.getBigDecimal("interest_owed");
                            long interestOwedInCents = dbInterest.movePointRight(2).longValue();

                            loan = new Loan(
                                    rs.getLong("loan_id"),
                                    rs.getLong("member_id"),
                                    principalInCents,
                                    rs.getDouble("interest_rate"),
                                    interestOwedInCents,
                                    rs.getString("loan_status"),
                                    rs.getTimestamp("applied_at").toLocalDateTime(),
                                    rs.getTimestamp("disbursed_at") != null
                                            ? rs.getTimestamp("disbursed_at").toLocalDateTime()
                                            : null);
                        }
                    }
                }

                if (loan == null) {
                    throw new SQLException("Credit Error: Loan record with ID " + loanId + " does not exist.");
                }

                if (!"PENDING APPROVAL".equalsIgnoreCase(loan.getLoanStatus())) {
                    throw new SQLException("Transaction Aborted: Only PENDING APPROVAL facilities can be disbursed.");
                }

                long memberId = loan.getMemberId();

                SaccoAccount savingsAccount = accountRepo.findSavingsAccountByMemberId(conn, memberId);
                SaccoAccount loanAccount = accountRepo.findLoanAccountByMemberId(conn, memberId);

                if (savingsAccount == null) {
                    throw new SQLException("Disbursement Failure: Active SAVINGS account tracking record missing.");
                }
                if (loanAccount == null) {
                    throw new SQLException("Disbursement Failure: Active LOAN balance sheet account missing.");
                }

                loan.disburseLoan();
                long principalAmount = loan.getPrincipleAmountInCents();
                long newSavingsBalance = savingsAccount.getBalanceInCents() + principalAmount;
                long newLoanAccountBalance = loanAccount.getBalanceInCents() + principalAmount;

                String updateLoanSql = "UPDATE loans SET loan_status = 'ACTIVE', interest_owed = ?, disbursed_at = CURRENT_TIMESTAMP WHERE loan_id = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(updateLoanSql)) {
                    stmt.setBigDecimal(1, java.math.BigDecimal.valueOf(loan.getInterestOwedInCents(), 2));
                    stmt.setLong(2, loanId);
                    stmt.executeUpdate();
                }

                accountRepo.updateBalance(conn, savingsAccount.getAccountId(), newSavingsBalance);
                accountRepo.updateBalance(conn, loanAccount.getAccountId(), newLoanAccountBalance);

                if (transactionRepo != null) {
                    LedgerTransaction loanTx = new LedgerTransaction(loanAccount.getAccountId(), "LOAN_DISBURSEMENT",
                            principalAmount);
                    transactionRepo.save(loanTx);

                    LedgerTransaction savingsTx = new LedgerTransaction(savingsAccount.getAccountId(), "DEPOSIT",
                            principalAmount);
                    transactionRepo.save(savingsTx);
                }

                conn.commit();
                System.out.println("LOAN FACILITY ONLINE: " + (principalAmount / 100.0)
                        + " KSh disbursed directly to Savings Account No: " + savingsAccount.getAccountNumber());

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("🚨 CREDIT TRANSACTION ROLLBACK TRIGGERED: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Critical Credit Engine Core Crash Exception: " + e.getMessage());
        }
    }

    public void processLoanRepayment(long loanId, long amountInCents, boolean isCashPayment) {
        if (amountInCents <= 0) {
            System.out.println("TRANSACTION BLOCKED: Repayment amount must be greater than zero.");
            return;
        }

        try (Connection conn = com.sacco.database.DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // 🔒 Freeze database states on disk

            try {
                Loan loan = null;
                String lookupLoanSql = "SELECT * FROM loans WHERE loan_id = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(lookupLoanSql)) {
                    stmt.setLong(1, loanId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            java.math.BigDecimal dbPrincipal = rs.getBigDecimal("principal_amount");
                            long principalInCents = dbPrincipal.movePointRight(2).longValue();
                            java.math.BigDecimal dbInterest = rs.getBigDecimal("interest_owed");
                            long interestOwedInCents = dbInterest.movePointRight(2).longValue();

                            loan = new Loan(
                                    rs.getLong("loan_id"),
                                    rs.getLong("member_id"),
                                    principalInCents,
                                    rs.getDouble("interest_rate"),
                                    interestOwedInCents,
                                    rs.getString("loan_status"),
                                    rs.getTimestamp("applied_at").toLocalDateTime(),
                                    rs.getTimestamp("disbursed_at") != null
                                            ? rs.getTimestamp("disbursed_at").toLocalDateTime()
                                            : null);
                        }
                    }
                }

                if (loan == null) {
                    throw new SQLException("Credit Error: Target Loan ID " + loanId + " does not exist.");
                }

                if (!"ACTIVE".equalsIgnoreCase(loan.getLoanStatus())) {
                    throw new SQLException(
                            "Transaction Blocked: Repayments can only be processed on ACTIVE facilities.");
                }

                long memberId = loan.getMemberId();
                SaccoAccount savingsAccount = null;

                if (!isCashPayment) {
                    savingsAccount = accountRepo.findSavingsAccountByMemberId(conn, memberId);
                    if (savingsAccount == null) {
                        throw new SQLException(
                                "Repayment Aborted: Destination SAVINGS account tracking record missing.");
                    }

                    long currentSavingsBalance = savingsAccount.getBalanceInCents();
                    long newSavingsBalance = currentSavingsBalance - amountInCents;
                    long minimumFloorLimit = 100000L;

                    if (newSavingsBalance < minimumFloorLimit) {
                        throw new SQLException(
                                "Insufficient Liquidity: Savings transfer denied. Account must retain a floor of 1,000.00 KSh.");
                    }

                    accountRepo.updateBalance(conn, savingsAccount.getAccountId(), newSavingsBalance);
                }

                long remainingPayment = amountInCents;
                long currentInterestOwed = loan.getInterestOwedInCents();
                long currentPrincipalOwed = loan.getPrincipleAmountInCents();

                long newInterestOwed = currentInterestOwed;
                long newPrincipalOwed = currentPrincipalOwed;

                if (remainingPayment >= currentInterestOwed) {
                    remainingPayment -= currentInterestOwed;
                    newInterestOwed = 0;

                    newPrincipalOwed = currentPrincipalOwed - remainingPayment;
                    if (newPrincipalOwed < 0) {
                        newPrincipalOwed = 0;
                    }
                } else {
                    newInterestOwed = currentInterestOwed - remainingPayment;
                }

                String newLoanStatus = (newPrincipalOwed == 0 && newInterestOwed == 0) ? "SETTLED" : "ACTIVE";

                String updateLoanSql = "UPDATE loans SET loan_status = ?, principal_amount = ?, interest_owed = ? WHERE loan_id = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(updateLoanSql)) {
                    stmt.setString(1, newLoanStatus);
                    stmt.setBigDecimal(2, java.math.BigDecimal.valueOf(newPrincipalOwed, 2));
                    stmt.setBigDecimal(3, java.math.BigDecimal.valueOf(newInterestOwed, 2));
                    stmt.setLong(4, loanId);
                    stmt.executeUpdate();
                }

                SaccoAccount loanAccount = accountRepo.findLoanAccountByMemberId(conn, memberId);
                if (loanAccount == null) {
                    throw new SQLException("Repayment Aborted: Active LOAN balance sheet account missing.");
                }

                long newLoanAccountBalance = loanAccount.getBalanceInCents() - amountInCents;
                accountRepo.updateBalance(conn, loanAccount.getAccountId(), newLoanAccountBalance);
                if (transactionRepo != null) {
                    LedgerTransaction loanRepayTx = new LedgerTransaction(loanAccount.getAccountId(), "LOAN_REPAYMENT",
                            amountInCents);
                    transactionRepo.save(loanRepayTx);
                }

                if (!isCashPayment && transactionRepo != null) {
                    LedgerTransaction savingsWithdrawTx = new LedgerTransaction(savingsAccount.getAccountId(),
                            "WITHDRAWAL", amountInCents);
                    transactionRepo.save(savingsWithdrawTx);
                }

                conn.commit();
                System.out.println("REPAYMENT PROCESSED SUCCESSFULLY: " + (amountInCents / 100.0)
                        + " KSh applied to Loan ID " + loanId + " (Status: " + newLoanStatus + ")");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("🚨 CREDIT REPAYMENT ROLLBACK TRIGGERED: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (

        SQLException e) {
            System.out.println("Critical Credit Engine Core Repayment Exception: " + e.getMessage());
        }
    }
}
