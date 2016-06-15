package de.btu.openinfra.backend.db.pojos.solr;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.btu.openinfra.backend.db.pojos.MetaDataPojo;
import de.btu.openinfra.backend.db.pojos.OpenInfraPojo;

/**
 * This POJO is a container for a database result of a Solr query.
 * <br/><br/>
 * Note: The POJOs must extend OpenInfraPojo to be accessible for the primer
 *       class. The UUID and TRID that is provided by the OpenInfraPojo will
 *       never be used.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 */
public class SolrResultDbPojo extends OpenInfraPojo {

    private UUID topicInstanceId;
    private UUID topicCharacteristicId;
    private UUID projectId;
    private MetaDataPojo topicCharacteristicMetaData;
    private MetaDataPojo topicInstanceMetaData;
    private Map<String, List<String>> highlight;

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public UUID getTopicInstanceId() {
        return topicInstanceId;
    }

    public void setTopicInstanceId(UUID topicInstanceId) {
        this.topicInstanceId = topicInstanceId;
    }

    public UUID getTopicCharacteristicId() {
        return topicCharacteristicId;
    }

    public void setTopicCharacteristicId(UUID topicCharacteristicId) {
        this.topicCharacteristicId = topicCharacteristicId;
    }

    public MetaDataPojo getTopicCharacteristicMetaData() {
        return topicCharacteristicMetaData;
    }

    public void setTopicCharacteristicMetaData(
            MetaDataPojo topicCharacteristicMetaData) {
        this.topicCharacteristicMetaData = topicCharacteristicMetaData;
    }

    public MetaDataPojo getTopicInstanceMetaData() {
        return topicInstanceMetaData;
    }

    public void setTopicInstanceMetaData(MetaDataPojo topicInstanceMetaData) {
        this.topicInstanceMetaData = topicInstanceMetaData;
    }

    public Map<String, List<String>> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, List<String>> highlight) {
        this.highlight = highlight;
    }
}
