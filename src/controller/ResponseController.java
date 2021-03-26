package controller;

import db.DBConnection;
import db.DataAccessException;
import db.ResponseDB;
import db.ResponseIF;
import model.Employee;
import model.Response;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ResponseController {
	private final ResponseIF responseDB;

	public ResponseController() {
		responseDB = new ResponseDB();
	}

	public boolean createResponse(Response response, int inquiryID) throws DataAccessException {
		boolean result = false;
		//DBConnection.getInstance().startTransaction();
		result = responseDB.insert(response, inquiryID);
		//DBConnection.getInstance().commitTransaction();
		return result;
	}

	public Response findResponseByID(int id, boolean fullAssociation) throws DataAccessException {
		return responseDB.findByID(id, fullAssociation);
	}

	public List<Response> findResponsesByTitle(String title, boolean fullAssociation) throws DataAccessException {
		return responseDB.findResponsesByTitle(title, fullAssociation);
	}

	public List<Response> findResponsesByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
		return responseDB.findResponsesByInquiryID(inquiryID, fullAssociation);
	}

	public List<Response> findResponsesByEmployeeID(int employeeID, boolean fullAssociation)
			throws DataAccessException {
		return responseDB.findResponsesByEmployeeID(employeeID, fullAssociation);
	}

	public List<Response> getAllResponses(boolean fullAssociation) throws DataAccessException {
		return responseDB.getAll(fullAssociation);
	}

	public boolean updateResponse(Response responseToUpdate, String newTitle, String newDescription,
			LocalDateTime newTimestamp, Employee newEmployee) throws DataAccessException {
		try {
			boolean result = false;
			DBConnection.getInstance().startTransaction();
			Response response = new Response();
			response.setTitle(newTitle).setDescription(newDescription).setTimestamp(newTimestamp)
					.setEmployee(newEmployee);
			result = responseDB.update(responseToUpdate);
			DBConnection.getInstance().commitTransaction();
			return result;
		} catch (SQLException e) {
			try {
				DBConnection.getInstance().rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new DataAccessException("Transaction error while updating a response.", e);
		}
	}

	public boolean deleteResponse(int ID) throws DataAccessException {
		try {
			boolean result = false;
			DBConnection.getInstance().startTransaction();
			result = responseDB.delete(ID);
			DBConnection.getInstance().commitTransaction();
			return result;
		} catch (SQLException e) {
			try {
				DBConnection.getInstance().rollbackTransaction();
			} catch (SQLException e1) {
			}			throw new DataAccessException("Transaction error while deleting a response.", e);
		}
	}
}
