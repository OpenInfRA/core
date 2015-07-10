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
 * The persistent class for the value_list_x_value_list database table.
 * 
 */
@Entity
@Table(name="value_list_x_value_list")
@NamedQuery(name="ValueListXValueList.findAll", query="SELECT v FROM ValueListXValueList v")
public class ValueListXValueList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	//bi-directional many-to-one association to ValueList
	@ManyToOne
	@JoinColumn(name="value_list_1")
	private ValueList valueList1Bean;

	//bi-directional many-to-one association to ValueList
	@ManyToOne
	@JoinColumn(name="value_list_2")
	private ValueList valueList2Bean;

	//bi-directional many-to-one association to ValueListValue
	@ManyToOne
	@JoinColumn(name="relationship")
	private ValueListValue valueListValue;

	public ValueListXValueList() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ValueList getValueList1Bean() {
		return this.valueList1Bean;
	}

	public void setValueList1Bean(ValueList valueList1Bean) {
		this.valueList1Bean = valueList1Bean;
	}

	public ValueList getValueList2Bean() {
		return this.valueList2Bean;
	}

	public void setValueList2Bean(ValueList valueList2Bean) {
		this.valueList2Bean = valueList2Bean;
	}

	public ValueListValue getValueListValue() {
		return this.valueListValue;
	}

	public void setValueListValue(ValueListValue valueListValue) {
		this.valueListValue = valueListValue;
	}

}