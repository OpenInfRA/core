package de.btu.openinfra.backend.rest.view.project;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.mvc.Template;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.pojos.ProjectPojo;
import de.btu.openinfra.backend.db.rbac.OpenInfraHttpMethod;
import de.btu.openinfra.backend.db.rbac.ProjectRbac;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

/**
 * This class represents the project resource of the REST API. You can access
 * an overview of all available HTTP methods by calling the following link:
 * <a href="http://localhost:8080/openinfra_backend/rest/application.wadl">
 * http://localhost:8080/openinfra_backend/rest/application.wadl</a>
 *
 * The project resource is available by calling the following link:
 * <a href="http://localhost:8080/openinfra_backend/rest/projects">
 * http://localhost:8080/openinfra_backend/rest/projects</a>
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path("/v1/projects")
@Produces(MediaType.TEXT_HTML +
		OpenInfraResponseBuilder.UTF8_CHARSET +
		OpenInfraResponseBuilder.HTML_PRIORITY)
public class ProjectResource {

	@GET
	@Template(name="/views/list/Projects.jsp")
	public List<ProjectPojo> get(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@QueryParam("language") String language,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new de.btu.openinfra.backend.rest.project.ProjectResource().get(
				uriInfo,
				request,
				language, 
				offset, 
				size);
	}

	@GET
	@Path("{projectId}")
	@Template(name="/views/detail/Projects.jsp")
	public Response getView(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId) {
		return new de.btu.openinfra.backend.rest.project.ProjectResource().get(
				uriInfo,
				request,
				language, 
				projectId);
	}

	@GET
	@Path("{projectId}/subprojects")
	@Template(name="/views/list/Projects.jsp")
	public Response getSubProjectsView(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return OpenInfraResponseBuilder.getResponse(
					new ProjectRbac(
							projectId,
							OpenInfraSchemas.PROJECTS).readSubProjects(
									OpenInfraHttpMethod.valueOf(
											request.getMethod()),
									uriInfo,
									PtLocaleDao.forLanguageTag(language),
									offset,
									size));
	}
	
	@GET
	@Path("{projectId}/files")
	@Template(name="/views/Upload.jsp")
	@Produces(MediaType.TEXT_HTML +
			OpenInfraResponseBuilder.UTF8_CHARSET +
			OpenInfraResponseBuilder.HTML_PRIORITY)
    public Response files() {
        return Response.ok().entity("file upload").build();
    }

	@GET
	@Path("/maps")
	@Template(name="/views/Map.jsp")
	public Response getMaps(
			@QueryParam("language") String language,
			@PathParam("tc") UUID projectId) {
		return Response.ok().entity("maps").build();
	}

	@GET
    @Path("/3d")
	@Template(name="/views/3dWebGIS.jsp")
    public Response index() {
        return Response.ok().entity("3dWebgis").build();
    }
}
