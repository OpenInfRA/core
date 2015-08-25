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
import de.btu.openinfra.backend.db.daos.meta.DatabaseConnectionDao;
import de.btu.openinfra.backend.db.pojos.meta.DatabaseConnectionPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/dbconnections")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class DatabaseConnectionResource {

    @GET
    public List<DatabaseConnectionPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new DatabaseConnectionDao(
                OpenInfraSchemas.META_DATA).read(null, offset, size);
    }

    @GET
    @Path("{databaseConnectionId}")
    public DatabaseConnectionPojo get(
            @PathParam("databaseConnectionId") UUID databaseConnectionId) {
        return new DatabaseConnectionDao(
                OpenInfraSchemas.META_DATA).read(
                        null,
                        databaseConnectionId);
    }

    @GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getCount() {
		return new DatabaseConnectionDao(
				OpenInfraSchemas.META_DATA).getCount();
	}

    @POST
    public Response create(DatabaseConnectionPojo pojo) {
        UUID id = new DatabaseConnectionDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{databaseConnectionId}")
    public Response update(
            @PathParam("databaseConnectionId") UUID databaseConnectionId,
            DatabaseConnectionPojo pojo) {
        UUID id = new DatabaseConnectionDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo,
                        databaseConnectionId, null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{databaseConnectionId}")
    public Response delete(
            @PathParam("databaseConnectionId") UUID databaseConnectionId) {
        boolean deleteResult =
                new DatabaseConnectionDao(OpenInfraSchemas.META_DATA).delete(
                        databaseConnectionId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                databaseConnectionId);
    }

    @GET
    @Path("/new")
    public DatabaseConnectionPojo newDatabaseConncetion() {
        return new DatabaseConnectionDao(
                OpenInfraSchemas.META_DATA).newDatabaseConnection();
    }

}


