package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopicCharacteristicToAttributeTypeGroupPojo
                extends OpenInfraMetaDataPojo {

	private TopicCharacteristicPojo topicCharacteristic;
	private UUID attributTypeGroupId;
	private MultiplicityPojo multiplicity;
	private int order;

	public TopicCharacteristicPojo getTopicCharacteristic() {
		return topicCharacteristic;
	}

	public void setTopicCharacteristic(TopicCharacteristicPojo topicCharacteristic) {
		this.topicCharacteristic = topicCharacteristic;
	}

	public UUID getAttributTypeGroupId() {
		return attributTypeGroupId;
	}

	public void setAttributTypeGroupId(UUID attributTypeGroupId) {
		this.attributTypeGroupId = attributTypeGroupId;
	}

	public MultiplicityPojo getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(MultiplicityPojo multiplicity) {
		this.multiplicity = multiplicity;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
