package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopicCharacteristicPojo extends OpenInfraMetaDataPojo {

	private PtFreeTextPojo descriptions;
	private ValueListValuePojo topic;
	private UUID projectId;

	public PtFreeTextPojo getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(PtFreeTextPojo descriptions) {
		this.descriptions = descriptions;
	}

	public ValueListValuePojo getTopic() {
		return topic;
	}

	public void setTopic(ValueListValuePojo topic) {
		this.topic = topic;
	}

	public UUID getProjectId() {
		return projectId;
	}

	public void setProjectId(UUID projectId) {
		this.projectId = projectId;
	}

}
