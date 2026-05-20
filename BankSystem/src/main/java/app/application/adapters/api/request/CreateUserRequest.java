package app.application.adapters.api.request;

import app.domain.enums.UserRole;

public class CreateUserRequest {
    private String fullName;
    private String identificationId;
    private String email;
    private String phone;
    private String birthDate;
    private String address;
    private UserRole role;
    private String username;
    private String password;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getIdentificationId() { return identificationId; }
    public void setIdentificationId(String identificationId) { this.identificationId = identificationId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
