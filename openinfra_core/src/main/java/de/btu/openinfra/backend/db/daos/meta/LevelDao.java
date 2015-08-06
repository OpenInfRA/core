package de.btu.openinfra.backend.db.daos.meta;

import java.util.Locale;

import de.btu.openinfra.backend.db.daos.MappingResult;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.daos.OpenInfraSchemas;
import de.btu.openinfra.backend.db.jpa.model.meta.Level;
import de.btu.openinfra.backend.db.pojos.meta.LevelPojo;

/**
 * This class represents the log level and is used to access the underlying
 * layer generated by JPA.
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class LevelDao
    extends OpenInfraDao<LevelPojo, Level> {

    /**
     * This is the required constructor which calls the super constructor and in 
     * turn creates the corresponding entity manager.
     * 
     * @param schema           the required schema
     */
    public LevelDao(OpenInfraSchemas schema) {
        super(null, schema, Level.class);
    }

    @Override
    public LevelPojo mapToPojo(Locale locale, Level l) {
        return null;
    }

    /**
     * This method implements the method mapToPojo in a static way.
     * 
     * @param at     the model object
     * @return       the POJO object when the model object is not null else null
     */
    public static LevelPojo mapPojoStatically(Level l) {
        if(l != null) {
            LevelPojo pojo = new LevelPojo();
            pojo.setUuid(l.getId());
            pojo.setLevel(l.getLevel());
            return pojo;
        } else {
            return null;
        }
    }
    
    @Override
    public MappingResult<Level> mapToModel(LevelPojo pojo, Level lv) {
        if(pojo != null) {            
            mapToModelStatically(pojo, lv);
            return new MappingResult<Level>(lv.getId(), lv);
        }
        else {
            return null;
        }
    }
    
    /**
     * This method implements the method mapToModel in a static way.
     * @param pojo the POJO object
     * @param level the pre initialized model object
     * @return return a corresponding JPA model object or null if the pojo
     * object is null
     */
    public static Level mapToModelStatically(LevelPojo pojo, Level level) {
        Level resultLevel = null;
        if(pojo != null) {
            resultLevel = level;
            if(resultLevel == null) {
                resultLevel = new Level();
                resultLevel.setId(pojo.getUuid());
            }
            resultLevel.setLevel(pojo.getLevel());
        }
        return resultLevel;
    }

}
