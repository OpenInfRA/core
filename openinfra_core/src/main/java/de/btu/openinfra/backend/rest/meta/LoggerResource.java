package de.btu.openinfra.backend.rest.meta;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.btu.openinfra.backend.db.OpenInfraOrderBy;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.pojos.meta.LoggerPojo;
import de.btu.openinfra.backend.db.rbac.OpenInfraHttpMethod;
import de.btu.openinfra.backend.db.rbac.meta.LoggerRbac;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;

/**
 * This class represents and implements the resource for logger in the meta data
 * schema. They are used to determine the logger that initiated the log event.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path(OpenInfraResponseBuilder.REST_URI_METADATA + "/logger")
@Produces({MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
    MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET})
public class LoggerResource {

    /**
     * This resource provides a list of all LoggerPojo's. This resource supports
     * sorting and pagination of the list.
     * <ul>
     *   <li>rest/v1/metadata/logger?orderBy=LOGGER&sortOrder=ASC&offset=0&size=10</li>
     * </ul>
     *
     * @param uriInfo
     * @param request
     * @param sortOrder The sort order for the list.
     * @param orderBy   The element the list should be ordered by.
     * @param offset    The offset parameter for the elements of the list.
     * @param size      The count of elements the list should contain.
     * @return          A list of LoggerPojo's
     *
     * @response.representation.200.qname A list of LoggerPojo's.
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.409.qname OpenInfraEntityException
     * @response.representation.409.doc   This error occurs if the parameters
     *                                    are not configured as expected.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @GET
    public List<LoggerPojo> get(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request,
            @QueryParam("sortOrder") OpenInfraSortOrder sortOrder,
            @QueryParam("orderBy") OpenInfraOrderBy orderBy,
            @QueryParam("offset") int offset,
            @QueryParam("size") int size) {
        return new LoggerRbac().read(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo,
                null,
                sortOrder,
                orderBy,
                offset, size);
    }

    /**
     * This resource provides a LoggerPojo for the specified UUID.
     * <ul>
     *   <li>rest/v1/metadata/logger/[uuid]</li>
     * </ul>
     *
     * @param uriInfo
     * @param request
     * @param loggerId The UUID of the LoggerPojo that should be retrieved.
     * @return         The specified LoggerPojo.
     *
     * @response.representation.200.qname A specified LoggerPojo.
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @GET
    @Path("{loggerId}")
    public LoggerPojo get(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request,
            @PathParam("loggerId") UUID loggerId) {
        return new LoggerRbac().read(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo,
                null,
                loggerId);
    }

    /**
     * This resource provides the count of LoggerPojo's in the whole system.
     * <ul>
     *   <li>rest/v1/metadata/logger/count</li>
     * </ul>
     *
     * @param uriInfo
     * @param request
     * @return        The count of LoggerPojo's.
     *
     * @response.representation.200.qname The count of LoggerPojo's as long.
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public long getCount(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request) {
        return new LoggerRbac().getCount(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo);
    }

    /**
     * This resource creates a new logger object. The specified LoggerPojo must
     * contain the logger as string. The parameter UUID and TRID of the
     * LoggerPojo must not be set.
     * <ul>
     *   <li>rest/v1/metadata/logger</li>
     * </ul>
     *
     * @param uriInfo
     * @param request
     * @param pojo    The LoggerPojo that represents the new object.
     * @return        A Response with the UUID of the new created object or the
     *                status code 204.
     *
     * @response.representation.200.qname Response
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.204.qname Response
     * @response.representation.204.doc   If the object could not be created.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @POST
    public Response create(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request,
            LoggerPojo pojo) {
        UUID id = new LoggerRbac().createOrUpdate(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo,
                null,
                pojo);
        return OpenInfraResponseBuilder.postResponse(id);
    }

    /**
     * This resource updates the Logger object with the specified UUID. The
     * specified LoggerPojo must contain the UUID of the object that should be
     * updated, the TRID and the logger as string.
     * <ul>
     *   <li>rest/v1/metadata/logger/[uuid]</li>
     * </ul>
     * <b>The object id in the LoggerPojo and in the URI that identifies the
     * logger must concur.</b>
     *
     * @param uriInfo
     * @param request
     * @param loggerId The UUID of the LoggerPojo that should be updated.
     * @param pojo     The LoggerPojo that represents the updated object.
     * @return         A Response with the status code 200 for a successful
     *                 update or 204 if the object could not be updated.
     *
     * @response.representation.200.qname Response
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.204.qname Response
     * @response.representation.204.doc   If the object could not be updated.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.409.qname OpenInfraEntityException
     * @response.representation.409.doc   This error occurs if the parameters
     *                                    are not configured as expected.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @PUT
    @Path("{loggerId}")
    public Response update(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request,
            @PathParam("loggerId") UUID loggerId,
            LoggerPojo pojo) {
        UUID id = new LoggerRbac().createOrUpdate(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo,
                loggerId,
                pojo);
        return OpenInfraResponseBuilder.putResponse(id);
    }

    /**
     * This resource deletes the Logger object with the specified UUID.
     * <ul>
     *   <li>rest/v1/metadata/logger/[uuid]</li>
     * </ul>
     *
     * @param uriInfo
     * @param request
     * @param loggerId The UUID of the Logger object that should be deleted.
     * @return         A Response with the status code 200 for a successful
     *                 deletion or 404 if the object was not found.
     *
     * @response.representation.200.qname Response
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.403.qname WebApplicationException
     * @response.representation.403.doc   This error occurs if you do not have
     *                                    the permission to access this
     *                                    resource.
     *
     * @response.representation.404.qname Response
     * @response.representation.404.doc   If the object could not be deleted
     *                                    because it was not found.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @DELETE
    @Path("{loggerId}")
    public Response delete(
            @Context UriInfo uriInfo,
            @Context HttpServletRequest request,
            @PathParam("loggerId") UUID loggerId) {
        boolean deleteResult = new LoggerRbac().delete(
                OpenInfraHttpMethod.valueOf(request.getMethod()),
                uriInfo,
                loggerId);
        return OpenInfraResponseBuilder.deleteResponse(deleteResult, loggerId);
    }

}
