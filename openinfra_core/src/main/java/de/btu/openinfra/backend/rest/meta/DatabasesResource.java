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
import de.btu.openinfra.backend.db.daos.meta.DatabasesDao;
import de.btu.openinfra.backend.db.pojos.meta.DatabasesPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/databases")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class DatabasesResource {

    @GET
    public List<DatabasesPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new DatabasesDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{databasesId}")
    public DatabasesPojo get(@PathParam("databasesId") UUID databasesId) {
        return new DatabasesDao(OpenInfraSchemas.META_DATA).read(
                null,
                databasesId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new DatabasesDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(DatabasesPojo pojo) {
        UUID id = new DatabasesDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{databasesId}")
    public Response update(
            @PathParam("databasesId") UUID databasesId,
            DatabasesPojo pojo) {
        UUID id = new DatabasesDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, databasesId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{databasesId}")
    public Response delete(@PathParam("databasesId") UUID databasesId) {
        boolean deleteResult =
                new DatabasesDao(OpenInfraSchemas.META_DATA).delete(
                        databasesId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                databasesId);
    }

    @GET
    @Path("/new")
    public DatabasesPojo newDatabases() {
        return new DatabasesDao(OpenInfraSchemas.META_DATA).newDatabases();
    }

}
