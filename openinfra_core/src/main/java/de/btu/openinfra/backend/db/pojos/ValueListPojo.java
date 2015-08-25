package de.btu.openinfra.backend.db.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValueListPojo extends OpenInfraMetaDataPojo {

	private PtFreeTextPojo names;
	private PtFreeTextPojo descriptions;

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
