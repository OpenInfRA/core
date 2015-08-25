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
import de.btu.openinfra.backend.db.daos.meta.LoggerDao;
import de.btu.openinfra.backend.db.pojos.meta.LoggerPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/logger")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class LoggerResource {

    @GET
    public List<LoggerPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new LoggerDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{loggerId}")
    public LoggerPojo get(@PathParam("loggerId") UUID loggerId) {
        return new LoggerDao(OpenInfraSchemas.META_DATA).read(null, loggerId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new LoggerDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(LoggerPojo pojo) {
        UUID id = new LoggerDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{loggerId}")
    public Response update(
            @PathParam("loggerId") UUID loggerId,
            LoggerPojo pojo) {
        UUID id = new LoggerDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, loggerId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{loggerId}")
    public Response delete(@PathParam("loggerId") UUID loggerId) {
        boolean deleteResult =
                new LoggerDao(OpenInfraSchemas.META_DATA).delete(loggerId);
        return OpenInfraResponseBuilder.deleteResponse(deleteResult, loggerId);
    }

    @GET
    @Path("/new")
    public LoggerPojo newLogger() {
        return new LoggerDao(OpenInfraSchemas.META_DATA).newLogger();
    }

}
