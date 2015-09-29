package de.btu.openinfra.backend.db.rbac;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.ProjectDao;
import de.btu.openinfra.backend.db.jpa.model.Project;
import de.btu.openinfra.backend.db.pojos.ProjectPojo;

public class ProjectRbac extends 
	OpenInfraRbac<ProjectPojo, Project, ProjectDao> {

	
	public ProjectRbac(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema, ProjectDao.class);
	}

	/**
	 * This is a special method which implements its own permission check.
	 * 
	 * @param locale
	 * @return
	 */
	public List<ProjectPojo> readMainProjects(Locale locale) {
		List<ProjectPojo> list = new ProjectDao(
				null,
				OpenInfraSchemas.META_DATA).readMainProjects(locale);
		
		Iterator<ProjectPojo> it = list.iterator();
		while(it.hasNext()) {
			if(!user.isPermitted(
					"/projects/{id}:get:" + it.next().getUuid())) {
				it.remove();
			}
		}
		return list;		
	}
	
	public long getMainProjectsCount() {
		return readMainProjects(null).size();
	}
	
	public List<ProjectPojo> readSubProjects(
			Locale locale,
			int offset,
			int size) {
		checkPermission();
		return new ProjectDao(
				currentProjectId, schema).readSubProjects(locale, offset, size);
	}
	
	public long getSubProjectsCount() {
		return readSubProjects(null, 0, Integer.MAX_VALUE).size();
	}
	
	public ProjectPojo newSubProject(Locale locale) {
		checkPermission();
		return new ProjectDao(currentProjectId, schema).newSubProject(locale);
	}
	
	public List<ProjectPojo> readParents(Locale locale) {
		checkPermission();
		return new ProjectDao(currentProjectId, schema).readParents(locale);
	}
	
}