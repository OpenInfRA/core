package de.btu.openinfra.backend.db.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValueListValueAssociationPojo extends OpenInfraMetaDataPojo {

	private ValueListValuePojo associatedValueListValue;
	private ValueListValuePojo relationship;

	public ValueListValuePojo getAssociatedValueListValue() {
		return associatedValueListValue;
	}

	public void setAssociatedValueListValue(
		ValueListValuePojo associatedValueListValue) {
		this.associatedValueListValue = associatedValueListValue;
	}

	public ValueListValuePojo getRelationship() {
		return relationship;
	}

	public void setRelationship(ValueListValuePojo relationship) {
		this.relationship = relationship;
	}

}
