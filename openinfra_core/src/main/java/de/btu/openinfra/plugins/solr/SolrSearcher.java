package de.btu.openinfra.plugins.solr;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import de.btu.openinfra.plugins.solr.db.pojos.SolrResultPojo;
import de.btu.openinfra.plugins.solr.db.pojos.SolrSearchPojo;
import de.btu.openinfra.plugins.solr.enums.SolrIndexEnum;


/**
 * This class is responsible for mapping a query request to Solr search query.
 * After mapping it will send the query to the Solr server and receive the
 * result. The result will be prepared and send back to the client.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class SolrSearcher {

    private SolrServer solrConnection = null;

    /**
     * Default constructor
     */
    public SolrSearcher() {
        // connect to the Solr server
        solrConnection = new SolrServer();
    }

    /**
     * This will start the search with the passed search term.
     *
     * @param searchPojo The SolrSearchPojo that contains the query.
     */
    public List<SolrResultPojo> search(SolrSearchPojo searchPojo) {

        // create a list for the result objects
        List<SolrResultPojo> resultList = new LinkedList<SolrResultPojo>();

        // create a Solr query object
        SolrQuery query = new SolrQuery();

        // parse the SolrSearchPojo to a Solr syntax query string
        String queryStr = new SolrQueryParser().getSolrSyntaxQuery(searchPojo);

        // add the query string to the query object.
        query.setQuery(queryStr);

        // define the return value
        query.set("wt", "json");

        // enable highlighting
        query.setHighlight(true);

        String hlFields = "*";

        List<String> lst = new SolrQueryParser().extractFields(queryStr);

        // enable highlighting for every field that is requested in the query
        for (int i = 0; i < lst.size(); i++) {
            if (i == 0) {
                hlFields = lst.get(i);
            } else {
                hlFields += "," + lst.get(i);
            }
        }
        query.set("hl.fl", hlFields);

        // set the enclosing highlight tags
        // TODO add this to properties?
        query.setHighlightSimplePre("<b>");
        query.setHighlightSimplePost("</b>");

        try {
            // send the query to the Solr server
            QueryResponse response = solrConnection.getSolr().query(query);

            // get the result list from the Solr server
            SolrDocumentList solrResults = response.getResults();

            // get the highlighting for the request
            Map<String, Map<String, List<String>>> hl =
                    response.getHighlighting();

            // run through the results and add them to a list
            for (int i = 0; i < solrResults.size(); ++i) {
                SolrResultPojo rP = new SolrResultPojo();

                // save the topic instance id from the result object
                rP.setTopicInstanceId(UUID.fromString(
                        solrResults.get(i).getFieldValue(
                                SolrIndexEnum.TOPIC_INSTANCE_ID.getString())
                                .toString()));

                // save the topic characteristic id from the result object
                rP.setTopicCharacteristicId(UUID.fromString(
                        solrResults.get(i).getFieldValue(
                                SolrIndexEnum.TOPIC_CHARACTERISTIC_ID.getString())
                                .toString()));

                // save the project id from the result object
                rP.setProjectId(UUID.fromString(
                        solrResults.get(i).getFieldValue(
                                SolrIndexEnum.PROJECT_ID.getString())
                                .toString()));


                // remove the default_search field from the highlighting map to
                // avoid double entries
                hl.get(rP.getTopicInstanceId().toString()).remove(
                        SolrIndexEnum.DEFAULT_SEARCH_FIELD.getString());

                // add the highlighted fields
                rP.setHighlight(hl.get(rP.getTopicInstanceId().toString()));

                // add the current result to the result list
                resultList.add(rP);
            }
            return resultList;
        } catch (SolrServerException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // return the highlighted result list
        return null;
    }
}