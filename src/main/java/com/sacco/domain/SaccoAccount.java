package com.sacco.domain;

import java.time.LocalDateTime;

public class SaccoAccount {

    private long accountId;
    private long memberId;
    private String accountNumber;
    private String accountType;
    private long balanceInCents;
    private String accountStatus;
    private LocalDateTime createdAt;

    public SaccoAccount(long memberId, String accountNumber, String accountType) {
        this.accountId = 0;
        this.memberId = memberId;
        this.accountNumber = accountNumber;
        this.accountType = accountType != null ? accountType.toUpperCase() : "";
        this.balanceInCents = 0;
        this.accountStatus = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public SaccoAccount(long accountId, long memberId, String accountNumber, String accountType, long balanceInCents,
            String accountStatus, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.memberId = memberId;
        this.accountNumber = accountNumber;
        this.accountType = accountType != null ? accountType.toUpperCase() : "";
        this.balanceInCents = balanceInCents;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public long getMemberId() {
        return this.memberId;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public long getBalanceInCents() {
        return this.balanceInCents;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void deposit(long amountInCents) {
        if (amountInCents > 0) {
            this.balanceInCents += amountInCents;
        }
    }

    public void withdraw(long amountInCents) {
        if (!(accountType.equalsIgnoreCase("SAVINGS"))) {
            throw new IllegalArgumentException("Withdrawals are restricted strictly to SAVINGS accounts only");
        }
        if (amountInCents <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if ((this.balanceInCents - amountInCents) < 100000) {
            throw new IllegalArgumentException("Insufficient funds: Account balance cannot drop below 1,000 KSh");
        }
        this.balanceInCents -= amountInCents;
    }

    @Override
    public String toString() {
        long shillings = this.balanceInCents / 100;
        int cents = (int) (Math.abs(this.balanceInCents) % 100);
        return "Account [ID: " + this.accountId +
                ", MemberID: " + this.memberId +
                ", No: " + this.accountNumber +
                ", Type: " + this.accountType +
                ", Balance: " + shillings + "." + String.format("%02d", cents) + " KSh" +
                ", Status: " + this.accountStatus +
                ", Created: " + this.createdAt + "]";
    }
}
