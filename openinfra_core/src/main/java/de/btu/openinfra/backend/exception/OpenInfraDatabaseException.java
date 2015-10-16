package de.btu.openinfra.backend.exception;

import javax.ws.rs.core.Response;

/**
 * This class provides an exception for the database.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraDatabaseException extends OpenInfraWebException {
	private static final long serialVersionUID = 1L;
	
	public OpenInfraDatabaseException(OpenInfraExceptionTypes type) {
		super(Response.Status.INTERNAL_SERVER_ERROR, 
				OpenInfraDatabaseException.class.getName(), type);
	}

}