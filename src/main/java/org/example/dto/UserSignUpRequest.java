package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserSignUpRequest {

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email format")
    private String emailId;

    @NotBlank(message = "userName is required")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String userName;

    @NotBlank(message = "password is required")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserSignUpRequest(String firstName, String lastName, String emailId, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.userName = userName;
        this.password = password;
    }

    public UserSignUpRequest() {
    }
}
