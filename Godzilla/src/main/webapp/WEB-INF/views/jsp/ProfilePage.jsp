<%@page import="com.godzilla.model.User"%>
<%@page import="java.util.Set"%>
<%@page import="com.godzilla.model.Project"%>
<%@page import="com.godzilla.model.Company"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- jQuery UI CSS -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/user_panel.css" rel="stylesheet">

<script>
	function closeDiv(target) {
		var div = $(target);
		div.hide();
	}
</script>

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

	<c:import url="Navigation.jsp" />

	<c:set var="errorMessage" value="${sessionScope.issueError}" />
	<c:set var="errorLengh" value="${fn:length(errorMessage)}" />
	<c:remove var="issueError" scope="session" />

	<c:set var="succeed" value="${sessionScope.succeed}"></c:set>
	<c:set var="succeedLengh" value="${fn:length(succeed)}" />
	<c:remove var="succeed" scope="session" />
	<div id="errors_container">
		<c:if test="${(errorLengh) gt 0}">
			<div id="tag1" class="tag1"
				style="text-align: center; color: red; font-family: 'Arial Black';"
				id="errors">
				<a class="closeButton" onclick="closeDiv('#tag1')"></a> <b>${(errorMessage)}
				</b>
			</div>
		</c:if>
		<c:if test="${(succeedLengh) gt 0}">
			<div id="tag1" class="tag2"
				style="text-align: center; color: green; font-family: 'Arial Black';"
				id="errors">
				<a class="closeButton" onclick="closeDiv('#tag1')"></a> <b>${(succeed)}</b>
			</div>
		</c:if>

	</div>

	<!-- hidden input to hold session attribute -->
	<input id="userInformation" type="hidden" value="${userJSON}">
	<!-- Page Content -->
	<div class="container">
		<div class="row">

			<!-- LEFT SIDE -->
			<div class="col-md-8">
				<div id="change_avatar" class="profile_edit_box">
					<h2>Change avatar</h2>
					<form>
						<input type="radio" name="avatar" value="blue"><img
							height="40" width="40" src="images/profile_photo.png">&nbsp
						<input type="radio" name="avatar" value="red"><img
							height="40" width="40" src="images/profile_photo.png"><br>
						<br> <input type="radio" name="avatar" value="green"><img
							height="40" width="40" src="images/profile_photo.png">&nbsp
						<input type="radio" name="avatar" value="yellow"><img
							height="40" width="40" src="images/profile_photo.png"><br>
						<br> <input type="radio" name="avatar" value="purple"><img
							height="40" width="40" src="images/profile_photo.png">&nbsp
						<input type="radio" name="avatar" value="black"><img
							height="40" width="40" src="images/profile_photo.png"><br>
						<br>

						<button>DONE</button>
					</form>
				</div>
				<hr>
				<form action="./changeEmail" method="POST">
					<div id="change_email" class="profile_edit_box">
						<h2>Change e-mail</h2>
						<!-- old email, new email, password twice and execute -->

						<table style="width: 80%">
							<tr>
								<td class="labels"><label>Current e-mail:</label></td>
								<td class="fields"><input type="text" name="current_email"
									placeholder="Enter your current e-mail" /></td>
							</tr>
							
							<tr>
								<td class="labels"><label>New e-mail:</label></td>
								<td class="fields"><input type="text" name="new_email"
									placeholder="Enter your new e-mail" /></td>
							</tr>
							<tr>
								<td class="labels"><label>Confirm e-mail:</label></td>
								<td class="fields"><input type="text" name="repeat_email"
									placeholder="Repeat your new e-mail" /></td>
							</tr>
							<tr>
								<td class="labels"><label>Password:</label></td>
								<td class="fields"><input type="password" name="password"
									placeholder="Enter your password" /></td>
							</tr>
							<tr>
								<td class="labels"><label>Confirm password:</label></td>
								<td class="fields"><input type="password"
									name="repeat_password" placeholder="Repeat your password" /></td>
							</tr>
						</table>
						<br> <input type="submit" value="Change email" />

					</div>
				</form>
				<hr>

				<form action="./changePassword" method="POST">
				<div id="change_password" class="profile_edit_box">
					<h2>Change password</h2>
					<!-- old password twice, new password twice and execute -->
					<table style="width: 80%">
						<tr>
							<td class="labels"><label>Current password:</label></td>
							<td class="fields"><input type="password" name="current_pass"
								placeholder="Enter your current password" /></td>
						</tr>
						<tr>
							<td class="labels"><label>Confirm current password:</label>
							</td>
							<td class="fields"><input type="password" name="conf_current_pass"
								placeholder="Repeat your current password" /></td>
						</tr>
						<tr>
							<td class="labels"><label>New password:</label></td>
							<td class="fields"><input type="password" name="new_password"
								placeholder="Enter your new password" /></td>
						</tr>
						<tr>
							<td class="labels"><label>Confirm new password:</label></td>
							<td class="fields"><input type="password" name="conf_new_password"
								placeholder="Repeat your new password" /></td>
						</tr>
					</table>
					<br>
					<input type="submit" value="Change password" />
				</div>
				</form>
				<hr>

				<form action="./deleteUser" method="POST">
				<div id="remove_user" class="profile_edit_box">
					<h2>Delete profile</h2>
					<!-- password twice and execute -->
					<table style="width: 80%">
						<tr>
							<td class="labels"><label>Password:</label></td>
							<td class="fields"><input type="password" name="delete_password"
								placeholder="Enter your current password" /></td>
						</tr>
						<tr>
							<td class="labels"><label>Confirm password:</label></td>
							<td class="fields"><input type="password" name="delete_conf_password"
								placeholder="Repeat your current password" /></td>
						</tr>
					</table>
					<br>
					<input type="submit" value="Delete User" />
				</div>
				</form>

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
							<th id="user_photo"><img src="images/profile_photo.png"
								width="50px" height="50px"></th>

							<th id="user_email">${user.email}</th>

						</tr>
					</table>
					<hr>
					<div id="user_profile_information"></div>
				</div>


				<div id='issue_info_well' class="well"></div>
			</div>
			<!-- /RIGHT SIDE -->

		</div>
		<!-- /.row -->

		<hr>

		<!-- Footer -->
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p>Copyright &copy; Godzilla 2016</p>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->
		</footer>

	</div>
	<!-- /.container -->


</body>
</html>