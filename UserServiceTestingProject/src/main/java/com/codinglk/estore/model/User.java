package com.codinglk.estore.model;

/**
 * @author codinglk
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;

    public User(String firstName, String lastName, String email, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

}
