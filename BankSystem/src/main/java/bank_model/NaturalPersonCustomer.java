package bank_model;

import bank_enums.UserStatus;

public class NaturalPersonCustomer extends Customer {

    private String fullName;
    private String identificationNumber;
    private String birthDate;

    public NaturalPersonCustomer() {
    }

    public NaturalPersonCustomer(String customerId, String email, String phone, String address, UserStatus status,
                                 String fullName, String identificationNumber, String birthDate) {
        super(customerId, email, phone, address, status);
        this.fullName = fullName;
        this.identificationNumber = identificationNumber;
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "NaturalPersonCustomer{" +
                "fullName='" + fullName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", baseData=" + super.toString() +
                '}';
    }
}