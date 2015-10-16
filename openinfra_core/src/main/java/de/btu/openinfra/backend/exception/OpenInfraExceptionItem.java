package de.btu.openinfra.backend.exception;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is an exception item which is send to the client when something goes
 * wrong.
 * 
 * http://slackspace.de/articles/jersey-how-to-provide-meaningful-exception-messages/
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@XmlRootElement
public class OpenInfraExceptionItem {
	
    private Integer errorCode;
    private String name;
    private String description;
    private String type;
    
    public OpenInfraExceptionItem() {}
    
    public OpenInfraExceptionItem(
    		Integer errorCode, 
    		String name,
    		String type,
    		String description) {
    	this.errorCode = errorCode;
    	this.name = name;
    	this.type = type;
    	this.description = description;
    }
    
	public Integer getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
}
