package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttributeTypeGroupPojo extends OpenInfraMetaDataPojo {

	// Fields of the object AttributeTypeGroup
	private PtFreeTextPojo names;
	private PtFreeTextPojo descriptions;
	private UUID subgroupOf;

	public PtFreeTextPojo getNames() {
		return names;
	}

	public void setNames(PtFreeTextPojo names) {
		this.names = names;
	}

	public PtFreeTextPojo getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(PtFreeTextPojo descriptions) {
		this.descriptions = descriptions;
	}

	public UUID getSubgroupOf() {
		return subgroupOf;
	}

	public void setSubgroupOf(UUID subgroupOf) {
		this.subgroupOf = subgroupOf;
	}

}
