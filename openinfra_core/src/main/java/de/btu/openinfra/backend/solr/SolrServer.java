package de.btu.openinfra.backend.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteSolrException;

import de.btu.openinfra.backend.OpenInfraProperties;
import de.btu.openinfra.backend.OpenInfraPropertyKeys;
import de.btu.openinfra.backend.exception.OpenInfraWebException;

/**
 * This class creates a connection to the Solr server. It will be able to work
 * with threads.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class SolrServer extends Thread {

    protected Thread thread;
    private String url;
    private SolrClient solr;
    private boolean alive = false;

    /**
     *  Default constructor that uses the Solr connection URL from the
     *  OpenInfra.properties file
     */
    protected SolrServer() {
        this(OpenInfraProperties.getProperty(
                OpenInfraPropertyKeys.SOLR_URL.getKey())
               + "/" +
               OpenInfraProperties.getProperty(
                       OpenInfraPropertyKeys.SOLR_CORE.getKey())
               );
    }

    /**
     *  Constructor that allows to override the Solr connection URL from the
     *  OpenInfra.properties file
     */
    protected SolrServer(String URL) {
        setUrl(URL);
        // initialize the server connection
        init();
    }

    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    protected SolrClient getSolr() {
        return solr;
    }

    private void setSolr(SolrClient solr) {
        this.solr = solr;
    }

    protected boolean getAlive() {
        return alive;
    }

    private void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * This method connects to the the Solr server and test the connection.
     *
     * @return boolean true if the connection was successful else false
     * @throws OpenInfraWebException if an error occurs while pinging the server
     */
    private void init() {
        try {
            // connects to the Solr server
            setSolr(new HttpSolrClient(getUrl()));

            // test the connection
            getSolr().ping();

            // set the alive flag for the server connection to true
            alive = true;
        } catch (SolrServerException | IOException | RemoteSolrException e) {
            // set the alive flag for the server connection to false
            alive = false;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        getSolr().close();
        super.finalize();
    }
}
