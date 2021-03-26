package controller;

import java.sql.SQLException;
import java.util.List;

import db.DBConnection;
import db.DataAccessException;
import db.FieldDB;
import db.FieldIF;
import model.FormField;

public class FieldController {
	private final FieldIF fieldDB;

	public FieldController() {
		fieldDB = new FieldDB();
	}

	public boolean createField(FormField field, int inquiryID) throws DataAccessException {
		try {
			boolean result = false;
			DBConnection.getInstance().startTransaction();
			result = fieldDB.insert(field, inquiryID);
			DBConnection.getInstance().commitTransaction();
			return result;
		} catch (SQLException e) {
			try {
				DBConnection.getInstance().rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new DataAccessException("Transaction error while creating a field.", e);
		}
	}

	public FormField findFieldByID(int id, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findByID(id, fullAssociation);
	}

	public List<FormField> findFieldsByName(String name, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findFieldsByName(name, fullAssociation);
	}

	public List<FormField> findFieldsByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
		return fieldDB.findFieldsByInquiryID(inquiryID, fullAssociation);
	}

	public List<FormField> getAllFields(boolean fullAssociation) throws DataAccessException {
		return fieldDB.getAll(fullAssociation);
	}

	public boolean updateField(FormField fieldToUpdate, String newName, String newContent) throws DataAccessException {
		FormField field = new FormField();
		try {
			boolean result = false;
			DBConnection.getInstance().startTransaction();
			field.setName(newName).setContent(newContent);
			result = fieldDB.update(fieldToUpdate);
			DBConnection.getInstance().commitTransaction();
			return result;
		} catch (SQLException e) {
			try {
				DBConnection.getInstance().rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new DataAccessException("Transaction error while updating a field.", e);
		}
	}

	public boolean deleteField(int id) throws DataAccessException {
		try {
			boolean result = false;
			DBConnection.getInstance().startTransaction();
			result = fieldDB.delete(id);
			DBConnection.getInstance().commitTransaction();
			return result;
		} catch (SQLException e) {
			try {
				DBConnection.getInstance().rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new DataAccessException("Transaction error while deleting a field.", e);
		}
	}
}
