package de.btu.openinfra.backend.db.daos;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.eclipse.persistence.jpa.JpaQuery;
import org.json.simple.JSONObject;

import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.db.OpenInfraOrderBy;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;
import de.btu.openinfra.backend.db.jpa.model.PtLocale;
import de.btu.openinfra.backend.db.jpa.model.TopicInstance;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;
import de.btu.openinfra.backend.exception.OpenInfraEntityException;
import de.btu.openinfra.backend.exception.OpenInfraExceptionTypes;

/**
 * This class extends the OpenInfraDao class in order to provide another
 * generic method which reads a list of JPA model objects by a given value.
 * Using this class prerequisites a named query of the TypeModel object.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 * @param <TypePojo>
 * @param <TypeModel>
 * @param <TypeModelValue>
 */
public abstract class OpenInfraValueDao<
	TypePojo extends OpenInfraPojo,
	TypeModel extends OpenInfraModelObject, TypeModelValue> extends
	OpenInfraDao<TypePojo, TypeModel> {

	protected Class<TypeModelValue> valueClass;

	/**
	 * Mandatory constructor method which calls the super constructor.
	 *
	 * @param currentProjectId the current project id
	 * @param schema           the required schema
	 * @param modelClass       a specific class of the JPA model class object
	 * @param valueClass       a specific class of the JPA model value class
	 *                         object
	 */
	protected OpenInfraValueDao(
			UUID currentProjectId,
			OpenInfraSchemas schema,
			Class<TypeModel> modelClass,
			Class<TypeModelValue> valueClass) {
		super(currentProjectId, schema, modelClass);
		this.valueClass = valueClass;
	}

	/**
	 * This is a generic method which reads a list of TypePojos defined by
	 * a specific UUID value (e.g. get a list of objects which belong to a
	 * specific object). This method presumes a corresponding named query of
	 * the TypeModel object.
	 *
	 * @param locale     the locale defined by request
	 * @param valueId    the specific id of the required value object
	 * @param offset     the number where to start
	 * @param size       the size of items to provide
	 * @return           a list of objects of type POJO class which directly
	 *                   belong to the TypeModelValue class
	 */
	public List<TypePojo> read(
			Locale locale,
			UUID valueId,
			int offset,
			int size) {
	    // 1. Define a list which holds the POJO objects
        List<TypePojo> pojos = new LinkedList<TypePojo>();
		// 2. Get the specific value object from JPA layer
		TypeModelValue tmv = em.find(valueClass, valueId);
		if(tmv != null) {
		    // 3. Construct the name of the named query
	        String namedQuery = modelClass.getSimpleName()
	                + ".findBy"
	                + valueClass.getSimpleName();
	        // 4. Retrieve the requested model objects from database
	        List<TypeModel> models = em.createNamedQuery(
	                namedQuery,
	                modelClass)
	                .setParameter("value", tmv)
	                .setFirstResult(offset)
	                .setMaxResults(size)
	                .getResultList();
	        // 5. Map the JPA model objects to POJO objects
	        for(TypeModel modelItem : models) {
	            pojos.add(mapToPojo(locale, modelItem));
	        } // end for
		}

		return pojos;
	}

	/**
     * This method checks if the first associated id that is part of the URI
     * matches with the association id in the POJO. If the matches are fulfilled
     * the createOrUpdate method of the OpenInfraDao will be called. Otherwise
     * it will return null.
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
        if (firstAssociationId != null &&
                !firstAssociationId.equals(firstAssociationIdFromPojo)) {
            throw new OpenInfraEntityException(
            		OpenInfraExceptionTypes.INCOMPATIBLE_UUIDS);
        }

        if (metaData != null) {
            // update the meta data as well if it exists
            return createOrUpdate(pojo,
                    firstAssociationId, metaData);
        } else {
            // pass the pojo UUID because this uuid is not part of the URI
            return createOrUpdate(pojo, pojo.getUuid());
        }
    }

	/**
     * This is a generic method which reads a list of TypePojos defined by
     * a specific UUID value (e.g. get a list of objects which belong to a
     * specific object). This method presumes a corresponding named query of
     * the TypeModel object.
     *
     * It provides the possibility to sort the list.
     *
     * @param locale     the locale defined by request
     * @param valueId    the specific id of the required value object
     * @param order      the sort direction
     * @param column     the column that should be used for sorting
     * @param offset     the number where to start
     * @param size       the size of items to provide
     * @return           a list of objects of type POJO class which directly
     *                   belong to the TypeModelValue class
     */
    @SuppressWarnings("unchecked")
    public List<TypePojo> read(
            Locale locale,
            UUID valueId,
            OpenInfraSortOrder order,
            OpenInfraOrderBy column,
            int offset,
            int size) {
        // Define a list which holds the POJO objects
        List<TypePojo> pojos = new LinkedList<TypePojo>();

        // Get the specific value object from JPA layer
        // TODO will be necessary for the not implemented else branch
        // TODO Check: if tmv == null then return pojos
        //TypeModelValue tmv = em.find(valueClass, valueId);

        // Define a model object that contains the query result
        List<TypeModel> models = null;

        // Flag to determine if the query has to run again with a different
        // locale
        boolean runAgain = false;

        // Use the default values for language and order when null.
        if(locale == null) {
            locale = OpenInfraProperties.DEFAULT_LANGUAGE;
        }
        if(order == null) {
            order = OpenInfraProperties.DEFAULT_ORDER;
        }

        // get the locale id
        UUID localeId = new PtLocaleDao(
                currentProjectId, schema).read(locale).getId();

        // Handle topic instances separately
        if (column == null || !column.isUuid()) {
            // If the column is null redirect to another read method
            return read(locale, valueId, offset, size);
        } else if (modelClass == TopicInstance.class) {
            String nativeQueryName = "";
            // Get the attribute value types from the object with the passed
            // attribute type id
            AttributeValueTypes atType = new AttributeTypeDao(
                    currentProjectId, schema).read(
                            locale, UUID.fromString(
                                    column.getContent().toString()))
                                    .getType();

            // Handle each attribute value type in a different way
            switch (atType) {
            case ATTRIBUTE_VALUE_VALUE:
                // Set the native query name for attribute value value objects
                nativeQueryName = "findAllByLocaleAndOrderForValues";
                break;
            case ATTRIBUTE_VALUE_DOMAIN:
                // Set the native query name for attribute value domain objects
                nativeQueryName = "findAllByLocaleAndOrderForDomains";
                break;
            default:
                // Sorting by geometry is not supported
                return read(locale, valueId, offset, size);
            }

            // Construct the origin SQL-based named query and append the sort
            // order.
            String sqlString = em.createNamedQuery(
                    modelClass.getSimpleName() + "." + nativeQueryName)
                    .unwrap(JpaQuery.class).getDatabaseQuery().getSQLString();
            sqlString += " " + order.name();

            // Retrieve the informations from the database
            models = em.createNativeQuery(
                            sqlString,
                            modelClass)
                        .setParameter(1, column.getContent())
                        .setParameter(2, localeId)
                        .setParameter(3, valueId)
                        .setFirstResult(offset)
                        .setMaxResults(size)
                        .getResultList();

            // Get the first result as topic instance model
            TopicInstance tim = (TopicInstance)models.get(0);

            // TODO The condition checks are not tested for values with a
            //      language != xx, a real 0 value and a topic characteristic
            //      that only contains 1 entry!
            // Test if the request returns no sufficient result
            switch (atType) {
            case ATTRIBUTE_VALUE_VALUE:
                // Check if only one result was returned and if this is equals
                // 0. JPA returns the string 0 if the free text is NULL.
                if (tim.getAttributeValueValues().get(0)
                        .getPtFreeText()
                        .getLocalizedCharacterStrings().get(0)
                        .getFreeText().equals("0") &&
                    tim.getAttributeValueValues().get(0)
                        .getPtFreeText()
                        .getLocalizedCharacterStrings().get(0)
                        .getFreeText().length() == 1) {
                    // Set flag to run the query again with the xx locale
                    runAgain = true;
                }
                break;
            case ATTRIBUTE_VALUE_DOMAIN:
                // Check if only one result was returned and if this is equals
                // 0. JPA returns the string 0 if the free text is NULL.
                if (tim.getAttributeValueDomains().get(0)
                        .getValueListValue().getPtFreeText2()
                        .getLocalizedCharacterStrings().get(0)
                        .getFreeText().equals("0") &&
                    tim.getAttributeValueDomains().get(0)
                        .getValueListValue().getPtFreeText2()
                        .getLocalizedCharacterStrings().get(0)
                        .getFreeText().length() == 1) {
                    // Set flag to run the query again with the xx locale
                    runAgain = true;
                }
                break;
            default:
                // This part is unreachable because of the previously executed
                // switch case statement
                break;
            }

            // Run the query again with the xx locale instead of the passed
            // locale
            if (runAgain) {
                // Retrieve the uuid of the xx locale
                localeId = em.createNamedQuery(
                                "PtLocale.xx",
                                PtLocale.class)
                                .getSingleResult().getId();

                // Retrieve the informations from the database with the new
                // locale
                models = em.createNativeQuery(
                        sqlString,
                        modelClass)
                    .setParameter(1, column.getContent())
                    .setParameter(2, localeId)
                    .setParameter(3, valueId)
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();
            }
        } else {
            // TODO implement this for all the other classes
        }

        // Map the JPA model objects to POJO objects
        for(TypeModel modelItem : models) {
            pojos.add(mapToPojo(locale, modelItem));
        } // end for
        return pojos;
    }

	/**
	 * This method returns the count of objects referring a specific object.
	 *
	 * @param valueId the specific object
	 * @return        the count of objects or -1 if the value id doesn't exists
	 */
	public Long getCount(UUID valueId) {
		// 1. Get the specific value object from JPA layer
		TypeModelValue tmv = em.find(valueClass, valueId);
		if(tmv != null) {
		    // 2. Construct the name of the named query
	        String namedQuery = modelClass.getSimpleName()
	                + ".countBy"
	                + valueClass.getSimpleName();
	        return em.createNamedQuery(
	                namedQuery,
	                Long.class)
	                .setParameter("value", tmv)
	                .getSingleResult()
	                .longValue();
		}
		return -1L;
	}

}
