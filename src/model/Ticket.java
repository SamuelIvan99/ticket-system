package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Group 1
 */
public class Ticket {
    public enum TicketComplaintStatus {
        SOLVED("solved"),
        INPROGRESS("in progress"),
        CANCELLED("cancelled"),
        UNKNOWN("unknown");

        private final String title;

        TicketComplaintStatus(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public static String[] getValues() {
            ArrayList<String> stringValues = new ArrayList<>();
            for (TicketComplaintStatus value :
                    TicketComplaintStatus.values()) {
                stringValues.add(value.getTitle());
            }

            return stringValues.toArray(String[]::new);
        }
    }

    public enum Priority {
        HIGH("high"),
        MEDIUM("medium"),
        LOW("low"),
        UNKNOWN("unknown");

        private final String title;

        Priority(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * Instance Variables
     */
    private int version;

    private int ticketID;
    private TicketComplaintStatus complaintStatus;
    private Priority priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * Reference Variables
     */
    private Employee employee;
    private Customer customer;
    private ArrayList<Inquiry> inquiries;

    /**
     * Constructors
     */
    public Ticket() {
        version = 0;
        priority = Priority.MEDIUM;
        complaintStatus = TicketComplaintStatus.INPROGRESS;
        startDate = LocalDateTime.now();
    }

    public Ticket(String complaintStatus, String priority) {
        version = 0;
        setComplaintStatus(complaintStatus);
        setPriority(priority);
        inquiries = new ArrayList<>();
    }

    /**
     * Getters and setters
     */
    public int getTicketID() {
        return ticketID;
    }

    public Ticket setTicketID(int ticketID) {
        this.ticketID = ticketID;
        return this;
    }

    public TicketComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public Ticket setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus.equals("solved") ? TicketComplaintStatus.SOLVED :
                complaintStatus.equals("in progress") ? TicketComplaintStatus.INPROGRESS :
                        complaintStatus.equals("cancelled") ? TicketComplaintStatus.CANCELLED : TicketComplaintStatus.UNKNOWN;
        if (this.complaintStatus != TicketComplaintStatus.INPROGRESS)
            this.endDate = LocalDateTime.now();
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public Ticket setPriority(String priority) {
        this.priority = priority.equals("high") ? Priority.HIGH : priority.equals("medium") ? Priority.MEDIUM : priority.equals("low") ? Priority.LOW : Priority.UNKNOWN;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Ticket setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Ticket setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Ticket setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ticket setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ArrayList<Inquiry> getInquiries() {
        return new ArrayList<>(inquiries);
    }

    public Ticket setInquiries(ArrayList<Inquiry> inquiries) {
        this.inquiries = new ArrayList<>(inquiries);
        return this;
    }

    public void addInquiry(Inquiry inquiry) {
        if (inquiry != null)
            inquiries.add(inquiry);
    }

    public void removeInquiry(Inquiry inquiry) {
        if (inquiry != null)
            inquiries.removeIf(element -> element == inquiry);
    }

    public int getVersion() {
        return version;
    }

    public Ticket setVersion(int version) {
        this.version = version;
        return this;
    }

    public Ticket incrementVersion() {
        this.version += 1;
        return this;
    }
}
