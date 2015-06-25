package de.btu.openinfra.backend.db.jpa.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the topic_characteristic database table.
 * 
 */
@Entity
@Table(name="topic_characteristic")
@NamedQueries({
	@NamedQuery(name="TopicCharacteristic.count", 
		query="SELECT COUNT(t) FROM TopicCharacteristic t "),
	@NamedQuery(name="TopicCharacteristic.findAll",	
		query="SELECT t "
				+ "FROM TopicCharacteristic t "),
	@NamedQuery(name="TopicCharacteristic.findByProject",
		query="SELECT t FROM TopicCharacteristic t "
					+ "WHERE t.project = :value "),
    // This query selects an orderd list of TopicCharacteristics by a like 
	// filter applied on LocalizedCharacterStrings
	@NamedQuery(name="TopicCharacteristic.findByDescription",
		query="SELECT t FROM TopicCharacteristic t "
				+ "JOIN t.ptFreeText.localizedCharacterStrings lcs "
				+ "WHERE t.id = ANY ("
					+ "SELECT ps.id "
					+ "FROM PtFreeText p "
					+ "JOIN p.topicCharacteristics ps "
					+ "WHERE p.id = ANY ("
						+ "SELECT l.ptFreeText.id "
						+ "FROM LocalizedCharacterString l "
						+ "WHERE l.ptLocale = :value "
						+ "AND l.freeText LIKE :filter)) "
				+ "ORDER BY lcs.freeText")
})
public class TopicCharacteristic implements Serializable, OpenInfraModelObject {
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	//bi-directional many-to-one association to AttributeTypeGroupToTopicCharacteristic
	@OneToMany(mappedBy="topicCharacteristic")
	private List<AttributeTypeGroupToTopicCharacteristic> attributeTypeGroupToTopicCharacteristics;

	//bi-directional many-to-one association to RelationshipTypeToTopicCharacteristic
	@OneToMany(mappedBy="topicCharacteristic")
	private List<RelationshipTypeToTopicCharacteristic> relationshipTypeToTopicCharacteristics;

	//bi-directional many-to-one association to Project
	@ManyToOne
	private Project project;

	//bi-directional many-to-one association to PtFreeText
	@ManyToOne
	@JoinColumn(name="description")
	private PtFreeText ptFreeText;

	//bi-directional many-to-one association to ValueListValue
	@ManyToOne
	@JoinColumn(name="topic")
	private ValueListValue valueListValue;

	//bi-directional many-to-one association to TopicInstance
	@OneToMany(mappedBy="topicCharacteristic")
	private List<TopicInstance> topicInstances;

	public TopicCharacteristic() {
	}

	public UUID getId() {
		return this.id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	public List<AttributeTypeGroupToTopicCharacteristic> getAttributeTypeGroupToTopicCharacteristics() {
		return this.attributeTypeGroupToTopicCharacteristics;
	}

	public void setAttributeTypeGroupToTopicCharacteristics(List<AttributeTypeGroupToTopicCharacteristic> attributeTypeGroupToTopicCharacteristics) {
		this.attributeTypeGroupToTopicCharacteristics = attributeTypeGroupToTopicCharacteristics;
	}

	public AttributeTypeGroupToTopicCharacteristic addAttributeTypeGroupToTopicCharacteristic(AttributeTypeGroupToTopicCharacteristic attributeTypeGroupToTopicCharacteristic) {
		getAttributeTypeGroupToTopicCharacteristics().add(attributeTypeGroupToTopicCharacteristic);
		attributeTypeGroupToTopicCharacteristic.setTopicCharacteristic(this);

		return attributeTypeGroupToTopicCharacteristic;
	}

	public AttributeTypeGroupToTopicCharacteristic removeAttributeTypeGroupToTopicCharacteristic(AttributeTypeGroupToTopicCharacteristic attributeTypeGroupToTopicCharacteristic) {
		getAttributeTypeGroupToTopicCharacteristics().remove(attributeTypeGroupToTopicCharacteristic);
		attributeTypeGroupToTopicCharacteristic.setTopicCharacteristic(null);

		return attributeTypeGroupToTopicCharacteristic;
	}

	public List<RelationshipTypeToTopicCharacteristic> getRelationshipTypeToTopicCharacteristics() {
		return this.relationshipTypeToTopicCharacteristics;
	}

	public void setRelationshipTypeToTopicCharacteristics(List<RelationshipTypeToTopicCharacteristic> relationshipTypeToTopicCharacteristics) {
		this.relationshipTypeToTopicCharacteristics = relationshipTypeToTopicCharacteristics;
	}

	public RelationshipTypeToTopicCharacteristic addRelationshipTypeToTopicCharacteristic(RelationshipTypeToTopicCharacteristic relationshipTypeToTopicCharacteristic) {
		getRelationshipTypeToTopicCharacteristics().add(relationshipTypeToTopicCharacteristic);
		relationshipTypeToTopicCharacteristic.setTopicCharacteristic(this);

		return relationshipTypeToTopicCharacteristic;
	}

	public RelationshipTypeToTopicCharacteristic removeRelationshipTypeToTopicCharacteristic(RelationshipTypeToTopicCharacteristic relationshipTypeToTopicCharacteristic) {
		getRelationshipTypeToTopicCharacteristics().remove(relationshipTypeToTopicCharacteristic);
		relationshipTypeToTopicCharacteristic.setTopicCharacteristic(null);

		return relationshipTypeToTopicCharacteristic;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public PtFreeText getPtFreeText() {
		return this.ptFreeText;
	}

	public void setPtFreeText(PtFreeText ptFreeText) {
		this.ptFreeText = ptFreeText;
	}

	public ValueListValue getValueListValue() {
		return this.valueListValue;
	}

	public void setValueListValue(ValueListValue valueListValue) {
		this.valueListValue = valueListValue;
	}

	public List<TopicInstance> getTopicInstances() {
		return this.topicInstances;
	}

	public void setTopicInstances(List<TopicInstance> topicInstances) {
		this.topicInstances = topicInstances;
	}

	public TopicInstance addTopicInstance(TopicInstance topicInstance) {
		getTopicInstances().add(topicInstance);
		topicInstance.setTopicCharacteristic(this);

		return topicInstance;
	}

	public TopicInstance removeTopicInstance(TopicInstance topicInstance) {
		getTopicInstances().remove(topicInstance);
		topicInstance.setTopicCharacteristic(null);

		return topicInstance;
	}

}