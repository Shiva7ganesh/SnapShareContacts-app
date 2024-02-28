package com.example.contactsharingqr;
public class Contact {

    private String name;
    private String phoneNumber;
    private String email; // Add a field for the email address

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email; // Initialize the email address field
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
