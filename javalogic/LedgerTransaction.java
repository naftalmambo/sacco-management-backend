import java.time.LocalDateTime;

public class LedgerTransaction {
    private long transactionId;
    private long accountId;
    private String transactionType;
    private long amountInCents;
    private LocalDateTime createdAt;

    public LedgerTransaction(long accountId, String transactionType, long amountInCents) {
        this.transactionId = 0;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amountInCents = amountInCents;
        this.createdAt = LocalDateTime.now();

    }

}
