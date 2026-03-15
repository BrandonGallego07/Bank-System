package bank_model;

import bank_enums.UserRole;
import bank_enums.UserStatus;

public class User {

    private int userId;
    private String relatedId;
    private String fullName;
    private String identificationId;
    private String email;
    private String phone;
    private String birthDate;
    private String address;
    private UserRole role;
    private UserStatus status;
    private String username;
    private String password;

    public User() {
    }

    public User(int userId, String relatedId, String fullName, String identificationId, String email,
                String phone, String birthDate, String address, UserRole role,
                UserStatus status, String username, String password) {
        this.userId = userId;
        this.relatedId = relatedId;
        this.fullName = fullName;
        this.identificationId = identificationId;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(String identificationId) {
        this.identificationId = identificationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", relatedId='" + relatedId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", identificationId='" + identificationId + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", username='" + username + '\'' +
                '}';
    }
}