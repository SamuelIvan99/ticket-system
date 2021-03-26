package model;

import java.util.ArrayList;

/**
 * @author Group 1
 */
public class Address {
    /**
     * Instance variables
     */
    private int addressID;
    private String country;
    private String zipCode;
    private String streetName;
    private int streetNumber;

    /**
     * Reference variables
     */
    private ArrayList<Department> departments;

    /**
     * Constructors
     */
    public Address() {
        this.country = "";
    }

    public Address(String country, String zipCode, String streetName, int streetNumber) {
        this.country = country;
        this.zipCode = zipCode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        departments = new ArrayList<>();
    }

    /**
     * Getters and Setters
     */
    public String getCountry() {
        return country;
    }

    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getStreetName() {
        return streetName;
    }

    public Address setStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public int getAddressID() {
        return addressID;
    }

    public ArrayList<Department> getDepartments() {
        return new ArrayList<>(departments);
    }

    public Address setDepartments(ArrayList<Department> departments) {
        this.departments = new ArrayList<>(departments);
        return this;
    }

    public void addDepartment(Department department) {
        if (department != null)
            departments.add(department);
    }

    public void removeDepartment(Department department) {
        if (department != null)
            departments.removeIf(element -> element == department);
    }

    public Address setAddressID(int addressID) {
        this.addressID = addressID;
        return this;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public Address setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    @Override
    public String toString() {
        return "    Address [ ID = " + addressID + ", country = " + country + ", departments = "
                + departments + ", streetName = " + streetName + ", streetNumber = " + streetNumber + ", zipCode = " + zipCode
                + " ]";
    }
}
