package de.btu.openinfra.backend.db.jpa.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the relationship_type_to_topic_characteristic database table.
 * 
 */
@Entity
@Table(name="relationship_type_to_topic_characteristic")
@NamedQueries({
	@NamedQuery(name="RelationshipTypeToTopicCharacteristic.findAll", 
			query="SELECT r FROM RelationshipTypeToTopicCharacteristic r"),
	@NamedQuery(name="RelationshipTypeToTopicCharacteristic"
			+ ".findfindByTopicCharacteristic", 
			query="SELECT r "
					+ "FROM RelationshipTypeToTopicCharacteristic r "
					+ "WHERE r.topicCharacteristic = :value")
})
public class RelationshipTypeToTopicCharacteristic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private UUID id;

	//bi-directional many-to-one association to Multiplicity
	@ManyToOne
	@JoinColumn(name="multiplicity")
	private Multiplicity multiplicityBean;

	//bi-directional many-to-one association to RelationshipType
	@ManyToOne
	@JoinColumn(name="relationship_type_id")
	private RelationshipType relationshipType;

	//bi-directional many-to-one association to TopicCharacteristic
	@ManyToOne
	@JoinColumn(name="topic_characteristic_id")
	private TopicCharacteristic topicCharacteristic;

	public RelationshipTypeToTopicCharacteristic() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Multiplicity getMultiplicityBean() {
		return this.multiplicityBean;
	}

	public void setMultiplicityBean(Multiplicity multiplicityBean) {
		this.multiplicityBean = multiplicityBean;
	}

	public RelationshipType getRelationshipType() {
		return this.relationshipType;
	}

	public void setRelationshipType(RelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}

	public TopicCharacteristic getTopicCharacteristic() {
		return this.topicCharacteristic;
	}

	public void setTopicCharacteristic(TopicCharacteristic topicCharacteristic) {
		this.topicCharacteristic = topicCharacteristic;
	}

}