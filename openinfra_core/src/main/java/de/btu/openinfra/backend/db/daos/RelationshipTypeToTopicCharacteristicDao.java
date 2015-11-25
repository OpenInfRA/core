package de.btu.openinfra.backend.db.daos;

import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.RelationshipType;
import de.btu.openinfra.backend.db.jpa.model.RelationshipTypeToTopicCharacteristic;
import de.btu.openinfra.backend.db.jpa.model.TopicCharacteristic;
import de.btu.openinfra.backend.db.pojos.RelationshipTypeToTopicCharacteristicPojo;
import de.btu.openinfra.backend.exception.OpenInfraEntityException;
import de.btu.openinfra.backend.exception.OpenInfraExceptionTypes;

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
			RelationshipTypeToTopicCharacteristic rtt) {

        RelationshipTypeToTopicCharacteristicPojo pojo =
                new RelationshipTypeToTopicCharacteristicPojo(rtt);

        try {
            pojo.setTopicCharacteristicId(
                    rtt.getTopicCharacteristic().getId());
            pojo.setMultiplicity(new MultiplicityDao(
                    currentProjectId,
                    schema).mapToPojo(
                            null,
                            rtt.getMultiplicityBean()));
            pojo.setRelationshipType(new RelationshipTypeDao(
                    currentProjectId,
                    schema).mapToPojo(
                            locale,
                            rtt.getRelationshipType()));

            return pojo;
        } catch (NullPointerException npe) {
            throw new OpenInfraEntityException(
                    OpenInfraExceptionTypes.MISSING_DATA_IN_POJO);
        }
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
}
