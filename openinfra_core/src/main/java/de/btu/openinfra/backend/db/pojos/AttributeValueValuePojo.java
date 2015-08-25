package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;


public class AttributeValueValuePojo extends OpenInfraMetaDataPojo {

	private UUID topicInstanceId;
	private PtFreeTextPojo value;
	private UUID attributeTypeToAttributeTypeGroupId;

	public UUID getTopicInstanceId() {
		return topicInstanceId;
	}

	public void setTopicInstanceId(UUID topicInstanceId) {
		this.topicInstanceId = topicInstanceId;
	}

	public PtFreeTextPojo getValue() {
		return value;
	}

	public void setValue(PtFreeTextPojo value) {
		this.value = value;
	}

    public UUID getAttributeTypeToAttributeTypeGroupId() {
        return attributeTypeToAttributeTypeGroupId;
    }

    public void setAttributeTypeToAttributeTypeGroupId(
            UUID attributeTypeToAttributeTypeGroupId) {
        this.attributeTypeToAttributeTypeGroupId =
                attributeTypeToAttributeTypeGroupId;
    }

}
