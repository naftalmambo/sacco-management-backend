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
        validateLoanData(principleAmountInCents, interestRate);
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
        return this.interestRate;
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

}
