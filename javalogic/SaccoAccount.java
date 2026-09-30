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

    public boolean withdraw(long amountInCents) {
        if (amountInCents > 0 && (this.balanceInCents - amountInCents) >= 100000) {
            this.balanceInCents = this.balanceInCents - amountInCents;
            return true;

        }
        return false;

    }

    @Override
    public String toString() {
        long shillings = this.balanceInCents / 100;
        long cents = this.balanceInCents % 100;
        return "Account Number: " + this.accountNumber + ", Member Id: " + this.memberId + ", Account Type: "
                + this.accountType + ", Balance: " + shillings + "." + cents + " Kshs";
    }

}
