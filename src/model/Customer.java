package model;

/**
 * @author Group 1
 */
public class Customer extends Person {
    public enum CustomerType {
        OLD_CUSTOMER("Old_Customer"), NEW_CUSTOMER("New_Customer"), SPECIAL_CUSTOMER("Special_Customer"),
        UNKNOWN("Unknown");

        final String label;

        private CustomerType(String label) {
            this.label = label;
        }

        public final String getLabel() {
            return label;
        }
    }

    /**
     * Instance Variables
     */
    private String companyName;

    /**
     * Reference Variables
     */
    private CustomerType customerType;
    private Address address;

    /**
     * Constructors
     */
    public Customer() {
    }

    public Customer(String firstName, String lastName, String phoneNumber, String email, String customerType,
            String companyName) {
        super(firstName, lastName, phoneNumber, email);
        setCustomerType(customerType);
        this.companyName = companyName;
    }

    /**
     * Getters and setters
     */
    public CustomerType getCustomerType() {
        return customerType;
    }

    public Customer setCustomerType(String customerType) {
        customerType = customerType.toUpperCase();
        try {
            this.customerType = CustomerType.valueOf(customerType);
        } catch (IllegalArgumentException e) {
            this.customerType = CustomerType.UNKNOWN;
        }
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Customer setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Customer setAddress(Address address) {
        this.address = address;
        return this;
    }

    @Override
    public String toString() {
        return "Customer " + super.toString() + ", companyName = " + companyName + ", customerType = " + customerType
                + " ]\n" + address;
    }
}
