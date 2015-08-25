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
import de.btu.openinfra.backend.db.daos.meta.ServersDao;
import de.btu.openinfra.backend.db.pojos.meta.ServersPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/servers")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class ServersResource {

    @GET
    public List<ServersPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new ServersDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{serversId}")
    public ServersPojo get(@PathParam("serversId") UUID serversId) {
        return new ServersDao(OpenInfraSchemas.META_DATA).read(
                        null,
                        serversId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new ServersDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(ServersPojo pojo) {
        UUID id = new ServersDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{serversId}")
    public Response update(
            @PathParam("serversId") UUID serversId,
            ServersPojo pojo) {
        UUID id = new ServersDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, serversId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{serversId}")
    public Response delete(@PathParam("serversId") UUID serversId) {
        boolean deleteResult =
                new ServersDao(OpenInfraSchemas.META_DATA).delete(
                        serversId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                serversId);
    }

    @GET
    @Path("/new")
    public ServersPojo newServers() {
        return new ServersDao(OpenInfraSchemas.META_DATA).newServers();
    }

}
