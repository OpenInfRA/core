package de.btu.openinfra.backend;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

/**
 * This class is represents the main class of the OpenInfRA application. It is
 * used to configure application specific stuff such as the Jersey JAX-RS and
 * HTML template engines. This class is referenced in the web.xml file and will
 * be called during application start up.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraApplication extends ResourceConfig {
	// alex sein test dingens
	/**
	 * This variable defines the used persistence context.
	 */
	public static final String PERSISTENCE_CONTEXT = "openinfra_core";

	/**
	 * This is the default non argument constructor which calls the super
	 * constructor and sets configuration specific arguments such as the
	 * resource packages and the used template engine.
	 */
	public OpenInfraApplication() {

		super();
		
		System.out.println("Debug test -- github");

		// Register the JSP template engine
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/jsp/");
		register(JspMvcFeature.class);

		// Register packages which include the URI mapping
		// This is done recursively
		packages("de.btu.openinfra.backend.rest");
		packages("de.btu.openinfra.plugins.solr.rest");
	}

	/**
	 * This method delivers the OpenInfRA version provided by the Maven
	 * pom file. In order to change the version number change it in the
	 * pom.xml file.
	 *
	 * @param sc the current servlet context
	 * @return   the current version as String
	 */
	public static String getOpenInfraVersion(ServletContext sc) {
		Properties prop = new Properties();
		try {
			prop.load(sc.getResourceAsStream("META-INF/maven/"
					+ "de.btu.openinfra.core/openinfra_core/pom.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		} // end try catch
		return prop.getProperty("version");
	}
}
