package com.example.contactsharingqr;
public class ContactUtils {

    public Contact parseVCardData(String vCardData) {
        // Implement your vCard parsing logic here
        // This is a simplified example, and you may need to adjust it based on your vCard format

        String[] lines = vCardData.split("\n");

        String name = "";
        String phoneNumber = "";
        String email = ""; // Add a variable to store the email address

        for (String line : lines) {
            if (line.startsWith("FN:")) {
                // Extracting the full name (FN) field
                name = line.substring(3);
            } else if (line.startsWith("TEL:")) {
                // Extracting the telephone number (TEL) field
                phoneNumber = line.substring(4);
            } else if (line.startsWith("EMAIL:")) {
                // Extracting the email address field
                email = line.substring(6);
            }
        }

        return new Contact(name, phoneNumber, email); // Return the contact object with the email address
    }
}
