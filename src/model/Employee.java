package model;

/**
 * @author Group 1
 */
public class Employee extends Person {
    public enum EmployeeType {
        MANAGER("Manager"),
        SALES_ASSISTANT("Sales_Assistant"),
        UNKNOWN("Unknown");
        
        final String label;

        private EmployeeType(String label) {
            this.label = label;
        }

        public final String getLabel() {
            return label;
        }
    }

    /**
     * Instance Variables
     */
    private String password;

    /**
     * Reference Variables
     */
    private EmployeeType employeeType;
    private Department department;

    /**
     * Constructors
     */
    public Employee() {
    }

    public Employee(String fistName, String lastName, String phoneNumber, String email, String password,
            String employeeType) {
        super(fistName, lastName, phoneNumber, email);
        employeeType = employeeType.toUpperCase();
        try {
            this.employeeType = EmployeeType.valueOf(employeeType);
        } catch (IllegalArgumentException e) {
            this.employeeType = EmployeeType.UNKNOWN;
        }
        this.password = password;
    }

    /**
     * Getters and setters
     */
    public String getPassword() {
        return password;
    }

    public Employee setPassword(String password) {
        this.password = password;
        return this;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public Employee setEmployeeType(String employeeType) {
        employeeType = employeeType.toUpperCase();
        try {
            this.employeeType = Employee.EmployeeType.valueOf(employeeType);
        } catch (IllegalArgumentException e) {
            this.employeeType = Employee.EmployeeType.UNKNOWN;
        }
        return this;
    }

    public Department getDepartment() {
        return department;
    }

    public Employee setDepartment(Department department) {
        this.department = department;
        return this;
    }
}
