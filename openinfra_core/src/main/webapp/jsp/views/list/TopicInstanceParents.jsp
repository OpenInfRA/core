<%@page import="de.btu.openinfra.backend.helper.ImgSize"%>
<%@page import="de.btu.openinfra.backend.helper.ImgUrlHelper"%>
<%@page import="java.util.UUID"%>
<%@page import="de.btu.openinfra.backend.db.daos.AttributeTypeDao"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="../../snippets/Head.jsp" %>
	<title>OpenInfRA <fmt:message key="topicinstances.label"/></title>
</head>
<body>
	<%@ include file="../../snippets/Menu.jsp" %>
	
	
	<div id="parents">
		<c:choose>
		<c:when test="${fn:length(it) gt 0}">
		<div class="panel panel-default">
				<div class="panel-heading">
			    	<h4 class="panel-title">		                    	
			        	<fmt:message key="parents.label"/>
			        </h4>
			   	</div>
			   	<div class="panel-collapse collapse in">
			   		<div class="panel-body">
			   			<div>
	<c:forEach items="${it}" var="pojo">
	
	<div class="panel panel-default">
		<div class="panel-heading">
			<c:set var="localizedStrings" value="${pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings}"/>
			<%@ include file="../../snippets/LocalizedStrings.jsp" %>
			${pojo.relationshipType.description.names.localizedStrings[0].characterString}
		</div>
		<c:set var="settingCount" value="${fn:length(pojo.associatedInstance.topicCharacteristic.metaData)}"/>
		<c:if test="${settingCount == 0}">
			<div class="panel-body">
	    		<p><fmt:message key="nosettings.label"/></p>
	  		</div>
	  	</c:if>
		<table class="table">
			<thead>
				<tr>
					<c:set var="columnName" value=""/>
					<c:set var="metaData" value="${pojo.associatedInstance.topicCharacteristic.metaData}"/>
					<c:set var="columns" value="${metaData.list_view_columns}"/>
					<c:forEach items="${columns}" var="setting">
						<th style="width: ${100/settingCount}%">
						<%
							pageContext.setAttribute(
																"columnName",
																new AttributeTypeDao(
																UUID.fromString(pageContext.getAttribute("currentProject").toString()),
																OpenInfraSchemas.PROJECTS).read(
																		PtLocaleDao.forLanguageTag(session.getAttribute("language").toString()), 
																		UUID.fromString(pageContext.getAttribute("setting").toString())),
																PageContext.PAGE_SCOPE);
						%>
								<c:choose>
									<c:when test="${columnName.names.localizedStrings[0] != null}">
										${columnName.names.localizedStrings[0].characterString}
										<c:if test="${columnName.domain != null}">
											(Domain)
										</c:if>
									</c:when>
									<c:otherwise>
										-
									</c:otherwise>
								</c:choose>
							
						</th>
					</c:forEach>
					<c:if test="${settingCount == 0}">
						<th>
							Name
						</th>
						<th>
							UUID
						</th>
					</c:if>
				</tr>
			</thead>

				<tr>
					<c:forEach items="${pojo.associatedInstance.topicCharacteristic.metaData}" var="setting">
						<c:set var="found" value="false"/>
						<c:forEach items="${pojo.associatedInstance.values}" var="value">
							<c:if test="${setting == value.attributeTypeId}">
								<c:set var="found" value="true"/>
								<td>
									<c:if test="${value.attributeValueDomain != null}">
										<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}&geomType=${geomType}">
											<c:forEach items="${value.attributeValueDomain.domain.names.localizedStrings}" var="item">
				  								<c:choose>
					  								<c:when test="${fn:endsWith(item.characterString, '.JPG') || fn:endsWith(item.characterString, '.jpg')}">
					  									<c:set var="currentImage" value="${item.characterString}"/>
							    						<img alt="<fmt:message key="noimage.label"/>: ${item.characterString}" 
							    									src="<%= ImgUrlHelper.getImgUrl(pageContext.getAttribute("currentImage").toString(), ImgSize.SMALL) %>"/>	
													</c:when>
													<c:otherwise>
														${item.characterString}
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</a>
									</c:if>
									<c:if test="${value.attributeValueValue != null}">
										<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}&geomType=${geomType}">
											<c:forEach items="${value.attributeValueValue.value.localizedStrings}" var="item">
				  								<c:choose>
					  								<c:when test="${fn:endsWith(item.characterString, '.JPG') || fn:endsWith(item.characterString, '.jpg')}">
							    						<c:set var="currentImage" value="${item.characterString}"/>
							    						<img alt="<fmt:message key="noimage.label"/>: ${item.characterString}" 
							    									src="<%= ImgUrlHelper.getImgUrl(pageContext.getAttribute("currentImage").toString(), ImgSize.SMALL) %>"/>	
													</c:when>
													<c:otherwise>
														${item.characterString}
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</a>
									</c:if>
								</td> 
							</c:if>
						</c:forEach>
						<c:if test="${found == 'false'}">
							<td>
								-
							</td>
						</c:if>
					</c:forEach>
					<c:if test="${settingCount == 0}">
						<td>
							<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}">
								<c:set var="localizedStrings" value="${pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings}"/>
								<%@ include file="../../snippets/LocalizedStrings.jsp" %>
							</a>
						</td>
						<td>
							<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}">
								${pojo.associatedInstance.uuid}
							</a>
						</td>
					</c:if>
				</tr>



		</table> 
	</div>
	
	
	</c:forEach>
			   			</div>
			   		</div>
			   	</div>
			   	</div>
			</c:when>
			<c:otherwise>
				<fmt:message key="noassignedinstances.label"/>
			</c:otherwise>
		</c:choose>	
	</div>

	
</body>
</html>