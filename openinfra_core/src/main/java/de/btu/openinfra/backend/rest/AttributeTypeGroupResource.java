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

import de.btu.openinfra.backend.db.OpenInfraOrderBy;
import de.btu.openinfra.backend.db.OpenInfraOrderByEnum;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.daos.AttributeTypeDao;
import de.btu.openinfra.backend.db.daos.AttributeTypeGroupDao;
import de.btu.openinfra.backend.db.daos.AttributeTypeToAttributeTypeGroupDao;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.daos.TopicCharacteristicToAttributeTypeGroupDao;
import de.btu.openinfra.backend.db.pojos.AttributeTypeGroupPojo;
import de.btu.openinfra.backend.db.pojos.AttributeTypeToAttributeTypeGroupPojo;
import de.btu.openinfra.backend.db.pojos.TopicCharacteristicToAttributeTypeGroupPojo;

/**
 * This class represents and implements the AttributeTypeGroupResource resource.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path(OpenInfraResponseBuilder.REST_URI + "/attributetypegroups")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AttributeTypeGroupResource {

	@GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getAttributeTypeGroupCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema) {
		return new AttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount();
	}

	@GET
	public List<AttributeTypeGroupPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderByEnum orderBy,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						sortOrder,
						orderBy,
						offset,
						size);
	}

	@GET
	@Path("{attributeTypeGroupId}")
	public AttributeTypeGroupPojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId) {
		return new AttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId);
	}

	@GET
	@Path("{attributeTypeGroupId}/attributetypes")
	public List<AttributeTypeToAttributeTypeGroupPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderByEnum orderBy,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeToAttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId,
						offset,
						size);
	}

	@GET
	@Path("{attributeTypeGroupId}/attributetypes/count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getAttributeTypeCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId) {
		return new AttributeTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
						attributeTypeGroupId);
	}

	@GET
	@Path("{attributeTypeGroupId}/attributetypes/{attributeTypeId}")
	public List<AttributeTypeToAttributeTypeGroupPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@PathParam("attributeTypeId") UUID attributeTypeId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeToAttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId,
						attributeTypeId,
						offset,
						size);
	}

	@GET
	@Path("{attributeTypeGroupId}/topiccharacteristics")
	public List<TopicCharacteristicToAttributeTypeGroupPojo>
		getTopicCharacteristics(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderBy orderBy,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new TopicCharacteristicToAttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId,
						offset,
						size);
	}

	@GET
    @Path("{attributeTypeGroupId}/topiccharacteristics/count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getTopicCharacteristicCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("attributeTypeGroupId") UUID attributeTypeGroupId) {
	    return new TopicCharacteristicToAttributeTypeGroupDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
                        attributeTypeGroupId);
    }

	@GET
	@Path("{attributeTypeGroupId}/topiccharacteristics/{topicCharacteristicId}")
	public List<TopicCharacteristicToAttributeTypeGroupPojo>
		getTopicCharacteristics(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new TopicCharacteristicToAttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId,
						topicCharacteristicId,
						offset,
						size);
	}

	/**
	 * This method represents a specific HTTP GET method and is used to retrieve
	 * a list sub groups of an AttributeTypeGroup.
	 *
	 * @param projectId            the required project id
	 * @param attributeTypeGroupId the id of the required AttributeTypeGroup
	 * @return                     a list of AttributeTypeGroups (sub groups)
	 */
	@GET
	@Path("{attributeTypeGroupId}/subgroups")
	public List<AttributeTypeGroupPojo> getSubGroups(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId) {
		return new AttributeTypeGroupDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).readSubGroups(
						PtLocaleDao.forLanguageTag(language),
						attributeTypeGroupId);
	}

	@GET
    @Path("/new")
    public AttributeTypeGroupPojo newAttributeTypeGroup(
            @QueryParam("language") String language,
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema) {
        return new AttributeTypeGroupDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase()))
                    .newAttributeTypeGroup(
                            PtLocaleDao.forLanguageTag(language));
    }

	@POST
    public Response create(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            AttributeTypeGroupPojo pojo) {
        UUID id = new AttributeTypeGroupDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(id);
    }

	@PUT
	@Path("{attributeTypeGroupId}")
	public Response update(
	        @PathParam("projectId") UUID projectId,
	        @PathParam("schema") String schema,
	        @PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
	        AttributeTypeGroupPojo pojo) {
	    UUID id = new AttributeTypeGroupDao(
	            projectId,
	            OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, attributeTypeGroupId, pojo.getMetaData());
	    return OpenInfraResponseBuilder.putResponse(id);
	}

	@DELETE
	@Path("{attributeTypeGroupId}")
	public Response delete(
	        @PathParam("projectId") UUID projectId,
	        @PathParam("schema") String schema,
	        @PathParam("attributeTypeGroupId") UUID attributeTypeGroupId) {
	    return OpenInfraResponseBuilder.deleteResponse(
	            new AttributeTypeGroupDao(
	                    projectId,
	                    OpenInfraSchemas.valueOf(schema.toUpperCase()))
	            .delete(attributeTypeGroupId), attributeTypeGroupId);
	}
}
