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
import de.btu.openinfra.backend.db.daos.AttributeTypeAssociationDao;
import de.btu.openinfra.backend.db.daos.AttributeTypeDao;
import de.btu.openinfra.backend.db.daos.AttributeTypeGroupToAttributeTypeDao;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.pojos.AttributeTypeAssociationPojo;
import de.btu.openinfra.backend.db.pojos.AttributeTypeGroupToAttributeTypePojo;
import de.btu.openinfra.backend.db.pojos.AttributeTypePojo;

@Path(OpenInfraResponseBuilder.REST_URI + "/attributetypes")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AttributeTypeResource {

	@GET
	public List<AttributeTypePojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderByEnum orderBy,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						sortOrder,
						orderBy,
						offset,
						size);
	}

	@GET
	@Path("{attributeTypeId}/associations")
	public List<AttributeTypeAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new AttributeTypeAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeId,
						offset,
						size);
	}

	@GET
    @Path("{attributeTypeId}/associations/count")
	@Produces({MediaType.TEXT_PLAIN})
    public long getAssociationsCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeId") UUID attributeTypeId) {

        return new AttributeTypeAssociationDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
                        attributeTypeId);
    }

	@GET
	@Path("{attributeTypeId}/associations/{associatedAttributeTypeId}")
	public List<AttributeTypeAssociationPojo> getAssociations(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId,
			@PathParam("associatedAttributeTypeId")
				UUID associatedAttributeTypeId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new AttributeTypeAssociationDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeId,
						associatedAttributeTypeId,
						offset,
						size);
	}

	@GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema) {
		return new AttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount();
	}

	@GET
	@Path("{attributeTypeId}")
	public AttributeTypePojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId) {
		return new AttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeId);
	}

	@GET
	@Path("{attributeTypeId}/attributetypegroups")
	public List<AttributeTypeGroupToAttributeTypePojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeGroupToAttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeId,
						offset,
						size);
	}

	@GET
	@Path("{attributeTypeId}/attributetypegroups/count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getAttributeTypeGroupsCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId) {
		return new AttributeTypeGroupToAttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
						attributeTypeId);
	}

	@GET
	@Path("{attributeTypeId}/attributetypegroups/{attributeTypeGroupId}")
	public List<AttributeTypeGroupToAttributeTypePojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeId") UUID attributeTypeId,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeGroupToAttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeId,
						attributeTypeGroupId,
						offset,
						size);
	}

	@GET
    @Path("/new")
    public AttributeTypePojo newAttributeType(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
        return new AttributeTypeDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .newAttributeType(PtLocaleDao.forLanguageTag(language));
    }

    @POST
    public Response create(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            AttributeTypePojo pojo) {
        UUID id = new AttributeTypeDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @DELETE
    @Path("{attributeTypeId}")
    public Response delete(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeId") UUID attributeTypeId) {
        return OpenInfraResponseBuilder.deleteResponse(
                new AttributeTypeDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase())).delete(
                                attributeTypeId),
                        attributeTypeId);
    }

    @PUT
    @Path("{attributeTypeId}")
    public Response update(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeId") UUID attributeTypeId,
            AttributeTypePojo pojo) {
        return OpenInfraResponseBuilder.putResponse(
                new AttributeTypeDao(
                        projectId,
                        OpenInfraSchemas.PROJECTS).createOrUpdate(pojo,
                                attributeTypeId, pojo.getMetaData()));
    }

    @GET
    @Path("{attributeTypeId}/associations/new")
    public AttributeTypeAssociationPojo newAttributeTypeAssociation(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeId") UUID attributeTypeId) {
        return new AttributeTypeAssociationDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .newAttributeTypeAssociation(attributeTypeId);
    }

    @POST
    @Path("{attributeTypeId}/associations")
    public Response createAssociation(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeId") UUID attributeTypeId,
            AttributeTypeAssociationPojo pojo) {
        UUID id = new AttributeTypeAssociationDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, attributeTypeId,
                        pojo.getAssociationAttributeTypeId(),
                        pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @DELETE
    @Path("{attributeTypeId}/associations/{associatedAttributeTypeId}")
    public Response deleteAssociation(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("associatedAttributeTypeId")
                UUID associatedAttributeTypeId) {
        return OpenInfraResponseBuilder.deleteResponse(
                new AttributeTypeAssociationDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase())).delete(
                                associatedAttributeTypeId),
                                associatedAttributeTypeId);
    }

    @PUT
    @Path("{attributeTypeId}/associations/{associatedAttributeTypeId}")
    public Response updateAssociation(
            @PathParam("projectId") UUID projectId,
            @PathParam("attributeTypeId") UUID attributeTypeId,
            @PathParam("associatedAttributeTypeId")
                UUID associatedAttributeTypeId,
            @PathParam("schema") String schema,
            AttributeTypeAssociationPojo pojo) {
        return OpenInfraResponseBuilder.putResponse(
                new AttributeTypeAssociationDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                            .createOrUpdate(
                                pojo, attributeTypeId,
                                pojo.getAssociationAttributeTypeId(),
                                associatedAttributeTypeId,
                                pojo.getAssociatedAttributeType().getUuid(),
                                pojo.getMetaData()));
    }
}
