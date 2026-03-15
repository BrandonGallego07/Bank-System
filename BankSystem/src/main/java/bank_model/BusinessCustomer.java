package bank_model;

import bank_enums.UserStatus;

public class BusinessCustomer extends Customer {

    private String businessName;
    private String taxId;
    private NaturalPersonCustomer legalRepresentative;

    public BusinessCustomer() {
    }

    public BusinessCustomer(String customerId, String email, String phone, String address, UserStatus status,
                            String businessName, String taxId, NaturalPersonCustomer legalRepresentative) {
        super(customerId, email, phone, address, status);
        this.businessName = businessName;
        this.taxId = taxId;
        this.legalRepresentative = legalRepresentative;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public NaturalPersonCustomer getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(NaturalPersonCustomer legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    @Override
    public String toString() {
        return "BusinessCustomer{" +
                "businessName='" + businessName + '\'' +
                ", taxId='" + taxId + '\'' +
                ", legalRepresentative=" + legalRepresentative +
                ", baseData=" + super.toString() +
                '}';
    }
}