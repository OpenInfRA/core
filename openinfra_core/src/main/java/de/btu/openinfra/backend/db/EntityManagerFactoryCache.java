package de.btu.openinfra.backend.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.btu.openinfra.backend.OpenInfraApplication;
import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.OpenInfraPropertyKeys;
import de.btu.openinfra.backend.db.daos.ProjectDao;
import de.btu.openinfra.backend.db.pojos.ProjectPojo;
import de.btu.openinfra.backend.db.pojos.meta.ProjectsPojo;

/**
 * This class is a container to cache objects from the class
 * EntityManagerFactory. The class uses the singleton implementation.
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class EntityManagerFactoryCache {

    // TODO add to OpenInfraProperties (PropertyFile)
    /**
     * Maximum number of entries the cache may contain.
     */
    private static int cacheSize = 100;
    
    // TODO Proposal: Create two static variables for system and meta data
    // entity manager factory. Does not need a cache look up. Only a get
    // method is required.
    /**
     * The cache used for creating and caching entity manager factories
     * (EntityManagerFactory). The cache is thread-safe.
     */
    private static final LoadingCache<CacheTuple, EntityManagerFactory> cache;
    
    /**
     * Creates the initial cache and adds some specific entity manager
     * factories.
     */
    static {
        // Create initial cache
        cache = CacheBuilder.newBuilder().maximumSize(cacheSize).build(
                new CacheLoader<CacheTuple, EntityManagerFactory>() {
                    @Override
                    public EntityManagerFactory load(CacheTuple tuple)
                            throws PersistenceException {
                        return createNewEntityManagerFactory(tuple);
                    }
                });
        
        // Add specific entity manager factories.
        // Add system entity manager factory
        if(cacheSize - cache.size() > 0) {
            try {
                cache.get(new CacheTuple(
                        OpenInfraApplication.PERSISTENCE_CONTEXT,
                        createProperties(null, OpenInfraSchemas.SYSTEM)));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Add meta data entity manager factory
        if(cacheSize - cache.size() > 0) {
            try {
                cache.get(new CacheTuple(
                        OpenInfraApplication.PERSISTENCE_CONTEXT,
                        createProperties(null, OpenInfraSchemas.META_DATA)));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        // Add RBAC entity manager factory
        if(cacheSize - cache.size() > 0) {
        	try {
        		cache.get(new CacheTuple(
        				OpenInfraApplication.PERSISTENCE_CONTEXT, 
        				createProperties(null, OpenInfraSchemas.RBAC)));
        	} catch(ExecutionException ee) {
        		ee.printStackTrace();
        	}
        }
        // Add project entity manager factories
        if(cacheSize - cache.size() > 0) {
            ProjectDao projectDao = new ProjectDao(null,
                    OpenInfraSchemas.META_DATA);
            List<ProjectPojo> projects = projectDao.readMainProjects(null);
          
            for(ProjectPojo projectPojo : projects) {
                try {
                    cache.get(new CacheTuple(
                            OpenInfraApplication.PERSISTENCE_CONTEXT,
                            createProperties(
                                    projectPojo.getUuid(),
                                    OpenInfraSchemas.PROJECTS)));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(cacheSize - cache.size() == 0) {
                    break;
                }
            }
        }
    }
    
    /**
     * Returns an entity factory manager for the given parameters.
     * @param currentProjectId identifier of the current project
     * @param schema this parameter defines the schema
     * @return entity factory manager if an entry exists in the cache or it
     * is possible to add an entry in the cache for the given parameters,
     * otherwise null 
     */
    public static EntityManagerFactory getEntityManagerFactory(
            UUID currentProjectId,
            OpenInfraSchemas schema) {
        // Create properties
        Map<String, String> properties =
                createProperties(currentProjectId, schema);
        
        try {
            // return entity factory manager
            return cache.get(new CacheTuple(
                    OpenInfraApplication.PERSISTENCE_CONTEXT,
                    properties));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // return null
        return null;
    }
    
    /**
     * Creates a hash map containing all necessary information for creating
     * a entity factory manager.
     * @param currentProjectId The identifier of the current project.
     * @param schema           This parameter defines the schema.
     * @return properties
     */
    private static Map<String, String> createProperties(
            UUID currentProjectId,
            OpenInfraSchemas schema) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(
                OpenInfraPropertyKeys.JDBC_DRIVER.toString(),
                OpenInfraPropertyValues.JDBC_DRIVER.toString());
        // 3. Set default properties
        String user = OpenInfraProperties.getProperty(
                OpenInfraPropertyKeys.USER.toString());
        String password = OpenInfraProperties.getProperty(
                OpenInfraPropertyKeys.PASSWORD.toString());
        String url = String.format(
                OpenInfraPropertyValues.URL.toString(),
                OpenInfraProperties.getProperty(
                        OpenInfraPropertyKeys.SERVER.toString()),
                OpenInfraProperties.getProperty(
                        OpenInfraPropertyKeys.PORT.toString()),
                OpenInfraProperties.getProperty(
                        OpenInfraPropertyKeys.DB_NAME.toString()));
        // 3. Decide if the system or a project database schema is requested
        String currentSchema = "currentSchema=";
        switch (schema) {
        case META_DATA:
            currentSchema +=
                OpenInfraPropertyValues.META_DATA_SEARCH_PATH + "," +
                OpenInfraPropertyValues.SEARCH_PATH;
            break;
        case PROJECTS:
            // Override default properties and set project and default search
            // path
            ProjectsPojo p = MetaDataManager.getProjects(currentProjectId);
            // The current project id might wrong which means that the id is not 
            // associated to an existing project
            if(p == null) {
            	break;
            }
            user = p.getDatabaseConnection().getCredentials().getUsername();
            password = p.getDatabaseConnection().getCredentials().getPassword();
            url = String.format(
                    OpenInfraPropertyValues.URL.toString(),
                    p.getDatabaseConnection().getServer().getServer(),
                    p.getDatabaseConnection().getPort().getPort(),
                    p.getDatabaseConnection().getDatabase().getDatabase());
            currentSchema +=
                    p.getDatabaseConnection().getSchema().getSchema() + "," +
                    OpenInfraPropertyValues.SEARCH_PATH;
            break;
        case RBAC:
        	currentSchema += OpenInfraPropertyValues.RBAC_SEARCH_PATH + "," +
        			OpenInfraPropertyValues.SEARCH_PATH;
        	break;
        case SYSTEM:
            // fall through
        default:
            // set default search path
            currentSchema +=
                OpenInfraPropertyValues.SYSTEM_SEARCH_PATH + "," +
                OpenInfraPropertyValues.SEARCH_PATH;
            break;
        }
        properties.put(OpenInfraPropertyKeys.USER.toString(), user);
        properties.put(OpenInfraPropertyKeys.PASSWORD.toString(), password);
        properties.put(
                OpenInfraPropertyKeys.URL.toString(),
                url + currentSchema);
        
        return properties;
    }

    /**
     * Returns the maximum number of cache entries.
     * @return maximum number of cache entries
     */
    public static int getCacheSize() {
        return cacheSize;
    }

    /**
     * Sets the maximum number of cache entries
     * @param cacheSize new maximum number of cache entries
     */
    public static void setCacheSize(int cacheSize) {
        EntityManagerFactoryCache.cacheSize = cacheSize;
    }
    
    /**
     * Creates a new entity manager factory using the Persistence class.
     * This method is called, if a new entry will be inserted in the
     * cache (LoadingCache).
     * @param cacheTuple cache tuple containing all information for creating
     * a new entity manager factory
     * @return a new entity manager factory
     */
    private static EntityManagerFactory createNewEntityManagerFactory(
            final CacheTuple cacheTuple) {
        return Persistence.createEntityManagerFactory(
                cacheTuple.getPersistenceUnitName(),
                cacheTuple.getProperties());
    }
    
    /**
     * This class represents a key used to cache entity manager factories.
     * @author <a href="http://www.b-tu.de">BTU</a> DBIS
     *
     */
    private static class CacheTuple { 
        
        /**
         * Name of the persistence unit.
         */
        private String persistenceUnitName; 
        
        /**
         * Properties to use when creating the factory.
         */
        private Map<String, String> properties;
        
        /**
         * Creates a cache tuple using the given parameters.
         * @param persistenceUnitName name of the persistence unit
         * @param properties properties to use when creating the factory
         */
        public CacheTuple(
                String persistenceUnitName,
                Map<String, String> properties) { 
            this.persistenceUnitName = persistenceUnitName; 
            this.properties = properties; 
        }   
        
        /**
         * Returns the name of the persistence unit.
         * @return the name of the persistence unit
         */
        public String getPersistenceUnitName() {
            return persistenceUnitName;
        }

        /**
         * Returns the properties used for creating a factory.
         * @return properties used for creating a factory
         */
        public Map<String, String> getProperties() {
            return properties;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof CacheTuple) {
                CacheTuple tuple = (CacheTuple) obj;
                return (persistenceUnitName.equals(tuple.persistenceUnitName)
                    && properties.equals(tuple.properties));
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (persistenceUnitName.hashCode() + properties.hashCode());
        }
    }
}
