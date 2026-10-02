package com.sacco.domain;

public class SaccoAccount {

    private String accountNumber;

    private long memberId;
    private String accountType;
    private long balanceInCents;

    public SaccoAccount(String accountNumber, long memberId, String accountType) {
        this.accountNumber = accountNumber;
        this.memberId = memberId;
        this.accountType = accountType;
        this.balanceInCents = 0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getMemberId() {
        return memberId;

    }

    public String getAccountType() {
        return accountType;

    }

    public long getBalanceInCents() {
        return balanceInCents;

    }

    public void deposit(long amountInCents) {
        if (amountInCents > 0) {
            this.balanceInCents += amountInCents;

        }

    }

    public void withdraw(long amountInCents) {

        if (!(accountType.equals("SAVINGS"))) {
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
        long cents = this.balanceInCents % 100;
        return "Account Number: " + this.accountNumber + ", Member Id: " + this.memberId + ", Account Type: "
                + this.accountType + ", Balance: " + shillings + "." + cents + " Kshs";
    }

}
