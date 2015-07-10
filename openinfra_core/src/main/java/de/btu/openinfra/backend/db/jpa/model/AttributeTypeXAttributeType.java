package de.btu.openinfra.backend.db.jpa.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the attribute_type_x_attribute_type database table.
 * 
 */
@Entity
@Table(name="attribute_type_x_attribute_type")
@NamedQuery(name="AttributeTypeXAttributeType.findAll", query="SELECT a FROM AttributeTypeXAttributeType a")
public class AttributeTypeXAttributeType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	//bi-directional many-to-one association to AttributeType
	@ManyToOne
	@JoinColumn(name="attribute_type_1")
	private AttributeType attributeType1Bean;

	//bi-directional many-to-one association to AttributeType
	@ManyToOne
	@JoinColumn(name="attribute_type_2")
	private AttributeType attributeType2Bean;

	//bi-directional many-to-one association to ValueListValue
	@ManyToOne
	@JoinColumn(name="relationship")
	private ValueListValue valueListValue;

	public AttributeTypeXAttributeType() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AttributeType getAttributeType1Bean() {
		return this.attributeType1Bean;
	}

	public void setAttributeType1Bean(AttributeType attributeType1Bean) {
		this.attributeType1Bean = attributeType1Bean;
	}

	public AttributeType getAttributeType2Bean() {
		return this.attributeType2Bean;
	}

	public void setAttributeType2Bean(AttributeType attributeType2Bean) {
		this.attributeType2Bean = attributeType2Bean;
	}

	public ValueListValue getValueListValue() {
		return this.valueListValue;
	}

	public void setValueListValue(ValueListValue valueListValue) {
		this.valueListValue = valueListValue;
	}

}