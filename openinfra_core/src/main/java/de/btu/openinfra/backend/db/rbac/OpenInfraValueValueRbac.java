package de.btu.openinfra.backend.db.rbac;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.UriInfo;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.OpenInfraValueValueDao;
import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;
import de.btu.openinfra.backend.exception.OpenInfraWebException;

public class OpenInfraValueValueRbac<
	TypePojo extends OpenInfraPojo,
	TypeModel extends OpenInfraModelObject,
	TypeModelValue,
	TypeModelValue2,
	TypeDao extends OpenInfraValueValueDao<TypePojo,
		TypeModel, TypeModelValue, TypeModelValue2>>
	extends OpenInfraValueRbac<TypePojo, TypeModel, TypeModelValue, TypeDao> {

	protected Class<TypeModelValue2> valueClass2;

	/**
	 * Refers to the OpenInfraValueValueDao
	 *
	 * @param currentProjectId
	 * @param schema
	 * @param valueClass
	 * @param valueClass2
	 * @param dao
	 */
	protected OpenInfraValueValueRbac(
			UUID currentProjectId,
			OpenInfraSchemas schema,
			Class<TypeModelValue> valueClass,
			Class<TypeModelValue2> valueClass2,
			Class<TypeDao> dao) {
		super(currentProjectId, schema, valueClass, dao);
		this.valueClass2 = valueClass2;
	}

	/**
	 * This is a generic method to delete associations.
	 *
	 * @param httpMethod the method which has been used to access the current
	 * resource
	 * @param uriInfo the URI information
	 * @param uuid1 first uuid
	 * @param uuid2 second uuid
	 * @return the uuid of the association when the association was deleted,
     * otherwise null
	 */
	public UUID delete(
	        OpenInfraHttpMethod httpMethod,
            UriInfo uriInfo,
            UUID uuid1,
            UUID uuid2) {
	    checkPermission(httpMethod, uriInfo);
        try {
            return dao.getDeclaredConstructor(constructorTypes).newInstance(
                    currentProjectId,
                    schema).delete(uuid1, uuid2);
        } catch (InstantiationException   | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException |
                 NoSuchMethodException    | SecurityException ex) {
            throw new OpenInfraWebException(ex);
        }
	}

	public List<TypePojo> read(
			OpenInfraHttpMethod httpMethod,
			UriInfo uriInfo,
			Locale locales,
			UUID valueId,
			UUID valueId2,
			int offset,
			int size) {
		checkPermission(httpMethod, uriInfo);
		try {
			return dao.getDeclaredConstructor(
					constructorTypes).newInstance(
							currentProjectId,
							schema).read(
									locales,
									valueId,
									valueId2,
									offset,
									size);
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
     * @param secondAssociationId
     * @param secondAssociationIdFromPojo
     * @return
     * @throws RuntimeException
     */
    public UUID createOrUpdate(
            OpenInfraHttpMethod httpMethod,
            UriInfo uriInfo,
            TypePojo pojo,
            UUID firstAssociationId,
            UUID firstAssociationIdFromPojo,
            UUID secondAssociationId,
            UUID secondAssociationIdFromPojo)
            throws RuntimeException {
        checkPermission(httpMethod, uriInfo);
        try {
            return dao.getDeclaredConstructor(constructorTypes).newInstance(
                    currentProjectId,
                    schema).createOrUpdate(
                            pojo,
                            firstAssociationId,
                            firstAssociationIdFromPojo,
                            secondAssociationId,
                            secondAssociationIdFromPojo);
		} catch (InstantiationException   | IllegalAccessException |
				 IllegalArgumentException | InvocationTargetException |
				 NoSuchMethodException    | SecurityException ex) {
			throw new OpenInfraWebException(ex);
		}
    }
}
