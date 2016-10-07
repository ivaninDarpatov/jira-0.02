<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Create issue</title>
</head>
<body>
	<label> Summary</label>
	<input type="text" name="summary" id="summary" placeholder="Enter summary" required />
	
	<label> description</label>
	<input type="text" name="description" id="description" placeholder="Enter description" />
	
	<label> Priority</label>
	<select>
		<option value="verry_low">VERY LOW</option>
		<option value="low">LOW</option>
		<option value="medium">MEDIUM</option>
		<option value="high">HIGH</option>
		<option value="verry_high">VERY HIGH</option>
	</select>
</body>
</html>

<!-- 
	 summary;
	 description;
	 priority;
	 state;
	 dateCreated;
	 dateLastModified;
	 linkedIssues -->