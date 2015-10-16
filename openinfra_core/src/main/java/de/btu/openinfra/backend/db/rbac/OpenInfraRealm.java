package de.btu.openinfra.backend.db.rbac;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import de.btu.openinfra.backend.db.daos.rbac.SubjectDao;
import de.btu.openinfra.backend.db.daos.rbac.SubjectObjectDao;
import de.btu.openinfra.backend.db.jpa.model.rbac.RolePermission;
import de.btu.openinfra.backend.db.jpa.model.rbac.Subject;
import de.btu.openinfra.backend.db.jpa.model.rbac.SubjectProject;
import de.btu.openinfra.backend.db.jpa.model.rbac.SubjectRole;
import de.btu.openinfra.backend.db.pojos.rbac.SubjectObjectPojo;

/**
 * This is the OpenInfraRealm which is used to retrieve user-specific 
 * information from database. The idea behind this implementation was: 
 * 'keep it stupid and simple' and it's closely related to the implementation
 * of the origin jdbcRealm.
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraRealm extends AuthorizingRealm {
	
	@Override
	protected SaltedAuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken upt = (UsernamePasswordToken)token;
		SubjectDao dao = new SubjectDao();
		Subject s = dao.readModel(upt.getUsername());
		dao.updateLoginTime(s);
		
		// Grant only access to users which have the status 1
		if(s.getStatus() == 1) {
			return new SimpleAuthenticationInfo(
					new SimplePrincipalCollection(
							upt.getUsername(),
							OpenInfraRealmNames.LOGIN.name()), 
							s.getPassword(),
							new SimpleByteSource(s.getSalt().toString()));
		} else {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		
		// Retrieve the login from principals (there might be multiple)
		List<String> login = new LinkedList<String>();
		for(Object o : principals.fromRealm(OpenInfraRealmNames.LOGIN.name())) {
			login.add(o.toString());
		}
		
		// Currently, we assume that there is only one entry in the principal 
		// collection with the name: 'OpenInfraRealmNames.LOGIN.name()'. This
		// login name is defined in the 'doGetAuthenticationInfo' method above.
		// Thus, we use always the very first entry. However, this might change
		// in the future and should be changed.
        Subject s = new SubjectDao().readModel(login.get(0));
        
        // 1. Get all default/ordinary roles and permissions
        Set<String> roleNames = new HashSet<String>();
        Set<String> permissions = new HashSet<String>();
        
        for(SubjectRole sr : s.getSubjectRoles()) {
        	roleNames.add(sr.getRoleBean().getName());
        	for(RolePermission rp : sr.getRoleBean().getRolePermissions()) {
        		permissions.add(rp.getPermissionBean().getPermission());
        	}
        }
        
        // 2. Generate project related permissions based on project related
        // roles.
        for(SubjectProject sp : s.getSubjectProjects()) {
        	if(sp.getProjectRelatedRoleBean().getName()
        			.equalsIgnoreCase("projectguest")) {
        		permissions.add("/projects/{id}:r:" + sp.getProjectId());
        		// get information about the object access
        		List<SubjectObjectPojo> soList = new SubjectObjectDao().read(
        				sp.getSubjectBean().getId(), sp.getProjectId());
        		if(soList.size() == 0) {
        			permissions.add("/projects/" + sp.getProjectId() + 
        					"/topiccharacteristics/{id}:r:*");
        		} else {
            		for(SubjectObjectPojo so : soList) {
            			permissions.add("/projects/" + sp.getProjectId() + 
            					"/topiccharacteristics/{id}:r:" + 
            					so.getObjectId());            			
            		} // end for
        		} // end if else
        	}
        	if(sp.getProjectRelatedRoleBean().getName()
        			.equalsIgnoreCase("projecteditor")) {
        		permissions.add("/projects/{id}:r,w:" + sp.getProjectId());
        		// get information about the object access
        		List<SubjectObjectPojo> soList = new SubjectObjectDao().read(
        				sp.getSubjectBean().getId(), sp.getProjectId());
        		if(soList.size() == 0) {
        			permissions.add("/projects/" + sp.getProjectId() + 
        					"/topiccharacteristics/{id}:r,w:*");
        		} else {
            		for(SubjectObjectPojo so : soList) {
            			String write = (so.isWriteObject()) ? "" : ",r";
            			permissions.add("/projects/" + sp.getProjectId() + 
            					"/topiccharacteristics/{id}:r" + write + ":" + 
            					so.getObjectId());            			
            		} // end for
        		} // end if else        		
        	}
        	if(sp.getProjectRelatedRoleBean().getName()
        			.equalsIgnoreCase("projectadmin")) {
        		permissions.add("/projects/" + sp.getProjectId() + ":r");
        		//TODO implement this - change of subjects (users) related
        		// to the project
        	}
        }
        		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
	}
	
	/**
	 * Creates an encrypted password.
	 * <a href="http://shiro.apache.org/realm.html"/>
	 * @param password the password to encrypt
	 * @return the encrypted password
	 */
	public static String encrypt(String plainTextPassword, UUID salt) {
		//Hash the plain-text password with the salt and multiple
		//iterations and then Base64-encode the value (requires less 
		//space than Hex):
		// TODO: the value 1024 is defined in the shiro.ini and should match
		// the same value 'credentialsMatcher.hashIterations = 1024'
		return new Sha256Hash(
				plainTextPassword, 
				salt.toString(), 
				1024).toBase64();		
	}

}
