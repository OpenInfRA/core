package de.btu.openinfra.backend.db.daos;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Table;

import org.eclipse.persistence.jpa.JpaQuery;
import org.json.simple.JSONObject;

import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.db.EntityManagerFactoryCache;
import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraOrderByEnum;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.jpa.model.MetaData;
import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;
import de.btu.openinfra.backend.db.jpa.model.PtLocale;
import de.btu.openinfra.backend.db.pojos.MetaDataPojo;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;

/**
 * This class is used to provide a sophisticated way to manage the JPA entity
 * manager.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 * @param <TypePojo>  defines the referring POJO class of the DAO class
 * @param <TypeModel> defines the referring JPA model class of the DAO class
 */
public abstract class OpenInfraDao<TypePojo extends OpenInfraPojo,
	TypeModel extends OpenInfraModelObject> {

	/**
	 * The JPA entity manager used to access the project or the system database.
	 */
	protected EntityManager em;

	/**
	 * The UUID of the current project required for creating the entity manager.
	 */
	protected UUID currentProjectId;

	/**
	 * This variable defines the underlying JPA model class.
	 */
	protected Class<TypeModel> modelClass;

	/**
	 * The currently used schema.
	 */
	protected OpenInfraSchemas schema;

	/**
	 * This method represents the super constructor in order to create an entity
	 * manager for each data access object (DAO) in a sophisticated way. We
	 * decided to use an application managed entity manager in order to have
	 * more control about its properties and its life cycle. Each DAO provides
	 * its own entity manager and the lifetime of the entity manager depends on
	 * the lifetime of the DAO object. The entity manager is automatically
	 * closed by the finalize method. Thus, don't call the close method on the
	 * entity manager otherwise you'll get an exception if you try to access the
	 * entity manager again.
	 *
	 * This method sets the properties of the connection dynamically. If you
	 * would like to change the connection, this should be the first class to
	 * look at. The connection strings should be managed by a configuration
	 * (properties) file or by a database.
	 *
	 * The super constructor will use the current project id in order to access
	 * the meta database and to retrieve the credentials of the currently used
	 * project database. If the current project id is set to null and the
	 * corresponding schema is specified, the super constructor will provide
	 * access to the system database.
	 *
	 * In order to change the schema value you have to consider query parameters
	 * of the connection URL like so:
	 * "jdbc:postgresql://localhost:5432/openinfra?currentSchema=project".
	 * By considering this, you'll be able to modify the required schema easily,
	 * nicely and dynamically during runtime.
	 *
	 * TODO in order to add a User Rights Management system add specific
	 * functions here
	 *
	 * @param currentProjectId The identifier of the current project.
	 * @param schema           This parameter defines the schema.
	 * @param modelClass       The underlying JPA model class.
	 */
	protected OpenInfraDao(
			UUID currentProjectId,
			OpenInfraSchemas schema,
			Class<TypeModel> modelClass) {
		// 1. Set local parameters
		this.currentProjectId = currentProjectId;
		this.schema = schema;
		this.modelClass = modelClass;

		// 2. Create the final entity manager
		em = EntityManagerFactoryCache.getEntityManagerFactory(
		        currentProjectId,
		        schema).createEntityManager();
	}

	/**
	 * This is the default generic method which provides read access to the
	 * selected database schema without sorting. It is almost the same routine
	 * for all DAO classes to access the database.
	 *
     * @param locale     A Java.util locale objects.
	 * @param offset     the number where to start
	 * @param size       the size of items to provide
	 * @return           a list of objects of type POJO class
	 */
    public List<TypePojo> read(Locale locale, int offset, int size) {
		// 1. Define a map which holds the POJO objects
		List<TypePojo> pojos = new LinkedList<TypePojo>();
		// 2. Define a list of model objects
		List<TypeModel> models = null;
		// 3. Define the named query
	    String namedQuery = modelClass.getSimpleName() + ".findAll";
	    // 4. Retrieve the requested model objects from database
	    models = em.createNamedQuery(
	                namedQuery,
	                modelClass)
	                .setFirstResult(offset)
	                .setMaxResults(size)
	                .getResultList();
		// 5. Finally, map the JPA model objects to POJO objects
		for(TypeModel modelItem : models) {
		    pojos.add(mapToPojo(locale, modelItem));
		} // end for
		return pojos;
	}

	/**
	 * This is a generic method which provides read access to the selected
	 * database schema. It is almost the same routine for all DAO classes to
	 * access the database. If not, this method should be extended in order
	 * to avoid overrides. Overrides could increase the effort regarding the
	 * integration and maintenance of a rights management system.
	 *
     * Since the system schema doesn't provide the project_id column for topic
     * characteristic objects it is necessary to handle this request separately.
     *
     * The meta data schema is also handled separately.
	 *
	 *
     * @param locale     A Java.util locale objects.
	 * @param order      the sort order (ascending or descending)
	 * @param column     the column to sort
	 * @param offset     the number where to start
	 * @param size       the size of items to provide
	 * @return           a list of objects of type POJO class
	 */
	@SuppressWarnings("unchecked")
    public List<TypePojo> read(
    		Locale locale,
    		OpenInfraSortOrder order,
    		OpenInfraOrderByEnum column,
    		int offset,
    		int size) {
		// 1. Define a list which holds the POJO objects
		List<TypePojo> pojos = new LinkedList<TypePojo>();
		// 2. Define a list of model objects
		List<TypeModel> models = null;
		// 3. Use the default values for language and order when null.
		if(locale == null) {
			locale = OpenInfraProperties.DEFAULT_LANGUAGE;
		}
		if(order == null) {
			order = OpenInfraProperties.DEFAULT_ORDER;
		}
		// 4. Read the required ptlocale object
		PtLocale ptl = new PtLocaleDao(currentProjectId, schema).read(locale);
		// 5. Handle requests that contains an order by column
        if(column == null) {
            // 5.a When the column is null redirect to another method
            return read(locale, offset, size);
        } else {
	        // 5.a Construct the origin SQL-based named query and replace the
			//     the placeholder by the required column and sort order.
	        String sqlString = em.createNamedQuery(
	        		modelClass.getSimpleName() + ".findAllByLocaleAndOrder")
	        		.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
	        sqlString = String.format(sqlString, column.name());
	        sqlString += " " + order.name();
	        // 5.b Retrieve the requested model objects from database
	        models = em.createNativeQuery(
	        		sqlString,
	                modelClass)
	                .setParameter(1, ptl.getId().toString())
	                .setFirstResult(offset)
	                .setMaxResults(size)
	                .getResultList();
		} // end if else

		// 6. Finally, map the JPA model objects to POJO objects
		for(TypeModel modelItem : models) {
		    pojos.add(mapToPojo(locale, modelItem));
		} // end for
		return pojos;
	}

	/**
	 * A special method which returns model objects. This method is primarily
	 * used to create a search index.
	 *
	 * @return a list of all model objects
	 */
	public List<TypeModel> read() {
		String namedQuery = modelClass.getSimpleName() + ".findAll";
		return em.createNamedQuery(
				namedQuery,
				modelClass).getResultList();
	}

	/**
	 * This function creates a new or updates an existing object in the
	 * database. The update function depends on the specified language.
	 *
	 * @param pojo    the POJO object which should be stored in the database
	 * @return        the UUID of the newly created or replaced object
	 * @throws RuntimeException
	 */
	public UUID createOrUpdate(TypePojo pojo)
			throws RuntimeException {

	    TypeModel model = createModelObject(pojo.getUuid());
        // abort if the pojo and the type model is null
        if (pojo == null || model == null) {
            return null;
        }

        // 1. Map the POJO object to a JPA model object
        MappingResult<TypeModel> result = mapToModel(pojo, model);

        // abort here if the result is null
        if (result == null) {
            return null;
        }

		// 2. Get the transaction and merge (create or replace) the JPA model
		// object.
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
	        em.merge(result.getModelObject());
			et.commit();
			return result.getId();
		} catch(RuntimeException ex) {
			if(et != null && et.isActive()) {
				et.rollback();
			} // end if
			throw ex;
		} // end try catch
    }

	/**
	 * This method will first execute a createOrUpdate for the TypePojo and
	 * second execute a createOrUpdate for the meta data. This is necessary for
	 * project and system databases where tables can store meta data inside the
	 * table meta_data.
	 *
	 * @param pojo     the POJO object which should be stored in the database
	 * @param metaData the JSON data that should be stored in the meta data
	 *                 associated to the POJO
	 * @return
	 */
	public UUID createOrUpdate(TypePojo pojo, JSONObject metaData) {

	    UUID retId = null;

	    if (pojo != null && metaData != null) {
	        // first createOrUpdate the TypePojo
	        retId = createOrUpdate(pojo);

	        // only check for meta data in project and system schema
	        switch (schema) {
	            case PROJECTS:
	            case SYSTEM:
	                // if the TypePojo was created successfully
	                if (retId != null) {
	                    // create a raw POJO for meta data
	                    MetaDataPojo mdPojo = new MetaDataPojo();

	                    // set the object id from the previously created
	                    // TypePojo
	                    mdPojo.setObjectId(retId);
	                    // TODO find a way to avoid hard coded id
	                    // define the primary key column
	                    mdPojo.setPkColumn("id");
	                    // define the table name for the object
	                    mdPojo.setTableName(modelClass
	                            .getAnnotation(Table.class).name());
	                    // set the meta data
	                    mdPojo.setData(metaData);
	                    // write the meta data
	                    UUID metaId = new MetaDataDao(currentProjectId, schema)
	                                        .createOrUpdate(mdPojo);
	                    if (metaId == null) {
	                        // TODO give feedback about meta data creation?
	                    }
	                }
	                break;
	            default:
	                break;
	        }
	    }
	    return retId;
	}

    /**
     * This method checks if the value that is part of the URI matches with
     * the object id in the POJO. If it matches it will call the createOrUpdate
     * method of the OpenInfraDao. Otherwise it will return null. If meta data
     * is passed it will be created or updated as well.
     *
     * @param pojo     the POJO object which should be stored in the database
     * @param valueId  the value id from the URI
     * @param metaData the JSON data that should be stored in the meta data
     *                 associated to the POJO
     * @return         the UUID of the newly created or replaced object or null
     * @throws RuntimeException
     */
    public UUID createOrUpdate(TypePojo pojo, UUID valueId, JSONObject metaData)
            throws RuntimeException {

        // check if the value id of the URI map to the POJO uuid
        try {
            if (!pojo.getUuid().equals(valueId)) {
                return null;
            }
        } catch (NullPointerException e) { /* do nothing */ }
        if (metaData != null) {
            // update the meta data as well if it exists
            return createOrUpdate(pojo, metaData);
        } else {
            return createOrUpdate(pojo);
        }
    }

    /**
     * This method checks if the first associated id that is part of the URI
     * matches with the association id in the POJO. If it matches it will call
     * the createOrUpdate method of the OpenInfraDao. Otherwise it will return
     * null.
     *
     * @param pojo                        the POJO object which should be stored
     *                                    in the database
     * @param firstAssociationId          the first association id from the URI
     * @param firstAssociationIdFromPojo  the first association id from the POJO
     * @param metaData                    the JSON data that should be stored in
     *                                    the meta data associated to the POJO
     * @return                            the UUID of the newly created or
     *                                    replaced object or null
     * @throws RuntimeException
     */
    public UUID createOrUpdate(TypePojo pojo, UUID firstAssociationId,
            UUID firstAssociationIdFromPojo, JSONObject metaData)
            throws RuntimeException {

        // check if the value id of the URI map to the pojo uuid
        try {
            if (!firstAssociationId.equals(firstAssociationIdFromPojo)) {
                return null;
            }
        } catch (NullPointerException e) { /* do nothing */ }

        if (metaData != null) {
            // update the meta data as well if it exists
            return createOrUpdate(pojo, metaData);
        } else {
            return createOrUpdate(pojo);
        }
    }

    /**
     * This method checks if the first associated id that is part of the URI
     * matches with the association id in the POJO and the second associated id
     * that is part of the URI matches with the id of the associated object in
     * the POJO. If the matches are fulfilled the createOrUpdate method of the
     * OpenInfraDao will be called. Otherwise it will return null.
     *
     * @param pojo                        the POJO object which should be stored
     *                                    in the database
     * @param firstAssociationId          the first association id from the URI
     * @param firstAssociationIdFromPojo  the first association id from the POJO
     * @param secondAssociationId         the second association id from the URI
     * @param secondAssociationIdFromPojo the second association id from the
     *                                    POJO
     * @param metaData                    the JSON data that should be stored in
     *                                    the meta data associated to the POJO
     * @return                            the UUID of the newly created or
     *                                    replaced object or null
     * @throws RuntimeException
     */
    public UUID createOrUpdate(TypePojo pojo, UUID firstAssociationId,
            UUID firstAssociationIdFromPojo, UUID secondAssociationId,
            UUID secondAssociationIdFromPojo, JSONObject metaData)
            throws RuntimeException {

        // check if the value id of the URI map to the pojo uuid
        try {
            if (!firstAssociationId.equals(firstAssociationIdFromPojo)) {
                return null;
            }
        } catch (NullPointerException e) { /* do nothing */ }

        try {
            if (!secondAssociationId.equals(secondAssociationIdFromPojo)) {
                return null;
            }
        } catch (NullPointerException e) { /* do nothing */ }

        if (metaData != null) {
            // update the meta data as well if it exists
            return createOrUpdate(pojo, metaData);
        } else {
            return createOrUpdate(pojo);
        }
    }

	/**
	 * This is a generic method which reads a specific pojo object. We decided
	 * to hide this functionality behind a protected class so that each deriving
	 * class must implement its own read method. This should provide more type
	 * safety.
	 *
     * @param locale             A Java.util locale object.
	 * @param id                 the UUID of the required model class
	 * @return                   The requested POJO object if present otherwise
	 *                           null.
	 */
    public TypePojo read(Locale locale, UUID id) {
		TypePojo pojo = null;
		try {
            TypeModel tm = em.find(modelClass, id);
            if(tm != null) {
                pojo = mapToPojo(locale, tm);
            }
		} catch(Exception ex) {
            ex.printStackTrace();
        } // end try catch
		return pojo;
	}

	/**
	 * This is an abstract method which must be implemented by deriving classes.
	 * It should map a JPA model object into corresponding a POJO object.
	 *
	 * This method is really slow. Thus, don't use this method too often. It's
	 * recommended to provide another static method which should be much faster.
	 *
	 * @param modelObject the JPA model object
	 * @return            a corresponding POJO object
	 */
	public abstract TypePojo mapToPojo(Locale locale, TypeModel modelObject);

	/**
	 * This is an abstract method which must be implemented by deriving classes.
	 * It should map a POJO object into a corresponding JPA model object.
	 *
	 * @param modelObject the pre initialized model object
	 * @param pojoObject  the POJO object
	 * @return            a corresponding JPA model object or null if the pojo
	 *                    object is null
	 */
	public abstract MappingResult<TypeModel> mapToModel(
			TypePojo pojoObject, TypeModel modelObject);

	/**
	 * This method deletes an entity from the database. Previously it checks if
	 * meta data exists for this entity and delete it as well.
	 *
	 * @param uuid the uuid which specifies the entity
	 * @return     true when the entity was deleted false when the uuid doesn't
	 *             exists
	 */
	public boolean delete(UUID uuid) {
	    // only check for meta data in project and system schema
	    switch (schema) {
            case PROJECTS:
            case SYSTEM:
        	    try {
        	        // retrieve meta data for the entity if exists
                    MetaData md = em.createNamedQuery(
                            "MetaData.findByObjectId", MetaData.class)
                            .setParameter("oId", uuid).getSingleResult();
                    // delete the meta data
                    if (new MetaDataDao(currentProjectId, schema).delete(
                            md.getId())) {
                        // TODO give feedback about meta data deletion?
                    }
                } catch (NoResultException nre) { /* do nothing */ }
        	    break;
            default:
                break;
        }

	    // delete the common entity
		TypeModel o = em.find(modelClass, uuid);
		EntityTransaction et = em.getTransaction();
		if(o != null) {
			try {
				et.begin();
				em.remove(o);
				et.commit();
				return true;
			} catch(RuntimeException ex) {
				if(et != null && et.isActive()) {
					et.rollback();
				} // end if
				throw ex;
			} // end try catch
		} else {
			return false;
		} // end if else
	}

	/**
	 * This method delivers a model object. When the UUID is not null this
	 * method tries to deliver an existing object. When the UUID is null it
	 * creates a new object and generates a random UUID.
	 *
	 * @param id the UUID of the requested object or null
	 * @return   a model object (new or old) or null if the pojo id doesn't
	 *           exists
	 */
	protected TypeModel createModelObject(UUID id) {
		TypeModel tm = null;
		if(id != null) {
			tm = em.find(modelClass, id);
			// TODO check if tm null
		} else {
			try {
				tm = modelClass.newInstance();
				tm.setId(UUID.randomUUID());
			} catch(Exception ex) {
				ex.printStackTrace();
			} // end try catch
		} // end if else
		return tm;
	}

	/**
	 * This method retrieves the count of objects and uses a named query. If the
	 * query doesn't exists, it will throw an exception.
	 *
	 * @return count of objects or zero
	 */
	public long getCount() {
		String namedQuery = modelClass.getSimpleName() + ".count";
		Long count = 0L;
		count = em.createNamedQuery(
            namedQuery,
            Long.class).getSingleResult().longValue();
		return count;
	}

	/**
	 * This method closes the entity manager automatically. Don't close the
	 * entity manager manually. This will cause an exception when you try to
	 * access the entity manager again during life time of the current object
	 * instance.
	 */
	@Override
	protected void finalize() throws Throwable {
		if(em != null) {
			em.close();
		} // end if
		super.finalize();
	}

}
