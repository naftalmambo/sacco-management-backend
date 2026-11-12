package com.sacco.service;

import com.sacco.domain.*;
import com.sacco.repository.*;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class SaccoEngineService {

    private final MemberRepository memberRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    private final LoanRepository loanRepo;

    public SaccoEngineService(MemberRepository memberRepo,
            AccountRepository accountRepo,
            TransactionRepository transactionRepo,
            LoanRepository loanRepo) {

        this.memberRepo = memberRepo;
        this.accountRepo = accountRepo;
        this.loanRepo = loanRepo;
        this.transactionRepo = transactionRepo;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public SaccoMember getMemberByNationalId(String nationalId) {
        if (nationalId == null || nationalId.isBlank()) {
            throw new IllegalArgumentException("Search Error: National ID cannot be empty.");
        }

        return memberRepo.findByNationalId(nationalId)
                .orElseThrow(() -> new RuntimeException(
                        "Member Not Found: No record exists with National ID " + nationalId));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SaccoAccount> getAccountsByMemberId(long memberId) {
        return accountRepo.findAllByMemberId(memberId);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public SaccoAccount getAccountByNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Search Error: Account number cannot be empty.");
        }

        return accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Account Not Found: No account exists with number " + accountNumber));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<LedgerTransaction> getRecentTransactions(long accountId) {
        return transactionRepo.findTop10ByAccountIdOrderByCreatedAtDesc(accountId);

    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Loan> getLoansByMemberId(long memberId) {
        return loanRepo.findAllByMemberId(memberId);
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Loan applyForLoan(long memberId, long principalAmountInCents, double interestRate) {
        if (!memberRepo.existsById(memberId)) {
            throw new RuntimeException(
                    "Member Not Found: Cannot apply for loan for non-existent Member ID " + memberId);
        }

        Loan newLoan = new Loan(memberId, principalAmountInCents, interestRate);
        newLoan.applyForLoan();
        return loanRepo.save(newLoan);
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

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void processLoanDisbursement(long loanId) {

        Loan loan = (loanRepo != null) ? loanRepo.findById(loanId).orElse(null) : null;
        if (loan == null) {
            throw new RuntimeException("Credit Error: Loan record with ID " + loanId + " does not exist.");
        }

        if (!"PENDING APPROVAL".equalsIgnoreCase(loan.getLoanStatus())) {
            throw new RuntimeException(
                    "Transaction Aborted: Only PENDING APPROVAL facilities can be disbursed.");
        }

        long memberId = loan.getMemberId();

        SaccoAccount savingsAccount = (accountRepo != null)
                ? accountRepo.findByMemberIdAndAccountType(memberId, "SAVINGS").orElse(null)
                : null;
        SaccoAccount loanAccount = (accountRepo != null)
                ? accountRepo.findByMemberIdAndAccountType(memberId, "LOAN").orElse(null)
                : null;

        if (savingsAccount == null) {
            throw new RuntimeException("Disbursement Failure: Active SAVINGS account tracking record missing.");
        }
        if (loanAccount == null) {
            throw new RuntimeException("Disbursement Failure: Active LOAN balance sheet account missing.");
        }

        loan.disburseLoan();
        long principalAmount = loan.getPrincipleAmountInCents();
        long newSavingsBalance = savingsAccount.getBalanceInCents() + principalAmount;
        long newLoanAccountBalance = loanAccount.getBalanceInCents() + principalAmount;

        if (loanRepo != null) {
            loanRepo.save(loan);
        }
        savingsAccount.setBalanceInCents(newSavingsBalance);
        loanAccount.setBalanceInCents(newLoanAccountBalance);

        if (accountRepo != null) {
            accountRepo.save(savingsAccount);
            accountRepo.save(loanAccount);
        }

        if (transactionRepo != null) {
            LedgerTransaction loanTx = new LedgerTransaction(loanAccount.getAccountId(), "LOAN_DISBURSEMENT",
                    principalAmount);
            transactionRepo.save(loanTx);

            LedgerTransaction savingsTx = new LedgerTransaction(savingsAccount.getAccountId(), "DEPOSIT",
                    principalAmount);
            transactionRepo.save(savingsTx);
        }

        System.out.println("LOAN FACILITY ONLINE: " + (principalAmount / 100.0)
                + " KSh disbursed directly to Savings Account No: " + savingsAccount.getAccountNumber());
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void processLoanRepayment(long loanId, long amountInCents, boolean isCashPayment) {
        if (amountInCents <= 0) {
            System.out.println("TRANSACTION BLOCKED: Repayment amount must be greater than zero.");
            return;
        }

        Loan loan = (loanRepo != null) ? loanRepo.findById(loanId).orElse(null) : null;

        if (loan == null) {
            throw new RuntimeException("Credit Error: Target Loan ID " + loanId + " does not exist.");
        }

        if (!"ACTIVE".equalsIgnoreCase(loan.getLoanStatus())) {
            throw new RuntimeException(
                    "Transaction Blocked: Repayments can only be processed on ACTIVE facilities.");
        }

        long memberId = loan.getMemberId();
        SaccoAccount savingsAccount = null;

        if (!isCashPayment) {
            savingsAccount = (accountRepo != null)
                    ? accountRepo.findByMemberIdAndAccountType(memberId, "SAVINGS").orElse(null)
                    : null;

            if (savingsAccount == null) {
                throw new RuntimeException(
                        "Repayment Aborted: Destination SAVINGS account tracking record missing.");
            }

            long currentSavingsBalance = savingsAccount.getBalanceInCents();
            long newSavingsBalance = currentSavingsBalance - amountInCents;
            long minimumFloorLimit = 100000L;

            if (newSavingsBalance < minimumFloorLimit) {
                throw new RuntimeException(
                        "Insufficient Liquidity: Savings transfer denied. Account must retain a floor of 1,000.00 KSh.");
            }

            savingsAccount.setBalanceInCents(newSavingsBalance);
            if (accountRepo != null) {
                accountRepo.save(savingsAccount);
            }
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

        String newLoanStatus = (newPrincipalOwed == 0 && newInterestOwed == 0) ? "FULLY PAID" : "ACTIVE";

        loan.setLoanStatus(newLoanStatus);
        loan.setPrincipleAmountInCents(newPrincipalOwed);
        loan.setInterestOwedInCents(newInterestOwed);

        if (loanRepo != null) {
            loanRepo.save(loan);
        }

        SaccoAccount loanAccount = (accountRepo != null)
                ? accountRepo.findByMemberIdAndAccountType(memberId, "LOAN").orElse(null)
                : null;

        if (loanAccount == null) {
            throw new RuntimeException("Repayment Aborted: Active LOAN balance sheet account missing.");
        }

        long newLoanAccountBalance = loanAccount.getBalanceInCents() - amountInCents;

        loanAccount.setBalanceInCents(newLoanAccountBalance);
        if (accountRepo != null) {
            accountRepo.save(loanAccount);
        }

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

        System.out.println("REPAYMENT PROCESSED SUCCESSFULLY: " + (amountInCents / 100.0)
                + " KSh applied to Loan ID " + loanId + " (Status: " + newLoanStatus + ")");
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public SaccoAccount createAccount(long memberId, String accountType) {
        if (accountType == null || accountType.isBlank()) {
            throw new IllegalArgumentException("Validation Error: Account type cannot be empty.");
        }

        String normalizedType = accountType.trim().toUpperCase();
        if (!normalizedType.equals("SAVINGS") && !normalizedType.equals("SHARES") && !normalizedType.equals("LOAN")) {
            throw new IllegalArgumentException("Validation Error: Account type must be SAVINGS, SHARES, or LOAN.");
        }

        if (!memberRepo.existsById(memberId)) {
            throw new RuntimeException("Member Not Found: Cannot open account for non-existent Member ID " + memberId);
        }

        if (accountRepo.findByMemberIdAndAccountType(memberId, normalizedType).isPresent()) {
            throw new RuntimeException(
                    "Duplicate Account: Member ID " + memberId + " already holds a " + normalizedType + " account.");
        }

        String accountNumber = generateUniqueAccountNumber();
        SaccoAccount newAccount = new SaccoAccount(memberId, accountNumber, normalizedType);
        return accountRepo.save(newAccount);
    }

    private String generateUniqueAccountNumber() {
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();
        String candidate;

        do {
            long suffix = random.nextLong(100_000_000L, 1_000_000_000L);
            candidate = "128" + suffix;

        } while (accountRepo.findByAccountNumber(candidate).isPresent());
        return candidate;
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public SaccoMember registerMember(String firstName, String lastName, java.time.LocalDate dateOfBirth,
            String sex, String emailAddress, String phoneNumber, String nationalId, String county) {

        SaccoMember newMember = new SaccoMember(firstName, lastName, dateOfBirth, sex, emailAddress, phoneNumber,
                nationalId, county);

        if (memberRepo.existsByNationalId(newMember.getNationalId())) {
            throw new RuntimeException(
                    "Duplicate Member: National ID " + newMember.getNationalId() + " is already registered.");
        }
        if (memberRepo.existsByEmailAddress(newMember.getEmailAddress())) {
            throw new RuntimeException(
                    "Duplicate Member: Email " + newMember.getEmailAddress() + " is already registered.");
        }
        if (memberRepo.existsByPhoneNumber(newMember.getPhoneNumber())) {
            throw new RuntimeException(
                    "Duplicate Member: Phone number " + newMember.getPhoneNumber() + " is already registered.");
        }

        return memberRepo.save(newMember);
    }

}
