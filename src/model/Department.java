package model;

/**
 * @author Group 1
 */
public class Department {
    /**
     * Instance Variables
     */
    private String name;
    private int departmentID;

    /**
     * Reference Variables
     */

    /**
     * Constructors
     */
    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    /**
     * Getters and setters
     */
    public String getName() {
        return name;
    }

    public Department setName(String name) {
        this.name = name;
        return this;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public Department setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
        return this;
    }
}
