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
        return interestRate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getDisbursedAt() {
        return disbursedAt;
    }

}
