package db;

import java.sql.*;

/**
 * @author Group-One
 */
public class DBConnection {

	private static final String SERVER_NAME = "hildur.ucn.dk";
	private static final String DB_NAME = "dmai0919_1081509";
	private static final String LOGIN = "dmai0919_1081509";
	private static final String PASSSWORD = "Password1!";

	private static final String CONNECTION_STR = "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DB_NAME;

	private static DBConnection instance;
	private static Connection connection;

	/**
	 * private constructor for the singleton pattern
	 */
	private DBConnection() {
	}

	/**
	 * Connects to the Database
	 *
	 * @return a Connection or an exception if it fails to connect
	 */
	public static Connection connect() {
		try {
			if (connection == null)
				connection = DriverManager.getConnection(CONNECTION_STR, LOGIN, PASSSWORD);
		} catch (SQLException ex) {
			System.err.println("Could not connect to " + DB_NAME + " database.");
			ex.printStackTrace();
		}
		return connection;
	}

	/**
	 * Disconnects
	 *
	 * @return true if disconnecting worked, or false if it could not disconnect
	 */
	public static boolean disconnect() {
		boolean result = false;
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
			result = true;
		} catch (SQLException ex) {
			System.err.println("Could not disconnect from " + SERVER_NAME + " server.");
			ex.printStackTrace();
		}
		return result;
	}

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}

	/**
	 * Execute query result set.
	 *
	 * @param code the code to execute
	 * @param var  the variables for a prepared statement
	 * @return the result set found from the query
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static ResultSet executeQuery(String code, Object... var) throws DataAccessException {
		try {
			PreparedStatement preparedStatement = SqlUtil.prepareStatement(code, var);
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			throw new DataAccessException("Couldn't execute query.", e);
		}
	}

	/**
	 * Execute query result set.
	 *
	 * @param scriptPath the path to the sql file to execute
	 * @param var        the variables for a prepared statement
	 * @return the result set found from the query
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static ResultSet executeQueryFromFile(String scriptPath, Object... var) throws DataAccessException {
		try {
			String code = SqlUtil.readScript(scriptPath);
			PreparedStatement preparedStatement = SqlUtil.prepareStatement(code, var);
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			throw new DataAccessException("Couldn't execute query.", e);
		}
	}

	/**
	 * Execute update int.
	 *
	 * @param sql the sql to execute
	 * @return returns the number of rows that the update affected
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static int executeUpdate(String sql) throws DataAccessException {
		try {
			Statement s = connection.createStatement();
			return s.executeUpdate(sql);
		} catch (SQLException e) {
			throw new DataAccessException("Could not execute update", e);
		}
	}

	/**
	 * Execute query result set.
	 *
	 * @param code the code to execute
	 * @param var  the variables for a prepared statement
	 * @return number of changed rows from the update
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static int executeUpdate(String code, Object... var) throws DataAccessException {
		try {
			PreparedStatement preparedStatement = SqlUtil.prepareStatement(code, var);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Couldn't execute query.", e);
		}
	}

	/**
	 * Execute update int.
	 *
	 * @param scriptPath the path to an sql file to execute
	 * @return returns the number of rows that the update affected
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static int executeUpdateFromFile(String scriptPath) throws DataAccessException {
		try {
			String code = SqlUtil.readScript(scriptPath);
			Statement s = connection.createStatement();
			return s.executeUpdate(code);
		} catch (SQLException e) {
			throw new DataAccessException("Could not execute update", e);
		}
	}

	/**
	 * Method to insert into table with an identity
	 *
	 * @param code sql code to execute
	 * @param var  variables to a prepared statement
	 * @return generatedKey representing the generated key
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static int executeInsertWithIdentity(String code, Object... var) throws DataAccessException {
		try {
			PreparedStatement preparedStatement = SqlUtil.prepareStatement(code, var);
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not execute update", e);
		}
	}

	/**
	 * Method to insert into table with an identity
	 *
	 * @param scriptPath path to an sql file to execute
	 * @param var        variables to a prepared statement
	 * @return generatedKey representing the generated key
	 * @throws DataAccessException the data access exception gets thrown with a
	 *                             database error
	 */
	public static int executeInsertWithIdentityFromFile(String scriptPath, Object... var) throws DataAccessException {
		String code = SqlUtil.readScript(scriptPath);
		try {
			PreparedStatement preparedStatement = SqlUtil.prepareStatement(code, var);
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DataAccessException("Could not execute update", e);
		}
	}

	public static void runScripts(String dirPath, String... fileNames) throws DataAccessException {
		for (String fileName : fileNames) {
			System.out.println("Executing " + dirPath + fileName + ".sql");
			executeUpdateFromFile(dirPath + fileName + ".sql");
		}
	}

	public void startTransaction() throws SQLException {
		connection.setAutoCommit(false);
	}

	public void commitTransaction() throws SQLException {
		connection.commit();
		connection.setAutoCommit(true);
	}

	public void rollbackTransaction() throws SQLException {
		connection.rollback();
		connection.setAutoCommit(true);
	}
}