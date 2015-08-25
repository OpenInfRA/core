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

import de.btu.openinfra.backend.db.OpenInfraOrderByEnum;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.daos.ValueListAssociationDao;
import de.btu.openinfra.backend.db.daos.ValueListDao;
import de.btu.openinfra.backend.db.daos.ValueListValueDao;
import de.btu.openinfra.backend.db.pojos.ValueListAssociationPojo;
import de.btu.openinfra.backend.db.pojos.ValueListPojo;
import de.btu.openinfra.backend.db.pojos.ValueListValuePojo;

@Path(OpenInfraResponseBuilder.REST_URI + "/valuelists")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ValueListResource {

	@GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getValueListsCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema) {
		return new ValueListDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount();
	}

	@GET
	public List<ValueListPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderByEnum orderBy,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new ValueListDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						sortOrder,
						orderBy,
						offset,
						size);
	}

	@GET
	@Path("{valueListId}/associations")
	public List<ValueListAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListId") UUID valueListId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new ValueListAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListId,
						offset,
						size);
	}

	@GET
    @Path("{valueListId}/associations/count")
	@Produces({MediaType.TEXT_PLAIN})
    public long getAssociationsCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("valueListId") UUID valueListId) {

        return new ValueListAssociationDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
                        valueListId);
    }

	@GET
	@Path("{valueListId}/associations/{associatedValueListId}")
	public List<ValueListAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListId") UUID valueListId,
			@PathParam("associatedValueListId") UUID associatedValueListId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new ValueListAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListId,
						associatedValueListId,
						offset,
						size);
	}

	@POST
	public Response createValueList(
	        @PathParam("projectId") UUID projectId,
	        @PathParam("schema") String schema,
	        ValueListPojo pojo) {
	    // call the create or update method for the DAO and return the uuid
	    return OpenInfraResponseBuilder.postResponse(
	                new ValueListDao(
	                        projectId,
	                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
	                        .createOrUpdate(pojo, pojo.getMetaData()));
	}

	@GET
	@Path("{valueListId}")
	public ValueListPojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListId") UUID valueListId) {
		return new ValueListDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListId);
	}

	@DELETE
	@Path("{valueListId}")
	public Response delete(
	        @PathParam("projectId") UUID projectId,
	        @PathParam("valueListId") UUID valueListId,
            @PathParam("schema") String schema) {
	    return OpenInfraResponseBuilder.deleteResponse(
	            new ValueListDao(
	                    projectId,
	                    OpenInfraSchemas.valueOf(schema.toUpperCase()))
	                .delete(valueListId),
	                valueListId);
	}

	@GET
    @Path("/new")
    public ValueListPojo newValueList(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
        return new ValueListDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .newValueList(PtLocaleDao.forLanguageTag(language));
    }

	@PUT
    @Path("{valueListId}")
    public Response update(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("valueListId") UUID valueListId,
            ValueListPojo pojo) {
	    UUID uuid = new ValueListDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, valueListId, pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(uuid);
    }

	@GET
	@Path("{valueListId}/valuelistvalues")
	public List<ValueListValuePojo> getValueListValues(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListId") UUID valueListId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new ValueListValueDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						valueListId,
						offset,
						size);
	}

	@POST
    @Path("{valueListId}/valuelistvalues")
	public Response createValueListValue(
            @PathParam("projectId") UUID projectId,
            @PathParam("valueListId") UUID valueListId,
            @PathParam("schema") String schema,
            ValueListValuePojo pojo) {
        // call the create or update method for the DAO and return the uuid
        return OpenInfraResponseBuilder.postResponse(
                    new ValueListValueDao(
                            projectId,
                            OpenInfraSchemas.valueOf(schema.toUpperCase()))
                            .createOrUpdate(pojo, valueListId,
                                    pojo.getMetaData()));
	}

	@GET
	@Path("{valueListId}/valuelistvalues/count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getValueListValuesCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("valueListId") UUID valueListId) {
		return new ValueListValueDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
						valueListId);
	}

}
