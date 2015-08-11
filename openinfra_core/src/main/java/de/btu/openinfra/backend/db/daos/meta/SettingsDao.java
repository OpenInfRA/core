package de.btu.openinfra.backend.db.daos.meta;

import java.util.Locale;

import de.btu.openinfra.backend.db.daos.MappingResult;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.daos.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.meta.Settings;
import de.btu.openinfra.backend.db.pojos.meta.SettingsPojo;

/**
 * This class represents the Settings and is used to access the underlying 
 * layer generated by JPA.
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class SettingsDao
    extends OpenInfraDao<SettingsPojo, Settings> {

    /**
     * This is the required constructor which calls the super constructor and in 
     * turn creates the corresponding entity manager.
     * 
     * @param schema           the required schema
     */
    public SettingsDao(OpenInfraSchemas schema) {
        super(null, schema, Settings.class);
    }
    
    @Override
    public SettingsPojo mapToPojo(Locale locale, Settings s) {
        return mapPojoStatically(s);
    }

    /**
     * This method implements the method mapToPojo in a static way.
     * 
     * @param s     the model object
     * @return       the POJO object when the model object is not null else null
     */
    public static SettingsPojo mapPojoStatically(Settings s) {
        if (s != null) {
            SettingsPojo pojo = new SettingsPojo();
            pojo.setUuid(s.getId());
            pojo.setKey(SettingKeysDao.mapPojoStatically(s.getSettingKey()));
            pojo.setUpdatedOn(s.getUpdatedOn());
            pojo.setValue(s.getValue());
            pojo.setProject(ProjectsDao.mapPojoStatically(s.getProject()));
            return pojo;
        } else {
            return null;
        }
    }

    @Override
    public MappingResult<Settings> mapToModel(SettingsPojo pojo, Settings s) {
        if(pojo != null) {            
            mapToModelStatically(pojo, s);
            return new MappingResult<Settings>(s.getId(), s);
        }
        else {
            return null;
        }
    }
    
    /**
     * This method implements the method mapToModel in a static way.
     * @param pojo the POJO object
     * @param s the pre initialized model object
     * @return return a corresponding JPA model object or null if the pojo
     * object is null
     */
    public static Settings mapToModelStatically(
            SettingsPojo pojo,
            Settings s) {
        Settings resultSettings = null;
        if(pojo != null) {
            resultSettings = s;
            if(resultSettings == null) {
                resultSettings = new Settings();
                resultSettings.setId(pojo.getUuid());
            }
            resultSettings.setSettingKey(
                    SettingKeysDao.mapToModelStatically(pojo.getKey(), null));
            resultSettings.setValue(pojo.getValue());
            resultSettings.setUpdatedOn(pojo.getUpdatedOn());
            resultSettings.setProject(
                    ProjectsDao.mapToModelStatically(pojo.getProject(), null));
        }
        return resultSettings;
    }
    
    /**
     * Creates an empty settings pojo.
     * @return an empty settings pojo
     */
    public SettingsPojo newSettings() {
       return newPojoStatically();
    }

    /**
     * This method implements the method newSettings in a static way.
     * @return an empty settings pojo
     */
    public static SettingsPojo newPojoStatically() {
        SettingsPojo newSettingsPojo = new SettingsPojo();
        newSettingsPojo.setProject(ProjectsDao.newPojoStatically());
        newSettingsPojo.setKey(SettingKeysDao.newPojoStatically());
        return newSettingsPojo;
    }

}
