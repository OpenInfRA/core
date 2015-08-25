package de.btu.openinfra.backend.rest;

import java.util.List;
import java.util.UUID;

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
import de.btu.openinfra.backend.db.daos.AttributeTypeGroupToTopicCharacteristicDao;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;
import de.btu.openinfra.backend.db.daos.RelationshipTypeDao;
import de.btu.openinfra.backend.db.daos.RelationshipTypeToTopicCharacteristicDao;
import de.btu.openinfra.backend.db.daos.TopicCharacteristicDao;
import de.btu.openinfra.backend.db.pojos.AttributeTypeGroupToTopicCharacteristicPojo;
import de.btu.openinfra.backend.db.pojos.RelationshipTypeToTopicCharacteristicPojo;
import de.btu.openinfra.backend.db.pojos.TopicCharacteristicPojo;

@Path(OpenInfraResponseBuilder.REST_URI + "/topiccharacteristics")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
	MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
	+ OpenInfraResponseBuilder.UTF8_CHARSET})
public class TopicCharacteristicResource {

	@GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getTopicCharacteristicsCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema) {
		return new TopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount();
	}

	@GET
	public List<TopicCharacteristicPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@QueryParam("filter") String filter,
			@QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
			@QueryParam("orderBy") OpenInfraOrderByEnum orderBy,
			@PathParam("offset") int offset,
			@PathParam("size") int size) {
		if(filter != null && filter.length() > 0) {
			return new TopicCharacteristicDao(
					projectId,
					OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
							PtLocaleDao.forLanguageTag(language),
							filter);
		} else {
			return new TopicCharacteristicDao(
					projectId,
					OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
							PtLocaleDao.forLanguageTag(language),
							sortOrder,
							orderBy,
							offset,
							size);
		} // end if else
	}

	@GET
	@Path("{topicCharacteristicId}")
	public TopicCharacteristicPojo get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId) {
		return new TopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						topicCharacteristicId);
	}

	@GET
	@Path("{topicCharacteristicId}/attributetypegroups")
	public List<AttributeTypeGroupToTopicCharacteristicPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new AttributeTypeGroupToTopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						topicCharacteristicId,
						offset,
						size);
	}

	@GET
    @Path("{topicCharacteristicId}/attributetypegroups/count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getAttributeTypeGroupCount(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("topicCharacteristicId") UUID topicCharacteristicId) {
        return new AttributeTypeGroupToTopicCharacteristicDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
                        topicCharacteristicId);
    }

	@GET
	@Path("{topicCharacteristicId}/attributetypegroups/"
			+ "{attributeTypeGroupId}")
	public List<AttributeTypeGroupToTopicCharacteristicPojo> get(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId,
			@PathParam("attributeTypeGroupId") UUID attributeTypeGroupId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {
		return new AttributeTypeGroupToTopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						topicCharacteristicId,
						attributeTypeGroupId,
						offset,
						size);

	}

	@GET
	@Path("{topicCharacteristicId}/relationshiptypes")
	public List<RelationshipTypeToTopicCharacteristicPojo> getRelationshipTypes(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new RelationshipTypeToTopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						topicCharacteristicId,
						offset,
						size);
	}

	@GET
	@Path("{topicCharacteristicId}/relationshiptypes/{relationShipTypeId}")
	public List<RelationshipTypeToTopicCharacteristicPojo> getRelationshipTypes(
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId,
			@PathParam("relationShipTypeId") UUID relationShipTypeId,
			@QueryParam("offset") int offset,
			@QueryParam("size") int size) {

		return new RelationshipTypeToTopicCharacteristicDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).read(
						PtLocaleDao.forLanguageTag(language),
						topicCharacteristicId,
						relationShipTypeId,
						offset,
						size);
	}

	@GET
	@Path("{topicCharacteristicId}/relationshiptypes/count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getRelationshipTypeCount(
			@PathParam("projectId") UUID projectId,
			@PathParam("schema") String schema,
			@PathParam("topicCharacteristicId") UUID topicCharacteristicId) {
		return new RelationshipTypeDao(
				projectId,
				OpenInfraSchemas.valueOf(schema.toUpperCase())).getCount(
						topicCharacteristicId);
	}

	@POST
    public Response create(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            TopicCharacteristicPojo pojo) {
        UUID id = new TopicCharacteristicDao(
                projectId,
                OpenInfraSchemas.valueOf(schema.toUpperCase())).createOrUpdate(
                        pojo, pojo.getMetaData());
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{topicCharacteristicId}")
    public Response update(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("topicCharacteristicId") UUID topicCharacteristicId,
            TopicCharacteristicPojo pojo) {
        return OpenInfraResponseBuilder.postResponse(
                new TopicCharacteristicDao(
                        projectId,
                        OpenInfraSchemas.PROJECTS).createOrUpdate(pojo,
                                topicCharacteristicId, pojo.getMetaData()));
    }

    @DELETE
    @Path("{topicCharacteristicId}")
    public Response delete(
            @PathParam("projectId") UUID projectId,
            @PathParam("schema") String schema,
            @PathParam("topicCharacteristicId") UUID topicCharacteristicId) {
        return OpenInfraResponseBuilder.deleteResponse(
                new TopicCharacteristicDao(
                        projectId,
                        OpenInfraSchemas.valueOf(schema.toUpperCase())).delete(
                                topicCharacteristicId),
                                topicCharacteristicId);
    }

}
