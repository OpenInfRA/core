package de.btu.openinfra.backend;

import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.daos.PtLocaleDao;

/**
 * OpenInfRA properties implemented as singleton.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraProperties {

	/**
	 * The properties object.
	 */
	private static Properties prop;

	/**
	 * This variable defines the location of the OpenInfRA properties file.
	 */
	private static final String PROPERTIES_FILE =
			"de/btu/openinfra/backend/properties/OpenInfRA.properties";

	/**
	 * This variable defines the default offset for lists. This should be used
	 * when the offset parameter is not specified in order to avoid infinite
	 * loading times.
	 */
	public static final int DEFAULT_OFFSET = Integer.parseInt(
            getProperty(OpenInfraPropertyKeys.DEFAULT_OFFSET.getKey()));

	/**
	 * This variable defines the default size for lists. This should be used
	 * when the size parameter is not specified in order to avoid infinite
	 * loading times.
	 */
	public static final int DEFAULT_SIZE = Integer.parseInt(
           getProperty(OpenInfraPropertyKeys.DEFAULT_SIZE.getKey()));

	/**
     * This variable defines the max size for lists. This should be used
     * when the resource must be restricted due to heavy load to the server.
     */
    public static final int MAX_SIZE = Integer.parseInt(
            getProperty(OpenInfraPropertyKeys.MAX_SIZE.getKey()));

    /**
     * This variable defines the default language.
     */
    public static final Locale DEFAULT_LANGUAGE = PtLocaleDao.forLanguageTag(
    		getProperty(OpenInfraPropertyKeys.DEFAULT_LANGUAGE.getKey()));

    /**
     * This variable defines the default sort order.
     */
    public static final OpenInfraSortOrder DEFAULT_ORDER =
    		OpenInfraSortOrder.valueOf(getProperty(
    				OpenInfraPropertyKeys.DEFAULT_ORDER.getKey()));
    
    /**
     * This variable defines the file path depending on the OS OpenInfRA is
     * currently running.
     */
    public static final String FILE_PATH = getFilePath();
    	
    /**
     * This is a simple method to decide on which OS OpenInfRA is running.
     * http://stackoverflow.com/questions/3282498/how-can-i-detect-a-unix-like-os-in-java
     * 
     * @return the specific file path
     */
    private static String getFilePath() {
        // includes: Windows 2000,  Windows 95, Windows 98, Windows NT,
    	// Windows Vista, Windows XP
    	// Otherwise return the unix file path
    	if (System.getProperty("os.name").startsWith("Windows")) {
    		String path = getProperty(
    				OpenInfraPropertyKeys.WIN_FILE_PATH.getKey());
    		if(!path.endsWith("\\")) {
    			path += "\\";
    		}
    		return path;
        } else {
        	String path = getProperty(
        			OpenInfraPropertyKeys.UNIX_FILE_PATH.getKey());
        	if(!path.endsWith("/")) {
        		path += "/";
        	}
            return path;
        } 
    }

	/**
	 * This method retrieves the required information from the property file
	 * located in the resource folder.
	 *
	 * @param key the key of the required value
	 * @return    the value referenced by the key
	 */
	public static String getProperty(String key) {
		if(prop == null) {
			prop = new Properties();
			try {
				prop.load(new OpenInfraProperties().getClass().getClassLoader()
						.getResourceAsStream(PROPERTIES_FILE));
			} catch (Exception e) {
				e.printStackTrace();
			} // end try catch
		} // end if
		return prop.getProperty(key);
	}

	/**
	 * This method retrieves the information that are necessary to connect to
	 * the central database. It contain the JDBC driver, user, password and
	 * database URL. It will retrieve the information from the
	 * OpenInfRA.properties file.
	 *
	 * @return connection properties for the central database
	 */
	public static HashMap<String, String> getConnectionProperties() {
	    HashMap<String, String> properties = new HashMap<String, String>();
	    // add the jdbc driver properties
	    properties.put(
                OpenInfraPropertyKeys.JDBC_DRIVER.getKey(),
                OpenInfraPropertyValues.JDBC_DRIVER.getValue());
	    // add the user properties
        properties.put(
                OpenInfraPropertyKeys.USER.getKey(),
                OpenInfraProperties.getProperty(
                        OpenInfraPropertyKeys.USER.getKey()));
        // add the password properties
        properties.put(
                OpenInfraPropertyKeys.PASSWORD.getKey(),
                OpenInfraProperties.getProperty(
                        OpenInfraPropertyKeys.PASSWORD.getKey()));
        // add the URL properties
        properties.put(
                OpenInfraPropertyKeys.URL.getKey(),
                String.format(
                    OpenInfraPropertyValues.URL.getValue(),
                    OpenInfraProperties.getProperty(
                            OpenInfraPropertyKeys.SERVER.getKey()),
                    OpenInfraProperties.getProperty(
                            OpenInfraPropertyKeys.PORT.getKey()),
                    OpenInfraProperties.getProperty(
                            OpenInfraPropertyKeys.DB_NAME.getKey())));

	    return properties;
	}
}
