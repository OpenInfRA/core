package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttributeTypeGroupToAttributeTypePojo
                extends OpenInfraMetaDataPojo {

	private AttributeTypeGroupPojo attributeTypeGroup;
	private UUID attributeTypeId;
	private MultiplicityPojo multiplicity;
	private ValueListValuePojo defaultValue;
	private Integer order;

	public AttributeTypeGroupPojo getAttributeTypeGroup() {
		return attributeTypeGroup;
	}

	public void setAttributeTypeGroup(
	        AttributeTypeGroupPojo attributeTypeGroup) {
		this.attributeTypeGroup = attributeTypeGroup;
	}

	public UUID getAttributeTypeId() {
		return attributeTypeId;
	}

	public void setAttributeTypeId(UUID attributeTypeId) {
		this.attributeTypeId = attributeTypeId;
	}

	public MultiplicityPojo getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(MultiplicityPojo multiplicity) {
		this.multiplicity = multiplicity;
	}

	public ValueListValuePojo getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(ValueListValuePojo defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
