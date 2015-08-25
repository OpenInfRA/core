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
import de.btu.openinfra.backend.db.daos.meta.CredentialsDao;
import de.btu.openinfra.backend.db.pojos.meta.CredentialsPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/credentials")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class CredentialsResource {

    @GET
    public List<CredentialsPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new CredentialsDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{credentialsId}")
    public CredentialsPojo get(@PathParam("credentialsId") UUID credentialsId) {
        return new CredentialsDao(OpenInfraSchemas.META_DATA).read(
                null,
                credentialsId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new CredentialsDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(CredentialsPojo pojo) {
        UUID id = new CredentialsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{credentialsId}")
    public Response update(
            @PathParam("credentialsId") UUID credentialsId,
            CredentialsPojo pojo) {
        UUID id = new CredentialsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, credentialsId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{credentialsId}")
    public Response delete(@PathParam("credentialsId") UUID credentialsId) {
        boolean deleteResult =
                new CredentialsDao(OpenInfraSchemas.META_DATA).delete(
                        credentialsId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                credentialsId);
    }

    @GET
    @Path("/new")
    public CredentialsPojo newCredentials() {
        return new CredentialsDao(OpenInfraSchemas.META_DATA).newCredentials();
    }

}
