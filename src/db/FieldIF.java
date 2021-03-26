package db;

import model.FormField;

import java.util.List;

/**
 * @author Group 1
 */
public interface FieldIF extends TableIF<FormField> {
    boolean insert(FormField field, int inquiryID) throws DataAccessException;

    List<FormField> findFieldsByName(String name, boolean fullAssociation) throws DataAccessException;

    List<FormField> findFieldsByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException;
}
