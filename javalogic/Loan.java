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

    public void disburseLoan() {
        if (!(this.loanStatus.equals("PENDING APPROVAL"))) {
            throw new IllegalStateException("Only loans with PENDING APPROVAL status can be disbursed");
        }
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

        this.principleAmountInCents -= transaction.getAmountInCents();

        if (this.principleAmountInCents <= 0) {
            this.principleAmountInCents = 0;
            this.loanStatus = "FULLY PAID";
        }
    }

    @Override
    public String toString() {
        long shillings = this.principleAmountInCents / 100;
        long cents = this.principleAmountInCents % 100;

        return "Loan [ID: " + this.loanId + " | Member ID: " + this.memberId + " | Status: "
                + this.loanStatus + " | Principal: " + shillings + "." + cents + " KSh | Rate: "
                + this.interestRate + "% | Applied: " + this.appliedAt + "]";

    }

}
