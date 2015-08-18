package de.btu.openinfra.backend.db.daos;

import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.RelationshipType;
import de.btu.openinfra.backend.db.jpa.model.RelationshipTypeToTopicCharacteristic;
import de.btu.openinfra.backend.db.jpa.model.TopicCharacteristic;
import de.btu.openinfra.backend.db.pojos.RelationshipTypeToTopicCharacteristicPojo;

/**
 * This class represents the RelationshipTypeToTopicCharacteristic and is used
 * to access the underlying layer generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class RelationshipTypeToTopicCharacteristicDao
	extends OpenInfraValueValueDao<RelationshipTypeToTopicCharacteristicPojo,
	RelationshipTypeToTopicCharacteristic, TopicCharacteristic,
	RelationshipType> {

    /**
     * This is the required constructor which calls the super constructor and in
     * turn creates the corresponding entity manager.
     *
     * @param currentProjectId the current project id (this should be null when
     *                         the system schema is selected)
     * @param schema           the required schema
     */
	public RelationshipTypeToTopicCharacteristicDao(
			UUID currentProjectId,
			OpenInfraSchemas schema) {
		super(currentProjectId, schema,
				RelationshipTypeToTopicCharacteristic.class,
				TopicCharacteristic.class, RelationshipType.class);
	}

	@Override
	public RelationshipTypeToTopicCharacteristicPojo mapToPojo(
			Locale locale,
			RelationshipTypeToTopicCharacteristic association) {
		return mapToPojoStatically(
			locale,
			association);
	}

	@Override
	public MappingResult<RelationshipTypeToTopicCharacteristic> mapToModel(
			RelationshipTypeToTopicCharacteristicPojo pojo,
			RelationshipTypeToTopicCharacteristic rtt) {

        // TODO set the model values

        // return the model as mapping result
        return new MappingResult<RelationshipTypeToTopicCharacteristic>(
                rtt.getId(), rtt);
	}

	/**
     * This method implements the method mapToPojo in a static way.
     *
     * @param locale       the requested language as Java.util locale
     * @param association  the model object
     * @return             the POJO object when the model object is not null
     *                     else null
     */
	public static RelationshipTypeToTopicCharacteristicPojo mapToPojoStatically(
			Locale locale,
			RelationshipTypeToTopicCharacteristic association) {

		if(association != null) {
			RelationshipTypeToTopicCharacteristicPojo pojo =
					new RelationshipTypeToTopicCharacteristicPojo();

			pojo.setUuid(association.getId());
			pojo.setTrid(association.getXmin());
			pojo.setTopicCharacteristicId(
				association.getTopicCharacteristic().getId());
			pojo.setMultiplicity(MultiplicityDao.mapToPojoStatically(
					association.getMultiplicityBean()));
			pojo.setRelationshipType(
				RelationshipTypeDao.mapToPojoStatically(
					locale,
					association.getRelationshipType()));

			return pojo;
		}
		else {
			return null;
		}
	}
}
