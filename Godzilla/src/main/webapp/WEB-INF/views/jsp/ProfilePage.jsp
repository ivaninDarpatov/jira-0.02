<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<!-- jQuery UI CSS -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
	
	<!-- Bootstrap Core CSS -->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	
	<!-- Custom CSS -->
	<link href="css/user_panel.css" rel="stylesheet">
	
	
	<!-- javascript files initialization -->
	
	<!-- jQuery JavaScript -->
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>
		
	<!-- header functionality javascript -->
	<script src="js/page_header.js"></script>
		
	<!-- profile page javascript -->
	<script src="js/profile_page.js"></script>
	
</head>

<body>

<!-- BEGINNING OF 'ONE-FOR-ALL' PART -->

<!-- Create Issue popUp  -->
<!----------------------------------------------------------------------------------------------------------------------------------------  -->
<div style="display: none;" id="dialog" title="Create Issue">
		<div id="container" class="ltr">
			<h1 id="logo">
				<a href="./homepage" title="Powered by Godzilla">Godzilla</a>
			</h1>

			<form action="./homepage" method="POST">
				
				<header id="header" class="info">
					<h2>Issue Tracking</h2>
					<div></div>
				</header>
				<ul>
					<li id="fo2li1" class="notranslate      ">
						<label class="desc" id="title1" for="Field1"> Summary 
							<span id="req_1" class="req">*</span>
						</label>
							<div>
								<input id="summary" name="summary" type="text"
									class="field text medium" value="" maxlength="255" tabindex="1"
									onkeyup="handleInput(this); " onchange="handleInput(this);"
									required />
							</div>
					</li>

					<li id="fo2li105" class="leftHalf      ">
						<label class="desc notranslate" id="title105" for="Field105">
							Issue type 
						</label>
						<div>
							<select id="issue_type" name="issue_type"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="2">
								<option value="task" selected="selected">
									<span class="notranslate">Task</span>
								</option>
								<option value="bug">
									<span class="notranslate">Bug</span>
								</option>
								<option value="story">
									<span class="notranslate">Story</span>
								<option value="epic">
									<span class="notranslate">Epic</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li105" class="leftHalf      ">
						<label class="desc notranslate" id="title105" for="Field105">
							Project 
						</label>
						<div>
							<select id="project" name="project" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="3">
								<c:forEach items="${companyProjects}" var="project">
									<option value="${project.name}">${project.name}</option>
								</c:forEach>
							</select>
						</div>
					</li>

					<li id="fo2li115" class="leftHalf      ">
						<label class="desc notranslate" id="title115" for="Field115">
							Priority
						</label>
						<div>
							<select id="priority" name="priority" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="4">
								<option value="verry_low" selected="selected">
									<span class="notranslate">Very Low</span>
								</option>
								<option value="low">
									<span class="notranslate">Low</span>
								</option>
								<option value="medium">
									<span class="notranslate">Medium</span>
								</option>
								<option value="high">
									<span class="notranslate">High</span>
								</option>
								<option value="verry high">
									<span class="notranslate">Very High</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li106" class="rightHalf      ">
						<label class="desc notranslate" id="title106" for="Field106">
							Status 
						</label>
						<div>
							<select id="status" name="status" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="5">
								<option value="to do" selected="selected">
									<span class="notranslate">TO DO</span>
								</option>
								<option value="in progress">
									<span class="notranslate">IN PROGRESS</span>
								</option>
								<option value="done">
									<span class="notranslate">DONE</span>
								</option>
							</select>
						</div>
					</li>

					<li id="fo2li112" class="notranslate      ">
						<label class="desc" id="title112" for="Field112"> 
							Description 
						</label>
						<div>
							<textarea id="description" name="description"
								class="field textarea medium" spellcheck="true" rows="10"
								cols="50" tabindex="6" onkeyup="handleInput(this); "
								onchange="handleInput(this);">
							</textarea>
						</div>
					</li>


					<li id="fo2li114" class="      ">
						<label class="desc notranslate" id="title114" for="Field114">
							Linked Issues 
						</label>
						<div>
							<select id="linked_issues" name="linked_issues"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="7">
								<c:forEach items="${companyProjects}" var="project">
									<c:forEach items="${project.issues}" var="issue">
										<option value="${issue.name}">${issue.name}</option>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</li>

					<li id="fo2li114" class="      ">
						<label class="desc notranslate" id="title114" for="Field114">
							Link Type 
						</label>
						<div>
							<select id="link_type" name="link_type"
								class="field select medium" onclick="handleInput(this);"
								onkeyup="handleInput(this);" tabindex="8">

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
						</div>
					</li>


					<li id="fo2li102" class="      ">
						<label class="desc notranslate" id="title102" for="Field102">
							Assign to 
						</label>
						<div>
							<select id="assignee" name="assignee" class="field select medium"
								onclick="handleInput(this);" onkeyup="handleInput(this);"
								tabindex="9">
								<c:forEach items="${companyUsers}" var="user">
									<c:if test="${user.isTester == false}">
 										<option value="${user.email}">${user.email}</option>
 									</c:if>
								</c:forEach>
							</select>
						</div>
					</li>

					<li class="buttons ">
						<div>
							<input type="hidden" name="currentPage" id="currentPage" /> 
							<input id="saveForm" name="saveForm" class="btTxt submit" type="submit" value="Submit" />
						</div>
					</li>

				</ul>
			</form>
		</div>
	</div>



<!----------------------------------------------------------------------------------------------------------------------------------------  -->

<c:import url="Navigation.jsp"/>

<!-- END OF 'ONE-FOR-ALL' PART -->
<input id="userInformation" type="hidden" value='${userJSON}'>
	<!-- Page Content -->
	<div class="container">
		<div class="row">
			
<!-- LEFT SIDE -->
			<div class="col-md-8">
				<div id="change_avatar" class="profile_edit_box">
					<h2>Change avatar</h2>
					<form>
  						<input type="radio" name="avatar" value="blue"><img height="40" width="40" src="images/profile_photo.png">&nbsp
  						<input type="radio" name="avatar" value="red"><img height="40" width="40" src="images/profile_photo.png"><br><br>
  						<input type="radio" name="avatar" value="green"><img height="40" width="40" src="images/profile_photo.png">&nbsp
  						<input type="radio" name="avatar" value="yellow"><img height="40" width="40" src="images/profile_photo.png"><br><br>
  						<input type="radio" name="avatar" value="purple"><img height="40" width="40" src="images/profile_photo.png">&nbsp
  						<input type="radio" name="avatar" value="black"><img height="40" width="40" src="images/profile_photo.png"><br><br>

						<button>DONE</button>
					</form>
				</div>
				<hr>
				
				<div id="change_email" class="profile_edit_box">
					<h2>Change e-mail</h2>
					<!-- old email, new email, password twice and execute -->
					
					<table style="width:80%">
						<tr>
							<td class="labels">
								<label>Current e-mail:</label>
							</td>
							<td class="fields">
								<input type="text" placeholder="Enter your current e-mail"/>
							</td>
						</tr>
						<tr>
							<td class="labels">
								<label>New e-mail:</label>
							</td>
							<td class="fields">
								<input type="text" placeholder="Enter your new e-mail"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Confirm e-mail:</label>
							</td>
							<td class="fields">
								<input type="text" placeholder="Repeat your new e-mail"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Enter your password"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Confirm password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Repeat your password"/>
							</td>							
						</tr>
					</table>
					<br>
					<button>DONE</button>
				</div>
				<hr>
				
				<div id="change_password" class="profile_edit_box">
					<h2>Change password</h2>
					<!-- old password twice, new password twice and execute -->
					<table style="width:80%">
						<tr>
							<td class="labels">
								<label>Current password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Enter your current password"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Confirm current password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Repeat your current password"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>New password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Enter your new password"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Confirm new password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Repeat your new password"/>
							</td>							
						</tr>
					</table>
					<br>
					<button>DONE</button>
				</div>
				<hr>
				
				<div id="remove_user" class="profile_edit_box">
					<h2>Delete profile</h2>
					<!-- password twice and execute -->
					<table style="width:80%">
						<tr>
							<td class="labels">
								<label>Password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Enter your current password"/>
							</td>							
						</tr>
						<tr>
							<td class="labels">
								<label>Confirm password:</label>
							</td>
							<td class="fields">
								<input type="password" placeholder="Repeat your current password"/>
							</td>							
						</tr>
					</table>
					<br>
					<button>DONE</button>
				</div>
				
				<!-- (manager functions) //selects a user from the company and manages status (deletes, updates permissions)
				<div id="update_user" class="profile_edit_box">
				</div>
				 -->
				
			</div>
<!-- /LEFT SIDE -->

<!-- RIGHT SIDE -->
			<!-- User projects Sidebar -->
			<div class="col-md-4">
				<!-- Sidebar Well -->
				<div class="well">
					<table id="user_email_photo">
						<tr>
							<th id="user_photo">
								<img src="images/profile_photo.png" width="50px" height="50px">
							</th>
						
							<th id="user_email">
								${user.email}
							</th>
							
						</tr>
					</table>
					<hr>
					<div id="user_profile_information">
					</div>
				</div>
				
				
				<div id='issue_info_well' class="well">
					
				</div>
			</div>
<!-- /RIGHT SIDE -->

		</div>
		<!-- /.row -->

		<hr>

		<!-- Footer -->
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p>
						Copyright &copy; Godzilla 2016
					</p>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
		</footer>

	</div>
	<!-- /.container -->


</body>
</html>