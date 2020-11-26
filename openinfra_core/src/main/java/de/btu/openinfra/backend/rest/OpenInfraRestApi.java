package de.btu.openinfra.backend.rest;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.glassfish.jersey.server.wadl.WadlApplicationContext;

/**
 * This class is used to generate a HTML view out of the XML-based rest API
 * documentation (WADL).
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path("/application.html")
@Singleton
@Produces({MediaType.TEXT_HTML})
public class OpenInfraRestApi {

	/**
	 * This variable defines the path to the XSL file.
	 */
	private static final String XSL_FILE =
			"de/btu/openinfra/backend/xsl/wadl.xsl";

	private WadlApplicationContext wadlContext;

	/**
	 * This constructor is used to inject the WADL application context which
	 * is used to instantiate the JAXB marshaller.
	 *
	 * @param wadlContext the application context
	 */
	public OpenInfraRestApi(@Context WadlApplicationContext wadlContext) {
        this.wadlContext = wadlContext;
    }

	/**
	 * The HTML page you're seeing is the result of this method. <br/>
	 * This method is used to transform the WADL into HTML output. Therefore,
	 * the injected URI info is used to retrieve the application. The
	 * application is used to generate the entire WADL as XML. Afterwards, a
	 * specific XSTL file is loaded which is subsequently used to transform the
	 * WADL into HTML.
	 *
	 * @param uriInfo
	 * @return Response object.
	 */
	@GET
	public Response getRestApi(@Context UriInfo uriInfo) {

		String wadlXmlRepresentation = "";
		try {
			final Marshaller marshaller =
					wadlContext.getJAXBContext().createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        final ByteArrayOutputStream os = new ByteArrayOutputStream();
	        // use the entire application as resource
	        marshaller.marshal(wadlContext.getApplication(
	        		uriInfo,
	        		false).getApplication(), os);
	        wadlXmlRepresentation = os.toString();
	        os.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		TransformerFactory tf = TransformerFactory.newInstance();
		StreamSource xslt = new StreamSource(
				new OpenInfraRestApi(wadlContext).getClass().getClassLoader()
					.getResourceAsStream(XSL_FILE));
		StreamResult result = new StreamResult(stream);
		Transformer tr;

		try {
			tr = tf.newTransformer(xslt);
			tr.transform(new StreamSource(new StringReader(
					wadlXmlRepresentation)), result);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return Response.ok().entity(stream.toByteArray()).build();
	}

}
