import java.time.LocalDateTime;

public class LedgerTransaction {
    private long transactionId;
    private long accountId;
    private String transactionType;
    private long amountInCents;
    private LocalDateTime createdAt;

    public LedgerTransaction(long accountId, String transactionType, long amountInCents) {
        validateTransaction(transactionType, amountInCents);
        this.transactionId = 0;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amountInCents = amountInCents;
        this.createdAt = LocalDateTime.now();
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
        if (!(transactionType.equals("DEPOSIT")) && !(transactionType.equals("WITHDRAWAL"))
                && !(transactionType.equals("LOAN REPAYMENT"))) {
            throw new IllegalArgumentException("Invalid Transaction Type");
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
