<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="../snippets/Head.jsp" %>
	<title>OpenInfRA <fmt:message key="system.label"/></title>
</head>
<body>
	<%@ include file="../snippets/Menu.jsp" %>
	
	<h2>File upload:</h2>
	
	<form action="./files/upload" method="post" enctype="multipart/form-data">
		<p>Select a file : <input type="file" name="file" size="50" /></p>
		<input type="submit" value="Upload It" />
	</form>
	
</body>
</html>