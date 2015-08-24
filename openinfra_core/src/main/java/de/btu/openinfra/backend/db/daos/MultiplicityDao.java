package de.btu.openinfra.backend.db.daos;

import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.Multiplicity;
import de.btu.openinfra.backend.db.pojos.MultiplicityPojo;

/**
 * This class represents the Multiplicity and is used to access the underlying
 * layer generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class MultiplicityDao extends
	OpenInfraDao<MultiplicityPojo, Multiplicity> {

	/**
	 * This is the required constructor which calls the super constructor and in
	 * turn creates the corresponding entity manager.
	 *
	 * @param currentProjectId the current project id (this should be null when
	 *                         the system schema is selected)
	 * @param schema           the required schema
	 */
	public MultiplicityDao(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema, Multiplicity.class);
	}

	@Override
	public MultiplicityPojo mapToPojo(Locale locale, Multiplicity mp) {
		return mapToPojoStatically(mp);
	}

	/**
	 * This is a static representation of the mapToPojo method and maps a JPA
	 * model object into the referring POJO object.
	 *
	 * @param mp the JPA model object
	 * @return   the referring PJO object
	 */
	public static MultiplicityPojo mapToPojoStatically(Multiplicity mp) {
		if(mp != null) {
			MultiplicityPojo pojo = new MultiplicityPojo();
			// Obviously, the maximum value can be null
			if(mp.getMaxValue() != null) {
				pojo.setMax(mp.getMaxValue());
			}
			pojo.setMin(mp.getMinValue());
			pojo.setUuid(mp.getId());
			pojo.setTrid(mp.getXmin().intValue());
			return pojo;
		} else {
			return null;
		} // end if else
	}

	@Override
	public MappingResult<Multiplicity> mapToModel(
			MultiplicityPojo pojo,
			Multiplicity mp) {

	    // set the max value
		mp.setMaxValue(pojo.getMax());

		// set the min value
		mp.setMinValue(pojo.getMin());

		// return the model as mapping result
		return new MappingResult<Multiplicity>(mp.getId(), mp);
	}
}
