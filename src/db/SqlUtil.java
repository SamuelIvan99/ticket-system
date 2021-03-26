package db;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Group 1
 */
public class SqlUtil {

    private static Connection connection = DBConnection.connect();

    /**
     * Get scripts from dir string [ ]. Returns a list of the names of all the .sql
     * files in a directory.
     *
     * @param dirPath the dir path
     * @return the string [ ]
     */
    public static String[] getScriptsFromDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".sql");
            }
        });
    }

    /**
     * Transforms the .sql file into a String with the sql code
     *
     * @param scriptPath the script path
     * @return the string
     */
    public static String readScript(String scriptPath) {
        StringBuilder builder = null;

        BufferedReader buffer;
        FileReader file;
        String line;

        try {
            builder = new StringBuilder();
            file = new FileReader(scriptPath);
            buffer = new BufferedReader(file);

            while ((line = buffer.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return builder.toString();
    }

    /**
     * Prepare statement creates a prepared statement with the code and puts the
     * variables int place of the question marks (?)
     *
     * @param code the code for the prepared statement with question marks
     * @param var  ... of variables to put in place of question marks (?)
     * @return the prepared statement
     * @throws DataAccessException the data access exception
     */
    public static PreparedStatement prepareStatement(String code, Object... var) throws DataAccessException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(code,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < var.length; i++) {
                if (var[i] instanceof Integer)
                    preparedStatement.setInt(i + 1, (int) var[i]);
                else if (var[i] instanceof String)
                    preparedStatement.setString(i + 1, String.valueOf(var[i]));
                else if (var[i] instanceof Float)
                    preparedStatement.setFloat(i + 1, (float) var[i]);
                else if (var[i] instanceof LocalDateTime)
                    preparedStatement.setTimestamp(i + 1, Timestamp.valueOf((LocalDateTime) var[i]));
            }
            return preparedStatement;
        } catch (SQLException e) {
            throw new DataAccessException("Couldn't prepare the statement", e);
        }
    }

    /**
     * Makes an INSERT statement like this: INSERT INTO "tableName" ("colNames ...")
     * VALUES (?, ?, ...) to use with PreparedStatement
     *
     * @param tableName the table name
     * @param colNames  the col names
     * @return the string
     */
    public static String buildInsert(String tableName, String... colNames) {
        if (colNames.length > 0) {
            StringBuilder INSERT_SCRIPT = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder VALUES = new StringBuilder("VALUES (");

            for (String colName : colNames) {
                INSERT_SCRIPT.append(colName).append(", ");
                VALUES.append("?, ");
            }

            INSERT_SCRIPT.replace(INSERT_SCRIPT.length() - 2, INSERT_SCRIPT.length(), ") ");
            VALUES.replace(VALUES.length() - 2, VALUES.length(), ")");

            INSERT_SCRIPT.append(VALUES);
            return INSERT_SCRIPT.toString();
        }
        return null;
    }

    public static String buildUpdate(String tableName, String idName, String... colNames) {
        if (colNames.length > 0) {
            StringBuilder UPDATE_SCRIPT = new StringBuilder("UPDATE " + tableName + " SET ");
            StringBuilder VALUES = new StringBuilder();

            for (String colName : colNames) {
                String element = String.format("%s = ?, ", colName);
                VALUES.append(element);
            }
            String WHERE = String.format(" WHERE %s = ?", idName);

            UPDATE_SCRIPT.append(VALUES);
            UPDATE_SCRIPT.replace(UPDATE_SCRIPT.length() - 2, UPDATE_SCRIPT.length(), WHERE);
            System.out.println(UPDATE_SCRIPT.toString());
            return UPDATE_SCRIPT.toString();
        }
        return null;
    }
}