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
import de.btu.openinfra.backend.db.daos.meta.PortsDao;
import de.btu.openinfra.backend.db.pojos.meta.PortsPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/ports")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class PortsResource {

    @GET
    public List<PortsPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new PortsDao(OpenInfraSchemas.META_DATA).read(
                null,
                offset,
                size);
    }

    @GET
    @Path("{portsId}")
    public PortsPojo get(@PathParam("portsId") UUID portsId) {
        return new PortsDao(OpenInfraSchemas.META_DATA).read(null, portsId);
    }

    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount() {
        return new PortsDao(OpenInfraSchemas.META_DATA).getCount();
    }

    @POST
    public Response create(PortsPojo pojo) {
        UUID id = new PortsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{portsId}")
    public Response update(@PathParam("portsId") UUID portsId, PortsPojo pojo) {
        UUID id = new PortsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, portsId, null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{portsId}")
    public Response delete(@PathParam("portsId") UUID portsId) {
        boolean deleteResult =
                new PortsDao(OpenInfraSchemas.META_DATA).delete(portsId);
        return OpenInfraResponseBuilder.deleteResponse(deleteResult, portsId);
    }

    @GET
    @Path("/new")
    public PortsPojo newPorts() {
        return new PortsDao(OpenInfraSchemas.META_DATA).newPorts();
    }

}
