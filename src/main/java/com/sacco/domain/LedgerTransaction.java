package com.sacco.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")

public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Column(name = "transaction_id")
    private long transactionId;

    @jakarta.persistence.Column(name = "account_id")
    private long accountId;

    @jakarta.persistence.Column(name = "transaction_type")
    private String transactionType;

    @jakarta.persistence.Column(name = "amount")
    private long amountInCents;

    @jakarta.persistence.Column(name = "created_at")
    private LocalDateTime createdAt;

    protected LedgerTransaction() {
    }

    public LedgerTransaction(long accountId, String transactionType, long amountInCents) {
        validateTransaction(transactionType, amountInCents);
        this.transactionId = 0;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amountInCents = amountInCents;
        this.createdAt = LocalDateTime.now();
    }

    public LedgerTransaction(long transactionId, long accountId, String transactionType, long amountInCents,
            LocalDateTime createdAt) {
        validateTransaction(transactionType, amountInCents);
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amountInCents = amountInCents;
        this.createdAt = createdAt;
    }

    public long getTransactionId() {
        return this.transactionId;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public long getAmountInCents() {
        return this.amountInCents;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    private void validateTransaction(String transactionType, long amountInCents) {
        if (!(transactionType.equals("DEPOSIT")) &&
                !(transactionType.equals("WITHDRAWAL")) &&
                !(transactionType.equals("LOAN_REPAYMENT")) &&
                !(transactionType.equals("LOAN REPAYMENT")) &&
                !(transactionType.equals("LOAN_DISBURSEMENT")) &&
                !(transactionType.equals("LOAN DISBURSEMENT"))) {
            throw new IllegalArgumentException("Invalid Transaction Type: " + transactionType);
        }

        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    @Override
    public String toString() {
        long shillings = this.amountInCents / 100;
        long cents = this.amountInCents % 100;

        return "Transaction [ID: " + this.transactionId + " | Account ID: " + this.accountId + " | Type: "
                + this.transactionType + " | Amount: " + shillings + "." + cents + " KSh | Date: " + this.createdAt
                + "]";
    }
}
