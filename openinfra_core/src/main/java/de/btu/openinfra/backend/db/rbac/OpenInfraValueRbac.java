package de.btu.openinfra.backend.db.rbac;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.UriInfo;

import org.json.simple.JSONObject;

import de.btu.openinfra.backend.db.OpenInfraOrderBy;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.OpenInfraSortOrder;
import de.btu.openinfra.backend.db.daos.OpenInfraValueDao;
import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;
import de.btu.openinfra.backend.exception.OpenInfraWebException;

public abstract class OpenInfraValueRbac<
		TypePojo extends OpenInfraPojo,
		TypeModel extends OpenInfraModelObject,
		TypeModelValue,
		TypeDao extends OpenInfraValueDao<TypePojo, TypeModel, TypeModelValue>>
			extends OpenInfraRbac<TypePojo, TypeModel, TypeDao> {

	protected Class<TypeModelValue> valueClass;

	/**
	 * This defines the constructor types in order to call the constructor in a
	 * generic way via:
	 */
	protected Class<?>[] constructorTypesValue =
			new Class[] {UUID.class, OpenInfraSchemas.class};

	/**
	 * Refers to the OpenInfraValueDao
	 *
	 * @param currentProjectId
	 * @param schema
	 * @param valueClass
	 * @param dao
	 */
	protected OpenInfraValueRbac(
			UUID currentProjectId,
			OpenInfraSchemas schema,
			Class<TypeModelValue> valueClass,
			Class<TypeDao> dao) {
		super(currentProjectId, schema, dao);
		this.valueClass = valueClass;
	}

	public List<TypePojo> read(
			OpenInfraHttpMethod httpMethod,
			UriInfo uriInfo,
			Locale locale,
			UUID valueId,
			int offset,
			int size) {
		checkPermission(httpMethod, uriInfo);
		try {
			return dao.getDeclaredConstructor(
					constructorTypesValue).newInstance(
							currentProjectId,
							schema).read(locale, valueId, offset, size);
		} catch (InstantiationException   | IllegalAccessException | 
				 IllegalArgumentException | InvocationTargetException | 
				 NoSuchMethodException    | SecurityException ex) {
			throw new OpenInfraWebException(ex);
		}
	}

	/**
     * This is a generic method which is provided by all RBAC classes.
     *
     * @param pojo
     * @param firstAssociationId
     * @param firstAssociationIdFromPojo
     * @return
     * @throws RuntimeException
     */
    public UUID createOrUpdate(
            OpenInfraHttpMethod httpMethod,
            UriInfo uriInfo,
            TypePojo pojo,
            UUID firstAssociationId,
            UUID firstAssociationIdFromPojo,
            JSONObject json)
            throws RuntimeException {
        checkPermission(httpMethod, uriInfo);
        try {
            return dao.getDeclaredConstructor(constructorTypes).newInstance(
                    currentProjectId,
                    schema).createOrUpdate(
                            pojo,
                            firstAssociationId,
                            firstAssociationIdFromPojo,
                            json);
		} catch (InstantiationException   | IllegalAccessException | 
				 IllegalArgumentException | InvocationTargetException | 
				 NoSuchMethodException    | SecurityException ex) {
			throw new OpenInfraWebException(ex);
		}
    }

	public List<TypePojo> read(
			OpenInfraHttpMethod httpMethod,
			UriInfo uriInfo,
            Locale locale,
            UUID valueId,
            OpenInfraSortOrder order,
            OpenInfraOrderBy column,
            int offset,
            int size) {
		checkPermission(httpMethod, uriInfo);
		try {
			return dao.getDeclaredConstructor(
					constructorTypesValue).newInstance(
							currentProjectId,
							schema).read(
									locale,
									valueId,
									order,
									column,
									offset,
									size);
		} catch (InstantiationException   | IllegalAccessException | 
				 IllegalArgumentException | InvocationTargetException | 
				 NoSuchMethodException    | SecurityException ex) {
			throw new OpenInfraWebException(ex);
		}
	}

	public Long getCount(
			OpenInfraHttpMethod httpMethod,
			UriInfo uriInfo, UUID valueId) {
		checkPermission(httpMethod, uriInfo);
		try {
			return dao.getDeclaredConstructor(
					constructorTypesValue).newInstance(
							currentProjectId,
							schema).getCount(valueId);
		} catch (InstantiationException   | IllegalAccessException | 
				 IllegalArgumentException | InvocationTargetException | 
				 NoSuchMethodException    | SecurityException ex) {
			throw new OpenInfraWebException(ex);
		}
	}
}
