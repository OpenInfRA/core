package de.btu.openinfra.backend.db.daos;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.json.simple.JSONObject;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;
import de.btu.openinfra.backend.exception.OpenInfraEntityException;
import de.btu.openinfra.backend.exception.OpenInfraExceptionTypes;

/**
 * This class extends the OpenInfraValueDao class in order to provide another
 * generic method which reads a list of JPA model objects by two given values.
 * Using this class prerequisites a named query of the TypeModel object.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 * @param <TypePojo>
 * @param <TypeModel>
 * @param <TypeModelValue>
 * @param <TypeModelValue2>
 */
public abstract class OpenInfraValueValueDao<
	TypePojo extends OpenInfraPojo,
	TypeModel extends OpenInfraModelObject, TypeModelValue, TypeModelValue2>
	extends OpenInfraValueDao<TypePojo, TypeModel, TypeModelValue> {

	/**
	 * Class of the second given value (TypeModelValue2).
	 */
	protected Class<TypeModelValue2> valueClass2;

	/**
	 * Mandatory constructor method which calls the super constructor.
	 *
	 * @param currentProjectId the current project id
	 * @param schema           the required schema
	 * @param modelClass       a specific class of the JPA model class object
	 * @param valueClass       a specific class of the first JPA model value
	 *                         class object
	 * @param valueClass2      a specific class of the second JPA model value
	 *                         class object
	 */
	protected OpenInfraValueValueDao(
			UUID currentProjectId,
			OpenInfraSchemas schema,
			Class<TypeModel> modelClass,
			Class<TypeModelValue> valueClass,
			Class<TypeModelValue2> valueClass2) {
		super(currentProjectId, schema, modelClass, valueClass);

		this.valueClass2 = valueClass2;
	}

	/**
	 * This is a generic method which reads a list of TypePojos defined by
     * two specific UUID values (e.g. get a list of objects which belong to a
     * combination of two specific objects). This method presumes a
     * corresponding named query of the TypeModel object.
	 *
	 * @param locales  the locale defined by request
	 * @param valueId  the specific id of the first required value object
	 * @param valueId2 the specific id of the second required value object
	 * @param offset   the number where to start
	 * @param size     the size of items to provide
	 * @return         a list of objects of type POJO class which directly
     *                 belong to the TypeModelValue and TypeModelValue2 classes
	 */
	public List<TypePojo> read(
			Locale locales,
			UUID valueId,
			UUID valueId2,
			int offset,
			int size) {
	    // 1. Define a list which holds the POJO objects
        List<TypePojo> pojos = new LinkedList<TypePojo>();
		// 2. Get the specific value objects from JPA layer
		TypeModelValue tmv = em.find(valueClass, valueId);
		TypeModelValue2 tmv2 = em.find(valueClass2, valueId2);

		if(tmv != null && tmv2 != null) {
    		// 3. Construct the name of the named query
    		String namedQuery = modelClass.getSimpleName()
    				+ ".findBy"	+ valueClass.getSimpleName()
    				+ "And" + valueClass2.getSimpleName();
    		// 4. Retrieve the requested model objects from database
    		List<TypeModel> models = em.createNamedQuery(
    				namedQuery,
    				modelClass)
    				.setParameter("value", tmv)
    				.setParameter("value2", tmv2)
    				.setFirstResult(offset)
    				.setMaxResults(size)
    				.getResultList();
    		// 5. Map the JPA model objects to POJO objects
    		for(TypeModel modelItem : models) {
    			pojos.add(mapToPojo(locales, modelItem));
    		} // end for
		}
		return pojos;
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
        if (firstAssociationId != null &&
                !firstAssociationId.equals(firstAssociationIdFromPojo)) {
            throw new OpenInfraEntityException(
            		OpenInfraExceptionTypes.INCOMPATIBLE_UUIDS);
        }

        if (secondAssociationIdFromPojo != null &&
                !secondAssociationIdFromPojo.equals(
                        secondAssociationIdFromPojo)) {
            throw new OpenInfraEntityException(
            		OpenInfraExceptionTypes.INCOMPATIBLE_UUIDS);
        }

        if (metaData != null) {
            // update the meta data as well if it exists
            return createOrUpdate(pojo,
                    secondAssociationId, metaData);
        } else {
            // pass the pojo UUID because this uuid is not part of the URI
            return createOrUpdate(pojo, pojo.getUuid());
        }
    }
}
