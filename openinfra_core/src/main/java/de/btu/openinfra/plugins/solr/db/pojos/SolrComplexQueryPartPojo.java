package de.btu.openinfra.plugins.solr.db.pojos;

import javax.xml.bind.annotation.XmlRootElement;

import de.btu.openinfra.plugins.solr.enums.LogicOperatorEnum;
import de.btu.openinfra.plugins.solr.enums.MandatoryEnum;
import de.btu.openinfra.plugins.solr.enums.RelationalOperatorEnum;

/**
 * This POJO is a container for a part of the complex search query that is
 * generated by the extended search.
 *
 * - mandatory: determine if a term should be part of the result or not
 * - attributeType: the attribute type
 * - relationanlOperator: the operator that defines the relation between the
 *                        attribute type and the attribute value
 * - attributeValue: the attribute value
 * - logicOperator: the connection to the previous complex query part
 * - relevance: the relevance of the complex query part in relation to other
 *              parts
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@XmlRootElement
public class SolrComplexQueryPartPojo {

    private MandatoryEnum mandatory;
    private String attributeType;
    private RelationalOperatorEnum relationalOperator;
    private String attributeValue;
    private String tillAttributeValue;
    private LogicOperatorEnum logicOperator;
    private float relevance;
//    private float fuzziness;

    public MandatoryEnum getMandatory() {
        return mandatory;
    }

    public void setMandatory(MandatoryEnum mandatory) {
        this.mandatory = mandatory;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public RelationalOperatorEnum getRelationalOperator() {
        return relationalOperator;
    }

    public void setRelationalOperator(RelationalOperatorEnum relationalOperator) {
        this.relationalOperator = relationalOperator;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getTillAttributeValue() {
        return tillAttributeValue;
    }

    public void setTillAttributeValue(String tillAttributeValue) {
        this.tillAttributeValue = tillAttributeValue;
    }

    public LogicOperatorEnum getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(LogicOperatorEnum logicOperator) {
        this.logicOperator = logicOperator;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }
}
