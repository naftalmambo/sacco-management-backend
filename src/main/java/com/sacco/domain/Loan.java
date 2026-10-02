package com.sacco.domain;

import java.time.LocalDateTime;

public class Loan {
    private long loanId;
    private long memberId;
    private long principleAmountInCents;
    private double interestRate;
    private long interestOwedInCents;
    private String loanStatus;
    private LocalDateTime appliedAt;
    private LocalDateTime disbursedAt;

    public Loan(long memberId, long principleAmountInCents, double interestRate) {
        validateLoanData(principleAmountInCents, interestRate);
        this.loanId = 0;
        this.memberId = memberId;
        this.principleAmountInCents = principleAmountInCents;
        this.interestRate = interestRate;
        this.interestOwedInCents = 0;
        this.loanStatus = "INITIALIZED";
        this.appliedAt = LocalDateTime.now();
        this.disbursedAt = null;

    }

    public long getLoanId() {
        return this.loanId;
    }

    public long getMemberId() {
        return this.memberId;
    }

    public long getPrincipleAmountInCents() {
        return this.principleAmountInCents;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public long getInterestOwedInCents() {
        return this.interestOwedInCents;
    }

    public String getLoanStatus() {
        return this.loanStatus;
    }

    public LocalDateTime getAppliedAt() {
        return this.appliedAt;
    }

    public LocalDateTime getDisbursedAt() {
        return this.disbursedAt;
    }

    private void validateLoanData(long principleAmountInCents, double interestRate) {
        if (principleAmountInCents <= 0 || interestRate <= 0) {
            throw new IllegalArgumentException(
                    "Loan principal amount and interest rate must be strictly greater than zero");
        }
    }

    public void applyForLoan() {
        if (!this.loanStatus.equals("INITIALIZED")) {
            throw new IllegalStateException("Cannot apply for a loan that has already been processed");
        }
        this.loanStatus = "PENDING APPROVAL";
        this.appliedAt = LocalDateTime.now();
    }

    public void disburseLoan() {
        if (!(this.loanStatus.equals("PENDING APPROVAL"))) {
            throw new IllegalStateException("Only loans with PENDING APPROVAL status can be disbursed");
        }

        double calculatedInterest = this.principleAmountInCents * (this.interestRate / 100.0);
        this.interestOwedInCents = (long) calculatedInterest;

        this.loanStatus = "ACTIVE";
        this.disbursedAt = LocalDateTime.now();
    }

    public void repayLoan(LedgerTransaction transaction) {
        if (!this.loanStatus.equals("ACTIVE")) {
            throw new IllegalStateException("Loan is not active for repayments");
        }

        if (!transaction.getTransactionType().equals("LOAN REPAYMENT")) {
            throw new IllegalArgumentException("Invalid transaction type for loan repayment");
        }

        long paymentAmount = transaction.getAmountInCents();

        if (this.interestOwedInCents > 0) {
            if (paymentAmount >= this.interestOwedInCents) {
                paymentAmount -= this.interestOwedInCents;
                this.interestOwedInCents = 0;
            } else {
                this.interestOwedInCents -= paymentAmount;
                paymentAmount = 0;
            }
        }

        if (paymentAmount > 0) {
            this.principleAmountInCents -= paymentAmount;
        }

        if (this.principleAmountInCents <= 0 && this.interestOwedInCents == 0) {
            this.principleAmountInCents = 0;
            this.loanStatus = "FULLY PAID";
        }
    }

    @Override
    public String toString() {
        long pShillings = this.principleAmountInCents / 100;
        long pCents = this.principleAmountInCents % 100;
        long iShillings = this.interestOwedInCents / 100;
        long iCents = this.interestOwedInCents % 100;

        return "Loan [ID: " + this.loanId + " | Status: " + this.loanStatus +
                " | Principal Owed: " + pShillings + "." + pCents + " KSh" +
                " | Interest Owed: " + iShillings + "." + iCents + " KSh" +
                " | Rate: " + this.interestRate + "%]";
    }
}
