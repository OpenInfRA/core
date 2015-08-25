package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopicCharacteristicToRelationshipTypePojo
                extends OpenInfraMetaDataPojo {

	private UUID relationshipType;
	private MultiplicityPojo multiplicity;
	private TopicCharacteristicPojo topicCharacteristic;

	public UUID getRelationshipe() {
		return relationshipType;
	}

	public void setRelationshipe(UUID relationShipe) {
		this.relationshipType = relationShipe;
	}

	public MultiplicityPojo getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(MultiplicityPojo multiplicity) {
		this.multiplicity = multiplicity;
	}

	public TopicCharacteristicPojo getTopicCharacteristic() {
		return topicCharacteristic;
	}

	public void setTopicCharacteristic(
			TopicCharacteristicPojo topicCharacteristic) {
		this.topicCharacteristic = topicCharacteristic;
	}


}
