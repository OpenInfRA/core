package de.btu.openinfra.backend.exception;

/**
 * This class provides the global OpenInfraException. It provides a global
 * default constructor with a default error message. Further a overloaded
 * constructor is supported, which allows to send individual error messages.
 * All other OpenInfra exception classes should extend this class.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraException extends Exception {
    private static final long serialVersionUID = 1L;

    private String msg;

    /**
     *  Default constructor
     */
    public OpenInfraException() {
        super();
    }

    /**
     *  Overloaded constructor
     */
    public OpenInfraException(String msg) {
        this.msg = msg;
    }

    /**
     *  Overrides toString() function.
     */
    @Override
    public String toString() {
        return this.msg;
    }
}
