package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TopicInstanceAssociationPojo extends OpenInfraMetaDataPojo {

	private TopicInstancePojo associatedInstance;
	private RelationshipTypePojo relationshipType;

	public TopicInstanceAssociationPojo() {}
	public TopicInstanceAssociationPojo(
			UUID uuid,
			TopicInstancePojo tip,
			RelationshipTypePojo rtp) {
		setUuid(uuid);
		associatedInstance = tip;
		relationshipType = rtp;
	}

	public RelationshipTypePojo getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(RelationshipTypePojo relationshipType) {
		this.relationshipType = relationshipType;
	}

	public TopicInstancePojo getAssociatedInstance() {
		return associatedInstance;
	}

	public void setAssociatedInstance(TopicInstancePojo associatedInstance) {
		this.associatedInstance = associatedInstance;
	}

}
