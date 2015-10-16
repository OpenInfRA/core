package de.btu.openinfra.backend.rest.project.files;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/v1/projects")
public class OpenInfraFileUpload {
	
	@POST
	@Path("{projectId}/files/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
	        @FormDataParam("file") InputStream fileContent,
	        @FormDataParam("file") FormDataContentDisposition fileInformation) {
		
		System.out.println("--> " + fileInformation.getFileName());
		System.out.println("--> " + fileInformation.getType());
		System.out.println("--> " + fileInformation.getParameters());

	    return Response.ok().entity("uploaded").build();
	}

}
