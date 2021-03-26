package model;

/**
 * @author Group 1
 */
public class FormField {
    /**
     * Instance Variables
     */
    private int fieldID;
    private String name;
    private String content;

    /**
     * Reference Variables
     */

    /**
     * Constructors
     */
    public FormField() {
    }

    public FormField(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * Getters and setters
     */
    public String getName() {
        return name;
    }

    public FormField setName(String name) {
        this.name = name;
        return this;
    }

    public String getContent() {
        return content;
    }

    public FormField setContent(String content) {
        this.content = content;
        return this;
    }

    public int getFieldID() {
        return fieldID;
    }

    public FormField setFieldID(int fieldID) {
        this.fieldID = fieldID;
        return this;
    }
}
