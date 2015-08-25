package de.btu.openinfra.backend.db.daos;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraMetaDataEnum;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.AttributeValueDomain;
import de.btu.openinfra.backend.db.jpa.model.AttributeValueValue;
import de.btu.openinfra.backend.db.jpa.model.PtLocale;
import de.btu.openinfra.backend.db.jpa.model.TopicCharacteristic;
import de.btu.openinfra.backend.db.jpa.model.TopicInstance;
import de.btu.openinfra.backend.db.pojos.AttributeValuePojo;
import de.btu.openinfra.backend.db.pojos.TopicInstancePojo;

/**
 * This class represents the TopicInstance and is used to access the underlying
 * layer generated by JPA.
 *
 * This class is used as a view object and contains selected attribute types
 * which should be displayed in an OpenInfRA list view. This object must not
 * be used to insert data to the database. It is a view object only!
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class TopicInstanceDao extends OpenInfraValueDao<TopicInstancePojo,
	TopicInstance, TopicCharacteristic> {

	/**
	 * This is the required constructor which calls the super constructor and
	 * in turn creates the corresponding entity manager.
	 *
	 * @param currentProjectId the current project id (this should be null when
	 *                         the system schema is selected)
	 * @param schema           the required schema
	 */
	public TopicInstanceDao(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema,
				TopicInstance.class, TopicCharacteristic.class);
	}

	// TODO add named query in order to retrieve attribute values
	@Override
	public TopicInstancePojo mapToPojo(Locale locale, TopicInstance ti) {
        return mapToPojoStatically(locale, ti,
                new MetaDataDao(currentProjectId, schema));
	}

	/**
     * This method implements the method mapToPojo in a static way.
     *
     * @param locale the requested language as Java.util locale
     * @param ti     the model object
     * @param mdDao  the meta data DAO
     * @return       the POJO object when the model object is not null else null
     */
    public static TopicInstancePojo mapToPojoStatically(
            Locale locale,
            TopicInstance ti,
            MetaDataDao mdDao) {
        if (ti != null) {
            // 1. Create new POJO object and set necessary stuff
            TopicInstancePojo pojo = new TopicInstancePojo(ti, mdDao);

            // set the topic characteristic POJO
            pojo.setTopicCharacteristic(TopicCharacteristicDao
                    .mapToPojoStatically(
                            locale, ti.getTopicCharacteristic(), null));

            String metaData = null;
            // check if meta data exists for this topic instance
            if (pojo.getTopicCharacteristic().getMetaData() != null) {
                if (pojo.getTopicCharacteristic().getMetaData()
                        .containsKey(OpenInfraMetaDataEnum.LIST_VIEW_COLUMNS)) {
                    metaData = pojo.getTopicCharacteristic().getMetaData()
                                   .get(OpenInfraMetaDataEnum.LIST_VIEW_COLUMNS)
                                   .toString();
                }
            }

            // 2. Associate only attribute values to the topic instance which are
            //    mentioned in the meta data.
            List<AttributeValuePojo> values =
                    new LinkedList<AttributeValuePojo>();

            // 3. This is for all attribute value domains
            for(AttributeValueDomain avd : ti.getAttributeValueDomains()) {
                // 3.a Check if the current id is mentioned in the meta data
                if( metaData != null && metaData.contains(
                        avd.getAttributeTypeToAttributeTypeGroup()
                            .getAttributeType().getId().toString()) ||
                        metaData == null ) {
                    AttributeValuePojo avPojo = new AttributeValuePojo();
                    avPojo.setUuid(avd.getId());
                    avPojo.setAttributeTypeId(
                            avd.getAttributeTypeToAttributeTypeGroup()
                            .getAttributeType().getId());
                    avPojo.setAttributeValueType(
                            AttributeValueTypes.ATTRIBUTE_VALUE_DOMAIN);
                    avPojo.setAttributeValueDomain(
                            AttributeValueDomainDao.mapToPojoStatically(
                                    locale,
                                    avd,
                                    mdDao));
                    values.add(avPojo);
                } // end if
            } // end for

            // 4. This is for all attribute value values
            for(AttributeValueValue avv : ti.getAttributeValueValues()) {
                // 4.a Check if the current id is mentioned in the settings
                if( metaData != null && metaData.contains(
                        avv.getAttributeTypeToAttributeTypeGroup()
                            .getAttributeType().getId().toString()) ||
                        metaData == null ) {
                    AttributeValuePojo avPojo = new AttributeValuePojo();
                    avPojo.setUuid(avv.getId());
                    avPojo.setAttributeTypeId(
                            avv.getAttributeTypeToAttributeTypeGroup()
                            .getAttributeType().getId());
                    avPojo.setAttributeValueType(
                            AttributeValueTypes.ATTRIBUTE_VALUE_VALUE);
                    avPojo.setAttributeValueValue(
                            AttributeValueValueDao.mapToPojoStatically(
                                    locale,
                                    avv,
                                    mdDao));
                    values.add(avPojo);
                } // end if
            } // end for

            pojo.setValues(values);
            return pojo;
        } else {
            return null;
        }
    }

	/**
	 * This method retrieves a list of TopicInstance objects belonging to a
	 * specified topic characteristic and where the attribute value is like
	 * the specified filter string.
	 *
	 * @param locale                 the required locale
	 * @param topicCharacteristicId  the id of the topic characteristic
	 * @param filter                 the filter string
	 * @param offset                 the number where to start
	 * @param size                   the max size of the result list
	 * @return                       a list of TopicInstances
	 */
	public List<TopicInstancePojo> read(
			Locale locale,
			UUID topicCharacteristicId,
			String filter,
			int offset,
			int size) {
		// 1. Read the required locale form database
		PtLocale pl = new PtLocaleDao(currentProjectId, schema).read(locale);
		// 2. Create a list of POJO objects
		List<TopicInstancePojo> tip = new LinkedList<TopicInstancePojo>();
		// 3. Get all related objects from database
		List<TopicInstance> tis = em.createNamedQuery(
				"TopicInstance.filterLikeAttributeValueValue",
				TopicInstance.class)
				.setParameter("tc",	em.find(
						TopicCharacteristic.class,
						topicCharacteristicId))
				.setParameter("ptl", pl)
				.setParameter("filter", filter)
				.setFirstResult(offset)
				.setMaxResults(size)
				.getResultList();
		for(TopicInstance ti : tis) {
			tip.add(this.mapToPojo(locale, ti));
		}
		return tip;
	}

	/**
     * This method retrieves a list of TopicInstance objects belonging to a
     * specified topic characteristic and where the attribute value has a 3D
     * geometry.
     *
     * @param locale                 the required locale
     * @param topicCharacteristicId  the id of the topic characteristic
     * @param offset                 the number where to start
     * @param size                   the size of items to provide
     * @return                       a list of TopicInstances
     */
	public List<TopicInstancePojo> readWithGeomz(
            Locale locale,
            UUID topicCharacteristicId,
            int offset,
            int size) {
        // Create a list of POJO objects
        List<TopicInstancePojo> tip = new LinkedList<TopicInstancePojo>();
        // Get all related objects from database
        List<TopicInstance> tis = em.createNamedQuery(
                "TopicInstance.findByTopicCharacteristicWithGeomz",
                TopicInstance.class)
                .setParameter("value", em.find(
                        TopicCharacteristic.class,
                        topicCharacteristicId))
                 .setFirstResult(offset)
                 .setMaxResults(size)
                .getResultList();
        for(TopicInstance ti : tis) {
            tip.add(this.mapToPojo(locale, ti));
        }
        return tip;
    }

	/**
	 * This is a special count method that returns the count of Topic
	 * Characteristic objects that contains a 3D geometry.
     *
     * @param topicCharacteristicId the Topic Characteristic Id
     * @return                      the count of objects
	 */
	public long getCountWithGeomz(UUID topicCharacteristicId) {
	    return em.createNamedQuery(
	                "TopicInstance.countByTopicGeomz",
	                Long.class)
	            .setParameter("value", em.find(
	                    TopicCharacteristic.class,
	                    topicCharacteristicId))
	            .getSingleResult()
	            .longValue();
	}

	@Override
	public MappingResult<TopicInstance> mapToModel(
			TopicInstancePojo pojo,
			TopicInstance ti) {

        // TODO set the model values

        // return the model as mapping result
        return new MappingResult<TopicInstance>(ti.getId(), ti);
	}

}
