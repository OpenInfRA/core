package de.btu.openinfra.backend.rest;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.daos.ValueListValueAssociationDao;
import de.btu.openinfra.backend.db.daos.ValueListValueDao;
import de.btu.openinfra.backend.db.pojos.ValueListValueAssociationPojo;
import de.btu.openinfra.backend.db.pojos.ValueListValuePojo;

@Path(OpenInfraResponseBuilder.REST_URI + "/valuelistvalues")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ValueListValuesResource {

	@GET
	@Path("{valueListValueId}")
	public ValueListValuePojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListValueId") UUID valueListValueId) {
		return new ValueListValueDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListValueId);
	}

	@GET
    @Path("/new")
    public ValueListValuePojo newAttributeValueValues(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
        return new ValueListValueDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .newAttributeValueValues(
                            PtLocaleDao.forLanguageTag(language));
    }

	@GET
	@Path("{valueListValueId}/associations")
	public List<ValueListValueAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListValueId") UUID valueListValueId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new ValueListValueAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListValueId,
						offset,
						size);
	}

	@GET
    @Path("{valueListValueId}/associations/count")
	@Produces({MediaType.TEXT_PLAIN})
    public long getAssociationsCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("valueListValueId") UUID valueListValueId) {

        return new ValueListValueAssociationDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
                        valueListValueId);
    }

	@GET
	@Path("{valueListValueId}/associations/{associatedValueListValueId}")
	public List<ValueListValueAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListValueId") UUID valueListValueId,
			@PathParam("associatedValueListValueId")
				UUID associatedValueListValueId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new ValueListValueAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListValueId,
						associatedValueListValueId,
						offset,
						size);
	}

	@PUT
    @Path("{valueListValueId}")
    public Response update(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("valueListValueId") UUID valueListValueId,
            ValueListValuePojo pojo) {
        UUID uuid = new ValueListValueDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, valueListValueId, pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(uuid);
    }

	@DELETE
    @Path("{valueListValueId}")
    public Response delete(
            @PathParam("projectId") UUID projectId,
            @PathParam("valueListValueId") UUID valueListValueId,
            @PathParam("schema") String schema) {
        return OpenInfraResponseBuilder.deleteResponse(
                new ValueListValueDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .delete(valueListValueId),
                    valueListValueId);
    }
}
