package de.btu.openinfra.backend.db.daos.meta;

import java.util.Locale;

import de.btu.openinfra.backend.db.MappingResult;
import de.btu.openinfra.backend.db.OpenInfraSchemas;
import de.btu.openinfra.backend.db.daos.OpenInfraDao;
import de.btu.openinfra.backend.db.jpa.model.meta.SettingKeys;
import de.btu.openinfra.backend.db.pojos.meta.SettingKeysPojo;

public class SettingKeysDao extends OpenInfraDao<SettingKeysPojo, SettingKeys> {

    /**
     * This is the required constructor which calls the super constructor and in
     * turn creates the corresponding entity manager.
     *
     * @param schema           the required schema
     */
    public SettingKeysDao(OpenInfraSchemas schema) {
        super(null, schema, SettingKeys.class);
    }

    @Override
    public SettingKeysPojo mapToPojo(Locale locale, SettingKeys sk) {
        return mapToPojoStatically(sk);
    }

    /**
     * This method implements the method mapToPojo in a static way.
     *
     * @param sk     the model object
     * @return       the POJO object when the model object is not null else null
     */
    public static SettingKeysPojo mapToPojoStatically(SettingKeys sk) {
        if (sk != null) {
            SettingKeysPojo pojo = new SettingKeysPojo(sk);
            pojo.setKey(sk.getKey());
            return pojo;
        } else {
            return null;
        }
    }

    @Override
    public MappingResult<SettingKeys> mapToModel(
            SettingKeysPojo pojo,
            SettingKeys sk) {
        if(pojo != null) {
            mapToModelStatically(pojo, sk);
            return new MappingResult<SettingKeys>(sk.getId(), sk);
        }
        else {
            return null;
        }
    }

    /**
     * This method implements the method mapToModel in a static way.
     * @param pojo the POJO object
     * @param sk the pre initialized model object
     * @return return a corresponding JPA model object or null if the pojo
     * object is null
     */
    public static SettingKeys mapToModelStatically(
            SettingKeysPojo pojo,
            SettingKeys sk) {
        SettingKeys resultSettingKeys = null;
        if(pojo != null) {
            resultSettingKeys = sk;
            if(resultSettingKeys == null) {
                resultSettingKeys = new SettingKeys();
                resultSettingKeys.setId(pojo.getUuid());
            }
            resultSettingKeys.setKey(pojo.getKey());
        }
        return resultSettingKeys;
    }

    /**
     * Creates an empty setting keys pojo.
     * @return an empty setting keys pojo
     */
    public SettingKeysPojo newSettingKeys() {
       return newPojoStatically();
    }

    /**
     * This method implements the method newSettingKeys in a static way.
     * @return an empty setting keys pojo
     */
    public static SettingKeysPojo newPojoStatically() {
        return new SettingKeysPojo();
    }

}
