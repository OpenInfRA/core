package de.btu.openinfra.backend.exception;

import javax.ws.rs.core.Response.Status;

public class OpenInfraEntityException extends OpenInfraWebException {

	private static final long serialVersionUID = 1L;

	public OpenInfraEntityException(OpenInfraExceptionTypes type) {
		super(Status.CONFLICT, OpenInfraEntityException.class.getName(), 
				type);
	}

	
	
}
