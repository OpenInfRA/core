package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttributeTypeGroupToTopicCharacteristicPojo
                extends OpenInfraMetaDataPojo {

	private AttributeTypeGroupPojo attributeTypeGroup;
	private UUID topicCharacteristicId;
	private MultiplicityPojo multiplicity;
	private int order;

	public AttributeTypeGroupPojo getAttributeTypeGroup() {
		return attributeTypeGroup;
	}

	public void setAttributeTypeGroup(
			AttributeTypeGroupPojo attributeTypeGroup) {
		this.attributeTypeGroup = attributeTypeGroup;
	}

	public UUID getTopicCharacteristicId() {
		return topicCharacteristicId;
	}

	public void setTopicCharacteristicId(UUID topicCharacteristicId) {
		this.topicCharacteristicId = topicCharacteristicId;
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
