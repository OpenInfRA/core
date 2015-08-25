package de.btu.openinfra.backend.db.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RelationshipTypePojo extends OpenInfraMetaDataPojo {

	private ValueListValuePojo description;
	private ValueListValuePojo RelationshipType;

	public ValueListValuePojo getDescription() {
		return description;
	}

	public void setDescription(ValueListValuePojo description) {
		this.description = description;
	}

	public ValueListValuePojo getRelationshipType() {
		return RelationshipType;
	}

	public void setRelationshipType(ValueListValuePojo relationshipType) {
		RelationshipType = relationshipType;
	}

}
