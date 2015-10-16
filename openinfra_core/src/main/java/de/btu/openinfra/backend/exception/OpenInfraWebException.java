package de.btu.openinfra.backend.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * This is the OpenInfRA web exception. Every OpenInfRA exception should extend
 * this exception. 
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraWebException extends WebApplicationException {
	
	private static final long serialVersionUID = 1L;
	private OpenInfraExceptionTypes type;

	/**
	 * Default constructor
	 * 
	 * @param status the HTTP status code
	 * @param name   the exception name
	 * @param type   the message which is shown to the client
	 */
	protected OpenInfraWebException(Status status, String name, 
			OpenInfraExceptionTypes type) {
		super(Response.status(status).entity(
				new OpenInfraExceptionItem(
						status.getStatusCode(), 
						name, 
						type.name(), 
						type.getMessage())).build());
	}
	
	public OpenInfraWebException(Exception ex) {
		super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(
				new OpenInfraExceptionItem(
						Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
						ex.getClass().getName(), 
						OpenInfraExceptionTypes
							.INTERNAL_SERVER_EXCEPTION.name(), 
						ex.getMessage())).build());
		ex.printStackTrace();
	}

	public OpenInfraExceptionTypes getType() {
		return type;
	}

	public void setType(OpenInfraExceptionTypes type) {
		this.type = type;
	}

}
