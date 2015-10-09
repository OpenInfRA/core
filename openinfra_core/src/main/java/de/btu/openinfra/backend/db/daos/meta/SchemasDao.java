package de.btu.openinfra.backend.db.daos.meta;

import java.util.Locale;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.jpa.model.meta.Schemas;
import de.btu.openinfra.backend.db.pojos.meta.SchemasPojo;

/**
 * This class represents the Schemas and is used to access the underlying layer
 * generated by JPA.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class SchemasDao
    extends OpenInfraDao<SchemasPojo, Schemas> {

    /**
     * This is the required constructor which calls the super constructor and in
     * turn creates the corresponding entity manager.
     *
     * @param schema           the required schema
     */
    public SchemasDao(OpenInfraSchemas schema) {
        super(null, schema, Schemas.class);
    }

    @Override
    public SchemasPojo mapToPojo(Locale locale, Schemas s) {
        return mapToPojoStatically(s);
    }

    /**
     * This method implements the method mapToPojo in a static way.
     *
     * @param at     the model object
     * @return       the POJO object when the model object is not null else null
     */
    public static SchemasPojo mapToPojoStatically(Schemas s) {
        if (s != null) {
            SchemasPojo pojo = new SchemasPojo(s);
            pojo.setSchema(s.getSchema());
            return pojo;
        } else {
            return null;
        }
    }

    @Override
    public MappingResult<Schemas> mapToModel(SchemasPojo pojo, Schemas s) {
        if(pojo != null) {
            mapToModelStatically(pojo, s);
            return new MappingResult<Schemas>(s.getId(), s);
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
    public static Schemas mapToModelStatically(SchemasPojo pojo, Schemas s) {
        Schemas resultSchemas = null;
        if(pojo != null) {
            resultSchemas = s;
            if(resultSchemas == null) {
                resultSchemas = new Schemas();
                resultSchemas.setId(pojo.getUuid());
            }
            resultSchemas.setSchema(pojo.getSchema());
        }
        return resultSchemas;
    }

    /**
     * Creates an empty schemas pojo.
     * @return an empty schemas pojo
     */
    public SchemasPojo newSchemas() {
       return newPojoStatically();
    }

    /**
     * This method implements the method newPorts in a static way.
     * @return an empty schemas pojo
     */
    public static SchemasPojo newPojoStatically() {
        return new SchemasPojo();
    }

}
