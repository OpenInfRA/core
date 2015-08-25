package de.btu.openinfra.backend.db.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;

@XmlRootElement
public class TopicInstancePojo extends OpenInfraMetaDataPojo {

	/**
	 * This variable defines the corresponding topic characteristic this topic
	 * instance belongs to.
	 */
	private TopicCharacteristicPojo topicCharacteristic;
	/**
	 * This variable defines the specific values defined by the settings of the
	 * corresponding topic characteristic.
	 */
	private List<AttributeValuePojo> values;
	private JSONObject metaData;

    @Override
    public JSONObject getMetaData() {
        return metaData;
    }

    @Override
    public void setMetaData(JSONObject metaData) {
        this.metaData = metaData;
    }

	public TopicCharacteristicPojo getTopicCharacteristic() {
		return topicCharacteristic;
	}

	public void setTopicCharacteristic(TopicCharacteristicPojo topicCharacteristic) {
		this.topicCharacteristic = topicCharacteristic;
	}

	public List<AttributeValuePojo> getValues() {
		return values;
	}

	public void setValues(List<AttributeValuePojo> values) {
		this.values = values;
	}

}
