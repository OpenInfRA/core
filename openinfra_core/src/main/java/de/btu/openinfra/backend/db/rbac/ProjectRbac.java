package de.btu.openinfra.backend.db.rbac;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.shiro.authz.UnauthorizedException;

import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.ProjectDao;
import de.btu.openinfra.backend.db.jpa.model.Project;
import de.btu.openinfra.backend.db.pojos.ProjectPojo;

public class ProjectRbac extends 
	OpenInfraRbac<ProjectPojo, Project, ProjectDao> {

	
	public ProjectRbac(UUID currentProjectId, OpenInfraSchemas schema) {
		super(currentProjectId, schema, ProjectDao.class);
	}

	public List<ProjectPojo> readMainProjects(Locale locale) 
			throws UnauthorizedException {
		if(user.isPermitted("/projects:get")) {
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
		} else {
			throw new UnauthorizedException();
		}
	}

}
