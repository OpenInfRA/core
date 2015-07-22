<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="../snippets/Head.jsp" %>
	<meta http-equiv="X-UA-Compatible" content="chrome=1"></meta>
	<title>OpenInfRA <fmt:message key="3dwebgis.label"/></title>
	
	<link rel="stylesheet" type="text/css" href="http://www.x3dom.org/x3dom/release/x3dom.css"></link>
   	<link rel="stylesheet" type="text/css" href="${contextPath}/3d/style.css"></link>
</head>
<body>
	<%@ include file="../snippets/Menu.jsp" %>
	<div id="3dwebgis">
	</div>

	<div id="content">
		<div id="navigation">
			<p class="ueberschrift">Navigation</p>
			<p>Navigationsmodus �ndern:</p>
			<form id="navmodus">
				<!--Current mode: <span id="nav_mode_info"></span><br>-->
				<input type="radio" name="nav_mode" value="examine" onclick="radio(this)" checked="checked"></input> Examine<br>
				<input type="radio" name="nav_mode" value="fly" onclick="radio(this)"></input> Fly<br>
				<input type="radio" name="nav_mode" value="game" onclick="radio(this)"></input> Game<br>
				<input type="radio" name="nav_mode" value="helicopter" onclick="radio(this)"></input> Helicopter<br>
				<input type="radio" name="nav_mode" value="lookAt" onclick="radio(this)"></input> LookAt<br>
				<input type="radio" name="nav_mode" value="lookAround" onclick="radio(this)"></input> LookAround<br>
				<input type="radio" name="nav_mode" value="walk" onclick="radio(this)"></input> Walk<br>
			</form>
				<hr></hr>
			<form id="navbuttons">
				<input type="button" value="Alles anzeigen" onclick="$element.runtime.showAll();return false;" title="Shortcut: a"></input><br>
				<input type="button" value="Konsole �ffnen" onclick="toggleDebug();" title="Shortcut: d"></input><br>
				<input type="button" value="Statistik anzeigen" onclick="toggleStats();" title="Shortcut: Leertaste"></input><br>
				<input type="button" value="Rendermodus �ndern" onclick="$element.runtime.togglePoints();return false;" title="Shortcut: m"></input><br>
			</form>
			<hr></hr>
			<ul>
				<li>Mausrad oder rechte Maustaste oder alt+LM: Zoom</li>
				<li>Linke Maustaste: Um Ursprung schwenken</li>
				<li>Strg+LM: Verschieben</li>
				<li>a: alles anzeigen</li>
				<li>d: Konsole �ffnen</li>
				<li>r: Kamera zuruecksetzen</li>
				<li>m: Rendermodus �ndern</li>
				<li>Doppelklick LM: Ursprung �ndern</li>
			</ul>
		</div>
		
		<div id ="center">
			<table><tr>
				<td><p class="ueberschrift">Projektauswahl:</p></td>
				<td><form name="auswahl">
						<select name="projekte" onchange="changeSelect(this)" id="projekte">
							
						</select>
					</form>
				</td>
				<!-- versteckter Tooltip -->
				<td><p id="tooltip" style="display:none;">text</p></td></tr>
			</table>
			<!-- X3D-Element -->
			<X3D xmlns="http://www.web3d.org/specifications/x3d-namespace" width="800px" height="600px" id="x3d" onmousemove="saveMouseCoords();">
				
				<!--button id="toggler" onclick="toggle(this);return false;">Zoom</button!-->
				<!-- <Param name="showProgress" value="false" ></Param>		Theoretisch m�glich, hat aber nie funktioniert!-->
				<Scene id="s">
					<Viewpoint id="view" position='-800 -1000 3000' />
				</Scene>
			</X3D>
			<br></br>
			<span id="w3ds_status"></span>
		</div>
		
		<div id="interaktion">
			<p class="ueberschrift">Interaktionsm�glichkeiten</p>
			<!-- Container f�r Modellauswahl -->
			<div id="modelle" style="max-height:700px;overflow: auto;"></div>
			<br></br>
			<div>
				<p>Modell ausw�hlen, dessen Farbe ge�ndert werden soll:</p>
				<form>
					<!-- Platzhalter f�r Pulldown -->
					<select id="modellAuswahl"></select>
				</form>
				<!-- Farbauswahl �ber JsColor -->
				<input id="farbe" class="color{valueElement:'myValue',hash:true}" onclick="changeColor(this)" size="3"></input>
				<input id="myValue" size="10px" onchange="changeColor(document.getElementById('farbe'))"></input>
				<br></br>
				<p>Transparenz (%)</p>
				<input type="button" value="-" onclick="transMinus()"></input>
				<input id="transparenz" size="3" type="text" value="0" disabled="disabled" style="text-align:center;"></input>
				<input type="button" value="+" onclick="transPlus()"></input>
			</div>
		</div>

	</div>
	<script type="text/javascript">
   		// TODO: Only a workaround: Globale to make 3D client access context path
   	 	window.contextPath = "${contextPath}";
   	</script>
   	<script type="text/javascript" src="http://www.x3dom.org/x3dom/release/x3dom.js"></script>
   	<script type="text/javascript" src="${contextPath}/3d/script.js"></script>
   	<script type="text/javascript" src="${contextPath}/3d/jscolor/jscolor.js"></script>

</body>
</html>
