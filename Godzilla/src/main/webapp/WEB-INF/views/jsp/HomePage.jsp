<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
function openWin() {
	 $( "#dialog" ).dialog();
}
</script>
<title>Insert title here</title>
</head>
<body>
<h1> Company</h1> 
<c:out value="${company.name}"> </c:out></p>


<h2>LogedUser</h2> 
<c:out value="${user.email}"></c:out></p>

<h3> Projects</h3>
<c:forEach items="${projects}" var="project">
	<c:out value="${project}">  </c:out></br>
 </c:forEach>
 
<h4>Users</h4>
 <c:forEach items="${companyUsers}" var="user">
	<c:out value="${user}"></c:out></br>
 </c:forEach>



<button onclick="openWin()" > Create Issue</button>


<div id="dialog" title="Basic dialog" >
  <p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div>
</body>
</html>