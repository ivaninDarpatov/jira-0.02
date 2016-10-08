<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
	function openWin() {
		$("#dialog").dialog({
			width: 535.6
		});
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<h1>Company</h1>
	<c:out value="${company.name}">
	</c:out>
	</p>


	<h2>LogedUser</h2>
	<c:out value="${user.email}"></c:out>
	</p>

	<h3>Projects</h3>
	<c:forEach items="${projects}" var="project">
		<c:out value="${project}">
		</c:out>
		</br>
	</c:forEach>

	<h4>Users</h4>
	<c:forEach items="${companyUsers}" var="user">
		<c:out value="${user}"></c:out>
		</br>
	</c:forEach>



	<button onclick="openWin()">Create Issue</button>


	<div style="display: none;" id="dialog" title="Create Issue" >

		<div id="container" class="ltr">
			<h1 id="logo">
				<a href="http://localhost:8080/MyProject/HomePage" title="Powered by Godzilla">Godzilla</a>
			</h1>

			<form id="form" name="form" class="wufoo topLabel page1"
				accept-charset="UTF-8" autocomplete="off"
				enctype="multipart/form-data" action="./HomePage" method="POST" >

				<header id="header" class="info">
				<h2>Issue Tracking</h2>
				<div></div>
				</header>


				<ul>
					<li id="fo2li1" class="notranslate      "><label class="desc"
						id="title1" for="Field1"> Summary <span id="req_1"
							class="req">*</span>
					</label>
						<div>
							<input id="summary" name="summary" type="text"
								class="field text medium" value="" maxlength="255" tabindex="1" 
								onkeyup="handleInput(this); " onchange="handleInput(this);"
								required />
						</div></li>

					<li id="fo2li105" class="leftHalf      "><label
						class="desc notranslate" id="title105" for="Field105">
							Issue type </label>
						<div>
							<select id="issue_type" name="issue_type" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="2">
								<option value="task" selected="selected">
								<span class="notranslate">Task</span>
								</option>
								<option value="bug"><span class="notranslate">Bug</span>
								</option>
								<option value="story"><span class="notranslate">Story</span>
								<option value="epic"><span class="notranslate">Epic</span>
								</option>
							</select>
						</div></li>
						
					<li id="fo2li105" class="leftHalf      "><label
						class="desc notranslate" id="title105" for="Field105">
							Project </label>
						<div>
							<select id="project" name="project" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="3">
								<c:forEach items="${projects}" var="project">
								    <option value="${project}">${project.name}</option>	
								</c:forEach>
							</select>
						</div></li>

					<li id="fo2li115" class="leftHalf      "><label
						class="desc notranslate" id="title115" for="Field115">
							Priority </label>
						<div>
							<select id="priority" name="priority" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="4">
								<option value="verry_low" selected="selected"><span
										class="notranslate">Verry Low</span>
								</option>
								<option value="low"><span class="notranslate">Low</span>
								</option>
								<option value="medium"><span class="notranslate">Medium</span>
								</option>
								<option value="high"><span class="notranslate">High</span>
								</option>
								<option value="verry high"><span class="notranslate">Verry High</span>
								</option>
							</select>
						</div></li>

					<li id="fo2li106" class="rightHalf      "><label
						class="desc notranslate" id="title106" for="Field106">
							Status </label>
						<div>
							<select id="status" name="status" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="5">
								<option value="to do" selected="selected"><span
										class="notranslate">TO DO</span>
								</option>
								<option value="in progress"><span class="notranslate">In Progress</span>
								</option>
								<option value="done"><span class="notranslate">Done</span>
								</option>
							</select>
						</div></li>

					<li id="fo2li112" class="notranslate      "><label
						class="desc" id="title112" for="Field112"> Description </label>

						<div>
							<textarea id="description" name="description"
								class="field textarea medium" spellcheck="true" rows="10"
								cols="50" tabindex="6" onkeyup="handleInput(this); "
								onchange="handleInput(this);"></textarea>

						</div></li>


					<li id="fo2li114" class="      "><label
						class="desc notranslate" id="title114" for="Field114">
							Linked Issues </label>
						<div>
							<select id="linked_issues" name="linked_issues" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="7">
								<c:forEach items="${projects}" var="project">
									<c:forEach items="${project.issues}" var="issue">
										<option value="${issue}">${issue.summary}</option>	
									</c:forEach>
								</c:forEach>
							</select>
						</div></li>
						
						<li id="fo2li114" class="      "><label
						class="desc notranslate" id="title114" for="Field114">
							Link Type </label>
						<div>
							<select id="link_type" name="link_type" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="8">
								
								<option value="IS BLOCKED BY">IS BLOCKED BY</option>
								<option value="BLOCKS">BLOCKS</option>
								<option value="IS CAUSED BY">IS CAUSED BY</option>
								<option value="CAUSES">CAUSES</option>
								<option value="IS CLONED BY">IS CLONED BY</option>
								<option value="CLONES">CLONES</option>
								<option value="IS DUPLICATED BY">IS DUPLICATED BY</option>
								<option value="DUPLICATES">DUPLICATES</option>
								<option value="RELATES TO">RELATES TO</option>		
							</select>
						</div></li>
						

					<li id="fo2li102" class="      "><label
						class="desc notranslate" id="title102" for="Field102">
							Assign to </label>
						<div>
							<select id="assignee" name="assignee" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="9">
								<c:forEach items="${companyUsers}" var="user">
								    <option value="${user}">${user.email}</option>	
								</c:forEach>
							</select>
						</div></li>

					<li class="buttons ">
						<div>
							<input type="hidden" name="currentPage" id="currentPage" />
							 <input	id="saveForm" name="saveForm" class="btTxt submit" type="submit" value="Submit" /> 
						</div>
					</li>
					
				</ul>
			</form>


		</div>
		<!--container-->
	</div>
</body>
</html>