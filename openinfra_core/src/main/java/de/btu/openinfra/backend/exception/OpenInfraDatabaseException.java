package de.btu.openinfra.backend.exception;

/**
 * This class provides an exception for the database and will extend the
 * OpenInfraException class. All database exception classes should extend this
 * class.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraDatabaseException extends OpenInfraException {
    private static final long serialVersionUID = 1L;

    /* Contains the exception type */
    private OpenInfraExceptionTypes type;

    /**
     *  Default constructor
     */
    public OpenInfraDatabaseException() {
        super();
    }

    /**
     *  Overloaded constructor that accept a defined error message.
     *
     *  @param String the exception message
     */
    public OpenInfraDatabaseException(String msg) {
        super(msg);
    }

    /**
     * Overloaded constructor for predefined error messages.
     *
     * @param type the {@link OpenInfraExceptionTypes} of the exception.
     */
    public OpenInfraDatabaseException(OpenInfraExceptionTypes type) {
        super(type.getMessage());
        this.type = type;
    }

    /**
     * Returns the exception type.
     *
     * @return {@link OpenInfraExceptionTypes} the exception type
     */
    public OpenInfraExceptionTypes getType() {
        return type;
    }
}