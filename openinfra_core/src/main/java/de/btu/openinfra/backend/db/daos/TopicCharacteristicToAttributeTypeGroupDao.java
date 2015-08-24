package de.btu.openinfra.backend.db.daos;

import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.AttributeTypeGroup;
import de.btu.openinfra.backend.db.jpa.model.AttributeTypeGroupToTopicCharacteristic;
import de.btu.openinfra.backend.db.jpa.model.MetaData;
import de.btu.openinfra.backend.db.jpa.model.TopicCharacteristic;
import de.btu.openinfra.backend.db.pojos.TopicCharacteristicToAttributeTypeGroupPojo;

/**
 * This class represents the TopicCharacteristicToAttributeTypeGroup and is used
 * to access the underlying layer generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class TopicCharacteristicToAttributeTypeGroupDao extends
	OpenInfraValueValueDao<TopicCharacteristicToAttributeTypeGroupPojo,
	AttributeTypeGroupToTopicCharacteristic, AttributeTypeGroup,
	TopicCharacteristic> {

	/**
     * This is the required constructor which calls the super constructor,
     * creates the corresponding entity manager and initiate
     * topicCharacteristicDao.
     *
     * @param currentProjectId the current project id (this should be null when
     *                         the system schema is selected)
     * @param schema           the required schema
     */
	public TopicCharacteristicToAttributeTypeGroupDao(
			UUID currentProjectId,
			OpenInfraSchemas schema) {
		super(currentProjectId, schema,
				AttributeTypeGroupToTopicCharacteristic.class,
				AttributeTypeGroup.class, TopicCharacteristic.class);
	}

	@Override
	public TopicCharacteristicToAttributeTypeGroupPojo mapToPojo(
			Locale locale,
			AttributeTypeGroupToTopicCharacteristic modelObject) {

		if(modelObject != null) {
			TopicCharacteristicToAttributeTypeGroupPojo pojo =
					new TopicCharacteristicToAttributeTypeGroupPojo();

			pojo.setUuid(modelObject.getId());
			pojo.setTrid(modelObject.getXmin());
			pojo.setTopicCharacteristic(
	                TopicCharacteristicDao.mapToPojoStatically(
	                        locale,
	                        modelObject.getTopicCharacteristic(),
	                        em.find(
	                                MetaData.class,
	                                modelObject.getTopicCharacteristic(
	                                        ).getId())));
			pojo.setAttributTypeGroupId(
				modelObject.getAttributeTypeGroup().getId());
			pojo.setMultiplicity(MultiplicityDao.mapToPojoStatically(
					modelObject.getMultiplicityBean()));

			if(modelObject.getOrder() != null) {
				pojo.setOrder(modelObject.getOrder());
			}

			return pojo;
		}
		else {
			return null;
		}
	}

	@Override
	public MappingResult<AttributeTypeGroupToTopicCharacteristic> mapToModel(
			TopicCharacteristicToAttributeTypeGroupPojo pojo,
			AttributeTypeGroupToTopicCharacteristic att) {

        // TODO set the model values

        // return the model as mapping result
        return new MappingResult<AttributeTypeGroupToTopicCharacteristic>(
                att.getId(), att);
	}

}
