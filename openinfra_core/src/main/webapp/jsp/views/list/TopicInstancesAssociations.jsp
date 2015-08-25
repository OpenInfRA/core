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
	
	<div class="panel-group" id="accordion">
		<c:choose>
			<c:when test="${fn:length(it) gt 0}">
				<!-- 1. Get all available topic characteristics and store them into an array -->
				<c:set var="tcs" value=""/>
				<c:forEach items="${it}" var="pojo">
					<c:set var="tc" value="${pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings[0].characterString}"/>
					<c:if test="${not fn:contains(tcs, tc)}">
						<c:choose>
							<c:when test="${empty tcs}">
								<c:set var="tcs" value="${tc}"/>
							</c:when>
							<c:otherwise>
								<c:set var="tcs" value="${tcs},${tc}"/>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach>
				<!-- 2. Iterate over all stored topic characteristics -->
				<c:forTokens items="${tcs}" delims="," var="tc" varStatus="theCount">
					<!-- Count TopicCharacteristics -->
					<c:set var="tcCount" value="0"/>
					<c:set var="ass" value=""/>
					<c:forEach items="${it}" var="pojo">
						<c:if test="${tc == pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings[0].characterString}">
							<c:set var="tcCount" value="${tcCount+1}"/>
							<c:set var="ass" value="${pojo.relationshipType.description.names.localizedStrings[0].characterString}"/>
						</c:if>
					</c:forEach>
					<div class="panel panel-default">
		            	<div class="panel-heading">
		                	<h4 class="panel-title">
		                		
		                    	<a data-toggle="collapse" data-parent="#accordion" href="#collapse${tc}">
		                         ${ass}	${tc} <span class="badge">${tcCount}</span>
		                        </a>
		                    </h4>
		                </div>
		                <!-- Open the first -->
		                <c:set var="in" value=""/>
		                <c:if test="${theCount.count == 1}">
		                	<c:set var="in" value="in" />
		                </c:if>
		                <!-- Create the collapsible panel -->
						<div id="collapse${tc}" class="panel-collapse collapse ${in}">
							<div class="panel-body">
								<table class="table">
									<!-- Only generate the header once -->
									<c:set var="head" value="false"/>
									<c:forEach items="${it}" var="pojo">
										<c:if test="${tc == pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings[0].characterString && head != 'true'}">
											<c:set var="head" value="true"/>
											<thead>
												<tr>
													<c:set var="settingCount" value="${fn:length(pojo.associatedInstance.topicCharacteristic.metaData)}"/>
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
										</c:if>
									</c:forEach>
									<c:forEach items="${it}" var="pojo">
										<c:if test="${tc == pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings[0].characterString}">
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
													    									src="<%= ImgUrlHelper.getImgUrl(pageContext.getAttribute("currentImage").toString(), ImgSize.MINI) %>"/>														
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
													    									src="<%= ImgUrlHelper.getImgUrl(pageContext.getAttribute("currentImage").toString(), ImgSize.MINI) %>"/>	
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
														<c:forEach items="${pojo.associatedInstance.topicCharacteristic.descriptions.localizedStrings}" var="item">
															<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}">
																${item.characterString}
															</a>
														</c:forEach>
													</td>
													<td>
														<a href="../../topicinstances/${pojo.associatedInstance.uuid}/topic?language=${language}">
															${pojo.associatedInstance.uuid}
														</a>
													</td>
												</c:if>
											</tr>
										</c:if>
									</c:forEach>
								</table>
							</div>
						</div>
					</div>			
				</c:forTokens>
			</c:when>
			<c:otherwise>
				<fmt:message key="noassignedinstances.label"/>
			</c:otherwise>
		</c:choose>		
	</div>
	
</body>
</html>