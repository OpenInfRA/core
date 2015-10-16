package de.btu.openinfra.backend.db.daos.rbac;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityTransaction;

import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.OpenInfraTime;
import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.jpa.model.rbac.Subject;
import de.btu.openinfra.backend.db.jpa.model.rbac.SubjectProject;
import de.btu.openinfra.backend.db.jpa.model.rbac.SubjectRole;
import de.btu.openinfra.backend.db.pojos.rbac.RolePojo;
import de.btu.openinfra.backend.db.pojos.rbac.SubjectPojo;
import de.btu.openinfra.backend.db.pojos.rbac.SubjectProjectPojo;
import de.btu.openinfra.backend.db.rbac.OpenInfraRealm;
import de.btu.openinfra.backend.exception.OpenInfraEntityException;
import de.btu.openinfra.backend.exception.OpenInfraExceptionTypes;

/**
 * This is the DAO class for users. In this case the map to model method skips
 * some values of the model class. This includes the salt string and the 
 * password sting. These values should not be transferred to the client.
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class SubjectDao extends OpenInfraDao<SubjectPojo, Subject> {

	public SubjectDao() {
		super(null, OpenInfraSchemas.RBAC, Subject.class);
	}
	
	public SubjectDao(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema, Subject.class);
	}
	
	/**
	 * This method reads a RBAC model object by login from database. This is
	 * required for Apache Shiro login. 
	 * @param login the login name
	 * @return the subject (user) as model object
	 */
	public Subject readModel(String login) {
		try {
			return em.createNamedQuery(
					"Subject.findByLogin",
					Subject.class).setParameter(
							"login",
							login).getSingleResult();			
		} catch(Exception ex) {
			throw new OpenInfraEntityException(
					OpenInfraExceptionTypes.ENTITY_NOT_FOUND);
		}
	}
	
	public SubjectPojo read(String login) {
		return mapToPojo(null, readModel(login));
	}

	@Override
	public SubjectPojo mapToPojo(Locale locale, Subject modelObject) {
		return mapToPojoStatically(locale, modelObject);
	}
	
	public static SubjectPojo mapToPojoStatically(
			Locale locale, Subject modelObject) {
		SubjectPojo pojo = new SubjectPojo(modelObject);
		pojo.setCreatedOn(OpenInfraTime.format(modelObject.getCreatedOn()));		
		pojo.setDefaultLanguage(modelObject.getDefaultLanguage());
		pojo.setDescription(modelObject.getDescription());
		pojo.setLastLoginOn(OpenInfraTime.format(modelObject.getLastLoginOn()));
		pojo.setLogin(modelObject.getLogin());
		pojo.setMail(pojo.getMail());
		pojo.setName(modelObject.getName());
		pojo.setPasswordCreatedOn(
				OpenInfraTime.format(modelObject.getPasswordCreatedOn()));
		// status: -1 blocked, 0 inactive, 1 active
		pojo.setStatus(modelObject.getStatus());
		pojo.setUpdatedOn(OpenInfraTime.format(modelObject.getUpdatedOn()));
		pojo.setWebApp(modelObject.getWebapp());
		
		List<SubjectProjectPojo> spList = new LinkedList<SubjectProjectPojo>();
		for(SubjectProject sp : modelObject.getSubjectProjects()) {
			spList.add(SubjectProjectDao.mapToPojoStatically(locale, sp));
		}
		pojo.setProjects(spList);
		
		List<RolePojo> roles = new LinkedList<RolePojo>();
		RoleDao dao = new RoleDao();		
		for(SubjectRole ur : modelObject.getSubjectRoles()) {
			roles.add(dao.mapToPojo(locale, ur.getRoleBean()));
		}
		
		pojo.setRoles(roles);
		
		return pojo;		
	}
	
	/**
	 * This method updates the login time of the current user.
	 */
	public void updateLoginTime(Subject modelObject) {
		modelObject.setLastLoginOn(OpenInfraTime.now());
		EntityTransaction et = em.getTransaction();
		try {
			et.begin();
			em.merge(modelObject);
			et.commit();
		} catch(RuntimeException ex) {
			if(et != null && et.isActive()) {
				et.rollback();
			} // end if
			throw ex;
		} // end try catch
	}

	@Override
	public MappingResult<Subject> mapToModel(
			SubjectPojo pojoObject, Subject modelObject) {
		// Please remember that the model object might be a new instance or an
		// already existing which was retrieved a few milliseconds before from 
		// database.
		if(pojoObject.getDefaultLanguage() != null) {
			modelObject.setDefaultLanguage(pojoObject.getDefaultLanguage());
		} else {
			modelObject.setDefaultLanguage(
					OpenInfraProperties.DEFAULT_LANGUAGE.getLanguage() + "-" +
					OpenInfraProperties.DEFAULT_LANGUAGE.getCountry());
		}
		modelObject.setDescription(pojoObject.getDescription());
		modelObject.setLogin(pojoObject.getLogin());
		modelObject.setMail(pojoObject.getMail());
		modelObject.setName(pojoObject.getName());
		// Set the password when the password is initially null and when a new 
		// password was send by the client.
		// TODO throw Exception when password in passwordblacklist
		if(modelObject.getPasswordCreatedOn() == null || 
				(pojoObject.getPassword() != null && 
				!modelObject.getPassword().equalsIgnoreCase(
						OpenInfraRealm.encrypt(
								pojoObject.getPassword(), 
								modelObject.getSalt())))) {
			modelObject.setPasswordCreatedOn(OpenInfraTime.now());
			modelObject.setSalt(UUID.randomUUID());
			modelObject.setPassword(
					OpenInfraRealm.encrypt(
							pojoObject.getPassword(), modelObject.getSalt()));
		}
		
		modelObject.setWebapp(pojoObject.getWebApp());
		modelObject.setUpdatedOn(OpenInfraTime.now());
		// status: -1 blocked, 0 inactive, 1 active
		modelObject.setStatus(pojoObject.getStatus());
		
		// Set the following parameters when the subject is created
		if(modelObject.getCreatedOn() == null) {
			modelObject.setCreatedOn(OpenInfraTime.now());
		}
		return new MappingResult<Subject>(modelObject.getId(), modelObject);
	}

}