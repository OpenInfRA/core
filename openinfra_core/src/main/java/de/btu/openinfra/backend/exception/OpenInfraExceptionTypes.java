package de.btu.openinfra.backend.exception;

/**
 * These enumeration define the OpenInfRA exception types and their
 * corresponding error messages.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public enum OpenInfraExceptionTypes {
    CREATE_SCHEMA (
            "Failed to create the new project schema."),
    RENAME_SCHEMA (
            "Failed to rename the project schema."),
    INSERT_META_DATA (
            "Failed to create an entry in the table database_connection."),
    INSERT_BASIC_PROJECT_DATA (
            "Failed to insert the initial project information into the projects"
            + " table."),
    MERGE_SYSTEM (
            "Failed to merge the content of the system database into the"
            + " project schema.");

    private String msg;

    private OpenInfraExceptionTypes(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
