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
import de.btu.openinfra.backend.db.daos.meta.SettingsDao;
import de.btu.openinfra.backend.db.pojos.meta.SettingsPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

@Path("/metadata/settings")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class SettingsResource {

    @GET
    public List<SettingsPojo> get(
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new SettingsDao(
                OpenInfraSchemas.META_DATA).read(null, offset, size);
    }

    @GET
    @Path("{settingsId}")
    public SettingsPojo get(
            @PathParam("settingsId") UUID settingsId) {
        return new SettingsDao(
                OpenInfraSchemas.META_DATA).read(
                        null,
                        settingsId);
    }

    @GET
	@Path("count")
	@Produces({MediaType.TEXT_PLAIN})
	public long getCount() {
		return new SettingsDao(
				OpenInfraSchemas.META_DATA).getCount();
	}

    @POST
    public Response create(SettingsPojo pojo) {
        UUID id = new SettingsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    @PUT
    @Path("{settingsId}")
    public Response update(
            @PathParam("settingsId") UUID settingsId,
            SettingsPojo pojo) {
        UUID id = new SettingsDao(
                OpenInfraSchemas.META_DATA).createOrUpdate(pojo, settingsId,
                        null);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    @DELETE
    @Path("{settingsId}")
    public Response delete(
            @PathParam("settingsId") UUID settingsId) {
        boolean deleteResult =
                new SettingsDao(OpenInfraSchemas.META_DATA).delete(
                        settingsId);
        return OpenInfraResponseBuilder.deleteResponse(
                deleteResult,
                settingsId);
    }

    @GET
    @Path("/new")
    public SettingsPojo newSettings() {
        return new SettingsDao(OpenInfraSchemas.META_DATA).newSettings();
    }

}
