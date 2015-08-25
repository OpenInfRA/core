package de.btu.openinfra.backend.rest.meta;

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

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.meta.LevelDao;
import de.btu.openinfra.backend.db.pojos.meta.LevelPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/level")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class LevelResource {

    @GET
    public List<LevelPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new LevelDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{levelId}")
    public LevelPojo get(@PathParam("levelId") UUID levelId) {
        return new LevelDao(OpenInfraSchemas.META_DATA).read(null, levelId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new LevelDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(LevelPojo pojo) {
        UUID id = new LevelDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{levelId}")
    public Response update(@PathParam("levelId") UUID levelId, LevelPojo pojo) {
        UUID id = new LevelDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, levelId, null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{levelId}")
    public Response delete(@PathParam("levelId") UUID levelId) {
        boolean deleteResult =
                new LevelDao(OpenInfraSchemas.META_DATA).delete(levelId);
        return OpenInfraResponseBuilder.deleteResponse(deleteResult, levelId);
    }

    @GET
    @Path("/new")
    public LevelPojo newLevel() {
        return new LevelDao(OpenInfraSchemas.META_DATA).newLevel();
    }

}
