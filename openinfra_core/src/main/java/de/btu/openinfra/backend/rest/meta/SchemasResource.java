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
import de.btu.openinfra.backend.db.daos.meta.SchemasDao;
import de.btu.openinfra.backend.db.pojos.meta.SchemasPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/schemas")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class SchemasResource {

    @GET
    public List<SchemasPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new SchemasDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{schemasId}")
    public SchemasPojo get(@PathParam("schemasId") UUID schemasId) {
        return new SchemasDao(OpenInfraSchemas.META_DATA).read(
                        null,
                        schemasId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new SchemasDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(SchemasPojo pojo) {
        UUID id = new SchemasDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{schemasId}")
    public Response update(
            @PathParam("schemasId") UUID schemasId,
            SchemasPojo pojo) {
        UUID id = new SchemasDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, schemasId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{schemasId}")
    public Response delete(@PathParam("schemasId") UUID schemasId) {
        boolean deleteResult =
                new SchemasDao(OpenInfraSchemas.META_DATA).delete(
                        schemasId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                schemasId);
    }

    @GET
    @Path("/new")
    public SchemasPojo newSchemas() {
        return new SchemasDao(OpenInfraSchemas.META_DATA).newSchemas();
    }

}
