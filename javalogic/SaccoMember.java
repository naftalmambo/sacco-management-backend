import java.time.LocalDate;

public class SaccoMember {
    private long memberId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String emailAddress;
    private String phoneNumber;
    private String country;
    private String county;

    public SaccoMember(String firstName, String lastName, LocalDate dateOfBirth, String sex, String emailAddress,
            String phoneNumber, String county) {
        this.memberId = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.country = "Kenya";
        this.county = county;

    }

    public long getMemberId() {
        return this.memberId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public String getSex() {
        return this.sex;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCountry() {
        return this.country;

    }

    public String getCounty() {
        return this.county;
    }

}
