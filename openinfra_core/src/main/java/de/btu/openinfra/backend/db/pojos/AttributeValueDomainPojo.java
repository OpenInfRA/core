package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;


public class AttributeValueDomainPojo extends OpenInfraMetaDataPojo {

	private UUID topicInstanceId;
	private ValueListValuePojo domain;
	private UUID attributeTypeToAttributeTypeGroupId;

    public UUID getTopicInstanceId() {
		return topicInstanceId;
	}

	public void setTopicInstanceId(UUID topicInstanceId) {
		this.topicInstanceId = topicInstanceId;
	}

	public ValueListValuePojo getDomain() {
		return domain;
	}

	public void setDomain(ValueListValuePojo domain) {
		this.domain = domain;
	}

	public UUID getAttributeTypeToAttributeTypeGroupId() {
        return attributeTypeToAttributeTypeGroupId;
    }

    public void setAttributeTypeToAttributeTypeGroupId(
            UUID attributeTypeToAttributeTypeGroupId) {
        this.attributeTypeToAttributeTypeGroupId
        = attributeTypeToAttributeTypeGroupId;
    }

}
