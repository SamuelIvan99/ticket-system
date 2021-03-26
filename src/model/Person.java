package model;

/**
 * @author Group 1
 */
public abstract class Person {
    /**
     * Instance Variables
     */
    protected int personID;
    protected String fullName;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String email;

    /**
     * Constructors
     */
    public Person() {
    }

    public Person(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        setFullName();
    }

    /**
     * Getters and setters
     */
    public String getFullName() {
        return fullName;
    }

    public Person setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Person setFullName() {
        this.fullName = this.firstName + " " + this.lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Person setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getPersonID() {
        return personID;
    }

    public Person setPersonID(int personID) {
        this.personID = personID;
        return this;
    }

    @Override
    public String toString() {
        return "[ ID = " + personID + ", fullName = " + fullName + ", email = " + email
                + ", phoneNumber = " + phoneNumber;
    }
}
