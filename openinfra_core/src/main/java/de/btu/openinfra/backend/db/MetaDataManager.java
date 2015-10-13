package de.btu.openinfra.backend.db;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import de.btu.openinfra.backend.db.daos.meta.ProjectsDao;
import de.btu.openinfra.backend.db.jpa.model.meta.Projects;
import de.btu.openinfra.backend.db.pojos.meta.ProjectsPojo;

/**
 * This class implements access to the meta database using a singleton
 * implementation.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class MetaDataManager {

	/**
	 * The entity manager used for accessing the meta database.
	 */
	private static EntityManager emMeta;

	/**
	 * This method retrieves the connection string and credentials required for
	 * accessing the current project. The current project id is set by the
	 * constructor method.
	 *
	 * @return The connection string and credentials required to access the
	 *         current project.
	 */
	public static ProjectsPojo getProjects(UUID projectId) {
		if(emMeta == null) {
			emMeta = EntityManagerFactoryCache.getEntityManagerFactory(
			        projectId,
			        OpenInfraSchemas.META_DATA).createEntityManager();
		}

		// retrieve the project from the meta data
		try {
    		Projects p = emMeta.createNamedQuery(
                  "Projects.findByProject",
                  Projects.class)
                  .setParameter("value", projectId)
                  .getSingleResult();
    		return ProjectsDao.mapToPojoStatically(p);
		} catch (NoResultException e) {
            return null;
        }
	}

}
