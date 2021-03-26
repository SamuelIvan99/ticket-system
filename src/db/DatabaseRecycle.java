package db;

/**
 * @author Group 1
 */
public class DatabaseRecycle {
    public static void recycleDatabase(String cleanPath, String setupPath, String populatePath)
            throws DataAccessException {

        DBConnection.runScripts(cleanPath, "CleanupField", "CleanupResponse", "CleanupBuilding",
                "CleanupInquiry", "CleanupTicket", "CleanupEmployee", "CleanupDepartment",
                "CleanupCustomer", "CleanupAddress");

        DBConnection.runScripts(setupPath, "SetupAddress", "SetupCustomer", "SetupDepartment", "SetupEmployee",
                "SetupTicket", "SetupInquiry", "SetupBuilding", "SetupResponse", "SetupField");

        // Put here whatever you want to populate
        DBConnection.runScripts(populatePath, "PopulateAddress", "PopulateCustomer", "PopulateDepartment",
                "PopulateEmployee");
    }
}
