package de.btu.openinfra.backend.rest.pdf;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import de.btu.openinfra.backend.db.daos.project.AttributeValueGeomType;
import de.btu.openinfra.backend.db.pojos.project.TopicPojo;
import de.btu.openinfra.backend.rest.OpenInfraResponseBuilder;
import de.btu.openinfra.backend.rest.project.TopicInstanceResource;
import java.io.File;
import org.apache.fop.apps.FopFactoryBuilder;

/**
 * This class transforms XML files into PDF files.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Path(OpenInfraResponseBuilder.REST_URI_PROJECTS + "/topicinstances")
public class TopicPdfResource {

	/**
	 * This variable defines the path to the XSL file.
	 */
	private static final String XSL_FILE =
			"de/btu/openinfra/backend/xsl/TopicPdf.xsl";

	/**
	 * This method executes the transformation.
	 * http://www.e-zest.net/blog/integrating-apache-fop-with-java-project-to-generate-pdf-files/
	 *
	 * @param language
	 * @param projectId
	 * @param topicInstanceId
	 * @param geomType
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Produces({"application/pdf"})
	@Path("{topicInstanceId}/topic.pdf")
	public Response getTopicAsPdf(
			@Context UriInfo uriInfo,
			@Context HttpServletRequest request,
			@QueryParam("language") String language,
			@PathParam("projectId") UUID projectId,
			@PathParam("topicInstanceId") UUID topicInstanceId,
			@QueryParam("geomType") AttributeValueGeomType geomType) {

		// Don't care about accessing the RBAC system classes. Just use the
		// resource class to retrieve the requested object.
		TopicPojo pojo = new TopicInstanceResource().get(
				uriInfo,
				request,
				language,
				projectId,
				topicInstanceId,
				geomType);

		// Use JAXB to retrieve XML output.
		String xml = "";
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jaxb = JAXBContext.newInstance(TopicPojo.class);
			Marshaller m = jaxb.createMarshaller();
			m.marshal(pojo , sw);
			xml = sw.toString();
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// Use formatted objects (FOP) and a specific XSL file in order to
		// generate PDF output.
		Fop fop;
		FopFactory fopf = FopFactory.newInstance(new File(".").toURI());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Transformer xslfoTransformer;

		try {
			fop = fopf.newFop(
					MimeConstants.MIME_PDF,
					fopf.newFOUserAgent(),
					stream);

			Result res = new SAXResult(fop.getDefaultHandler());

			TransformerFactory transfact = TransformerFactory.newInstance();
			xslfoTransformer =
					transfact.newTransformer(
							new StreamSource(
									new TopicPdfResource().getClass()
									.getClassLoader().getResourceAsStream(
											XSL_FILE)));
			xslfoTransformer.transform(
					new StreamSource(new StringReader(xml)), res);

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return Response.ok().entity(stream.toByteArray())
				.type("application/pdf").build();
	}

}
