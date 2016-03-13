package com.example.whatsemo.classmates;

/**
 * Created by WhatsEmo on 3/5/2016.
 *
 * Used to store User data
 */
public class User {
    private CharSequence firstName;
    private CharSequence middleName;
    private CharSequence lastName;
    private CharSequence school;
    private CharSequence[] interests;
    private CharSequence email;

    public User(CharSequence firstName,
                CharSequence middleName,
                CharSequence lastName,
                CharSequence school,
                CharSequence[] interests,
                CharSequence email){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.school = school;
        this.interests = interests;
        this.email = email;
    }

    public void setFirstName(CharSequence firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(CharSequence middleName) {
        this.middleName = middleName;
    }

    public void setLastName(CharSequence lastName) {
        this.lastName = lastName;
    }

    public void setSchool(CharSequence school) {
        this.school = school;
    }

    public void setInterests(CharSequence[] interests) {
        this.interests = interests;
    }

    public void setEmail(CharSequence email) {
        this.email = email;
    }
}
