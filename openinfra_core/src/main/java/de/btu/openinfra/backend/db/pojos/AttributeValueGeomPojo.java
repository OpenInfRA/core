package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import de.btu.openinfra.backend.db.daos.AttributeValueGeomType;

@XmlRootElement
public class AttributeValueGeomPojo extends OpenInfraMetaDataPojo {

	private UUID topicInstanceId;
	private String geom;
	private AttributeValueGeomType geomType;
    private UUID attributeTypeToAttributeTypeGroupId;

	public UUID getTopicInstanceId() {
		return topicInstanceId;
	}

	public void setTopicInstanceId(UUID topicInstanceId) {
		this.topicInstanceId = topicInstanceId;
	}

	public String getGeom() {
		return geom;
	}

	public void setGeom(String geom) {
		this.geom = geom;
	}

	public AttributeValueGeomType getGeomType() {
		return geomType;
	}

	public void setGeomType(AttributeValueGeomType geomType) {
		this.geomType = geomType;
	}

	public UUID getAttributeTypeToAttributeTypeGroupId() {
        return attributeTypeToAttributeTypeGroupId;
    }

    public void setAttributeTypeToAttributeTypeGroupId(
            UUID attributeTypeToAttributeTypeGroupId) {
        this.attributeTypeToAttributeTypeGroupId =
                attributeTypeToAttributeTypeGroupId;
    }

}
