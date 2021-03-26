package model;

import java.time.LocalDateTime;

/**
 * @author Group 1
 */
public class Response {
    /**
     * Instance Variables
     */
    private int responseID;
    private String title;
    private String description;
    private LocalDateTime timestamp;

    /**
     * Reference Variables
     */
    private Employee employee;

    /**
     * Constructors
     */
    public Response() {
    }

    public Response(String title, String description, LocalDateTime timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Getters and setters
     */
    public String getTitle() {
        return title;
    }

    public Response setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Response setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Response setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getResponseID() {
        return responseID;
    }

    public Response setResponseID(int responseID) {
        this.responseID = responseID;
        return this;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Response setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }
}
