package de.btu.openinfra.backend.db.pojos;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProjectPojo extends OpenInfraMetaDataPojo {

	private UUID subprojectOf;
	private PtFreeTextPojo names;
	private PtFreeTextPojo descriptions;

	public UUID getSubprojectOf() {
		return subprojectOf;
	}

	public void setSubprojectOf(UUID subprojectOf) {
		this.subprojectOf = subprojectOf;
	}

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

}
