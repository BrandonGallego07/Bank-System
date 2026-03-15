package bank_model;

import bank_enums.ProductCategory;

public class BankProduct {

    private String productCode;
    private String productName;
    private ProductCategory category;
    private boolean requiresApproval;

    public BankProduct() {
    }

    public BankProduct(String productCode, String productName, ProductCategory category, boolean requiresApproval) {
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.requiresApproval = requiresApproval;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    @Override
    public String toString() {
        return "BankProduct{" +
                "productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", category=" + category +
                ", requiresApproval=" + requiresApproval +
                '}';
    }
}
