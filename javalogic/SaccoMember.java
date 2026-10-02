import java.time.LocalDate;

public class SaccoMember {
    private long memberId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String emailAddress;
    private String phoneNumber;
    private String nationalId;
    private String country;
    private String county;

    public SaccoMember(String firstName, String lastName, LocalDate dateOfBirth, String sex, String emailAddress,
            String phoneNumber, String nationalId, String county) {
        if (!(phoneNumber == null)) {
            phoneNumber = phoneNumber.replace("+", "");
        }
        validateMemberData(dateOfBirth, nationalId, phoneNumber, emailAddress);
        this.memberId = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.nationalId = nationalId;
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

    public String getNationalId() {
        return this.nationalId;
    }

    public String getCountry() {
        return this.country;

    }

    public String getCounty() {
        return this.county;
    }

    private void validateMemberData(LocalDate dateOfBirth, String nationalId, String phoneNumber, String emailAddress) {
        if (java.time.Period.between(dateOfBirth, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Member must be at least 18 years old to register.");
        }

        if (nationalId == null || nationalId.isEmpty()) {
            throw new IllegalArgumentException("National ID cannot be blank");
        }

        if (!(nationalId.matches("[0-9]+"))) {
            throw new IllegalArgumentException("National ID must contain numbers only");
        }

        if (nationalId.length() < 6 || nationalId.length() > 9) {
            throw new IllegalArgumentException("Invalid Kenyan National ID length");
        }

        if (phoneNumber == null || !(phoneNumber.matches("[0-9]+"))) {
            throw new IllegalArgumentException("Phone number must contain numbers only");
        }

        if (!(phoneNumber.length() == 10) && !(phoneNumber.length() == 12)) {
            throw new IllegalArgumentException("Invalid Kenyan phone number length");
        }

        if (emailAddress == null || !(emailAddress.contains(".")) || !(emailAddress.contains("@"))) {
            throw new IllegalArgumentException("Invalid email address format");
        }
    }

    @Override
    public String toString() {
        return "Member [ID: " + this.memberId +
                ", Name: " + this.firstName + " " + this.lastName +
                ", Sex: " + this.sex +
                ", Phone: " + this.phoneNumber +
                ", Location: " + this.country + ", " + this.county + "]";
    }

}
