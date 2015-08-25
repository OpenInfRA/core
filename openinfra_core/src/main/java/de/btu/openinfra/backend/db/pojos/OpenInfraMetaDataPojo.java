package de.btu.openinfra.backend.db.pojos;

import org.json.simple.JSONObject;

public abstract class OpenInfraMetaDataPojo extends OpenInfraPojo{

    /**
     * This variable stands for the meta data of the data object stored
     * in PostgreSQL in the meta data table.
     */
    private JSONObject metaData;

    public JSONObject getMetaData() {
        return metaData;
    }

    public void setMetaData(JSONObject metaData) {
        this.metaData = metaData;
    }
}
