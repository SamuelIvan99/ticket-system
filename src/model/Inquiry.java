package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group 1
 */
public class Inquiry {
    public enum InquiryCategory {
        QUESTION("Question"), COMPLAINT("Complaint"), REFUND("Refund"), RETURN("Return"), MIXED("Mixed"),
        OTHER("Other"), UNKNOWN("Unknown");

        final String label;

        private InquiryCategory(String label) {
            this.label = label;
        }

        public final String getLabel() {
            return label;
        }

    }

    /**
     * Instance Variables
     */
    private int inquiryID;
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private InquiryCategory category;

    /**
     * Reference Variables
     */
    private List<FormField> formFields;
    private Department department;
    private List<Response> responses;

    /**
     * Constructors
     */
    public Inquiry() {
        formFields = new ArrayList<>();
        responses = new ArrayList<>();
        category = InquiryCategory.UNKNOWN;
    }

    public Inquiry(String title, String description, LocalDateTime timestamp) {
        this();
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

    public Inquiry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Inquiry setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Inquiry setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<FormField> getFormFields() {
        return new ArrayList<>(formFields);
    }

    public Inquiry setFormFields(List<FormField> formFields) {
        this.formFields = new ArrayList<>(formFields);
        return this;
    }

    public List<Response> getResponses() {
        return new ArrayList<>(responses);
    }

    public Inquiry setResponses(List<Response> responses) {
        this.responses = new ArrayList<>(responses);
        return this;
    }

    public void addField(FormField formField) {
        if (formField != null) {
            this.formFields.add(formField);
        }
    }

    public void removeField(FormField formField) {
        if (formField != null)
            formFields.removeIf(element -> element == formField);
    }

    public void addResponse(Response response) {
        if (response != null) {
            this.responses.add(response);
        }
    }

    public void removeResponse(Response response) {
        if (response != null)
            responses.removeIf(element -> element == response);
    }

    public int getInquiryID() {
        return inquiryID;
    }

    public Inquiry setInquiryID(int inquiryID) {
        this.inquiryID = inquiryID;
        return this;
    }

    public Department getDepartment() {
        return department;
    }

    public Inquiry setDepartment(Department department) {
        this.department = department;
        return this;
    }

    public InquiryCategory getCategory() {
        return category;
    }

    public Inquiry setCategory(String category) {
        category = category.toUpperCase();
        try {
            this.category = InquiryCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            this.category = InquiryCategory.UNKNOWN;
        }
        return this;
    }
}
