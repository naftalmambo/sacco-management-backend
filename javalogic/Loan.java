import java.time.LocalDateTime;

public class Loan {
    private long loanId;
    private long memberId;
    private long principleAmountInCents;
    private double interestRate;
    private String loanStatus;
    private LocalDateTime appliedAt;
    private LocalDateTime disbursedAt;

    public Loan(long memberId, long principleAmountInCents, double interestRate) {
        this.loanId = 0;
        this.memberId = memberId;
        this.principleAmountInCents = principleAmountInCents;
        this.interestRate = interestRate;
        this.loanStatus = "PENDING APPROVAL";
        this.appliedAt = LocalDateTime.now();
        this.disbursedAt = null;

    }

}
