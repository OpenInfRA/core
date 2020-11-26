package de.btu.openinfra.backend.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.PojoPrimer;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;

/**
 * This class provides Pojo primer objects. It does not supports the schema
 * project. For the schema projects use the class
 * 'de.btu.openinfra.backend.rest.project/PrimerResource' instead. The
 * reason therefore lay in the fact that the regex expression
 * '/v1/{schema:(projects|system|rbac|metadata)}{optional:(/?)}
 * {projectId:([0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12})?}'
 * does not work for the schema projects.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path(OpenInfraResponseBuilder.REST_URI_PRIMER + "/primer")
@Produces({ MediaType.APPLICATION_JSON + OpenInfraResponseBuilder.JSON_PRIORITY
    + OpenInfraResponseBuilder.UTF8_CHARSET,
        MediaType.APPLICATION_XML + OpenInfraResponseBuilder.XML_PRIORITY
        + OpenInfraResponseBuilder.UTF8_CHARSET })
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class PrimerResource {

    /**
     * This resource provides a primer Pojo for the requested class. The primer
     * is an empty image of the requested class Pojo. This can be used as a base
     * for POST requests. Further the resource awaits a language definition to
     * fill in the correct language informations whenever they are required. If
     * the language parameter is not set the default language parameter defined
     * in the OpenInfRA.properties file will be used-
     * <ul>
     *   <li>rest/v1/system/primer?language=de-DE&pojoClass=attributeType</li>
     * </ul>
     *
     * @response.representation.200.qname The specified Pojo class.
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     *
     * @param schema    The schema name the request should be processed at.
     * @param pojoClass The name of the class a Pojo should be created for.
     * @param language  A java locale as string.
     * @return An OpenInfrPojo instance.
     */
    @GET
    public OpenInfraPojo primePojo(
            @PathParam("schema") String schema,
            @QueryParam("pojoClass") String pojoClass,
            @QueryParam("language") String language) {
        return PojoPrimer.primePojoStatically(
                (schema.toUpperCase().equals("METADATA"))
                    ? OpenInfraSchemas.META_DATA
                    : OpenInfraSchemas.valueOf(schema.toUpperCase()),
                null,
                language,
                pojoClass);
    }

    /**
     * This resource provides a list of class names that can be used as value
     * for the pojoClass parameter at the primer resource.
     * <ul>
     *   <li>rest/v1/system/primer?language=de-DE&pojoClass=attributeType</li>
     * </ul>
     *
     * @param schema The schema name the request should be processed at.
     * @return       A list of class names as string.
     *
     * @response.representation.200.qname A list of class names.
     * @response.representation.200.doc   This is the representation returned by
     *                                    default.
     *
     * @response.representation.500.qname OpenInfraWebException
     * @response.representation.500.doc   An internal error occurs if the
     *                                    backend runs into an unexpected
     *                                    exception.
     */
    @GET
    @Path("/names")
    public List<String> getPrimerNames(@PathParam("schema") String schema) {
        return PojoPrimer.getPrimerNamesStatically(
                (schema.toUpperCase().equals("METADATA"))
                ? OpenInfraSchemas.META_DATA
                : OpenInfraSchemas.valueOf(schema.toUpperCase()));
    }
}
