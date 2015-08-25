package de.btu.openinfra.backend.db.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValueListAssociationPojo extends OpenInfraMetaDataPojo {

	private ValueListPojo associatedValueList;
	private ValueListValuePojo relationship;

	public ValueListPojo getAssociatedValueList() {
		return associatedValueList;
	}

	public void setAssociatedValueList(ValueListPojo associatedValueList) {
		this.associatedValueList = associatedValueList;
	}

	public ValueListValuePojo getRelationship() {
		return relationship;
	}

	public void setRelationship(ValueListValuePojo relationship) {
		this.relationship = relationship;
	}


}
