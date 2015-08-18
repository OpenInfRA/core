package de.btu.openinfra.backend.db.daos.meta;

import java.util.Locale;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.jpa.model.meta.Credentials;
import de.btu.openinfra.backend.db.pojos.meta.CredentialsPojo;

/**
 * This class represents the Credentials and is used to access the underlying
 * layer generated by JPA.
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class CredentialsDao
    extends OpenInfraDao<CredentialsPojo, Credentials>{

    /**
     * This is the required constructor which calls the super constructor and in 
     * turn creates the corresponding entity manager.
     * 
     * @param schema           the required schema
     */
    public CredentialsDao(OpenInfraSchemas schema) {
        super(null, schema, Credentials.class);
    }

    @Override
    public CredentialsPojo mapToPojo(Locale locale, Credentials c) {
        return mapPojoStatically(c);
    }

    /**
     * This method implements the method mapToPojo in a static way.
     * 
     * @param at     the model object
     * @return       the POJO object when the model object is not null else null
     */
   public static CredentialsPojo mapPojoStatically(Credentials c) {
        if (c != null) {
            CredentialsPojo pojo = new CredentialsPojo();
            pojo.setUuid(c.getId());
            pojo.setTrid(c.getXmin());
            pojo.setUsername(c.getUsername());
            pojo.setPassword(c.getPassword());
            return pojo;
        } else {
            return null;
        }
    }

    @Override
    public MappingResult<Credentials> mapToModel(
    		CredentialsPojo pojo, 
    		Credentials cd) {
        if(pojo != null) {            
            mapToModelStatically(pojo, cd);
            return new MappingResult<Credentials>(cd.getId(), cd);
        }
        else {
            return null;
        }
    }
    
    /**
     * This method implements the method mapToModel in a static way.
     * @param pojo the POJO object
     * @param cd the pre initialized model object
     * @return return a corresponding JPA model object or null if the pojo
     * object is null
     */
    public static Credentials mapToModelStatically(
            CredentialsPojo pojo,
            Credentials cd) {
        Credentials resultCredentials = null;
        if(pojo != null) {
            resultCredentials = cd;
            if(resultCredentials == null) {
                resultCredentials = new Credentials();
                resultCredentials.setId(pojo.getUuid());
            }
            resultCredentials.setPassword(pojo.getPassword());
            resultCredentials.setUsername(pojo.getUsername());
        }
        return resultCredentials;
    }
    
    /**
     * Creates an empty credentials pojo.
     * @return an empty credentials pojo
     */
    public CredentialsPojo newCredentials() {
       return newPojoStatically();
    }

    /**
     * This method implements the method newCredentials in a static way.
     * @return an empty credentials pojo
     */
    public static CredentialsPojo newPojoStatically() {
        return new CredentialsPojo();
    }

}
