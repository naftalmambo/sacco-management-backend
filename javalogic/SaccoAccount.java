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

}
