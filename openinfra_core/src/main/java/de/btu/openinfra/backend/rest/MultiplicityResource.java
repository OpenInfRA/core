package de.btu.openinfra.backend.rest;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.MultiplicityDao;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.pojos.MultiplicityPojo;

/**
 * This class represents and implements the Multiplicity resource.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path(OpenInfraResponseBuilder.REST_URI + "/multiplicities")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MultiplicityResource {

	@GET
	public List<MultiplicityPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new MultiplicityDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						offset,
						size);
	}

	@GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getMultiplicityCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
        return new MultiplicityDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount();
    }

	@GET
	@Path("{multiplicityId}")
	public MultiplicityPojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("multiplicityId") UUID multiplicityId) {
		return new MultiplicityDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						multiplicityId);
	}

	@GET
    @Path("/new")
    public MultiplicityPojo newMultiplicity(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
	    return new MultiplicityPojo();
    }

	@POST
	public Response create(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			MultiplicityPojo pojo) {
		UUID id = new MultiplicityDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
						pojo, pojo.getMetaData());
		return OpenInfraResponseBuilder.postResponse(id);
	}

	@DELETE
	@Path("{multiplicityId}")
	public Response delete(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("multiplicityId") UUID multiplicityId) {
		return OpenInfraResponseBuilder.deleteResponse(
				new MultiplicityDao(
						projectId,
						OpenInfraSchemas.valueOf(schema.toUpperCase())).delete(
								multiplicityId),
				multiplicityId);
	}

	@PUT
	@Path("{multiplicityId}")
	public Response update(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("multiplicityId") UUID multiplicityId,
			MultiplicityPojo pojo) {
		return OpenInfraResponseBuilder.postResponse(
				new MultiplicityDao(
						projectId,
						OpenInfraSchemas.PROJECTS).createOrUpdate(pojo,
						        multiplicityId, pojo.getMetaData()));
	}

}
