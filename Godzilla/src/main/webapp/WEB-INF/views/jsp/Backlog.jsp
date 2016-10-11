<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Backlog</title>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
	
<!--  <link rel="stylesheet" href="/resources/demos/style.css"> -->


<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/user_panel.css" rel="stylesheet">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>



<script>
function viewMap (map) {
	var string = JSON.stringify(map);
	var object = JSON.parse(string);
	console.log(object);
}

</script>

</head>
<body>

<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="" style="font-size: 200%;"><b>
						godzilla |<small style="font-size:60%;"> ${company.name} </small></b></a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse--1">
				<ul class="nav navbar-nav">
					<li><a href='#' id='projects_button' class='dropbtn'
						onclick="toggleShowDiv('#projects_menu')">Projects</a>
						<div id='projects_menu' class='dropdown_content'>
							<ul id='projects_menu_ul' class="nav navbar-nav">
								<li><a href='#'>Create Project</a></li>
								
								<c:forEach items="${companyProjects}" var="project">
									<br><li><a href='#'>${project.name}</a></li>
								</c:forEach>
								
							</ul>
						</div></li>
					<li><a href="#" id="opener">Create Issue</a></li>
					<li><a href="./backlog">Backlog</a></li>
					<li><a href="#">Board</a></li>
					<li><a href="./homepage">User Panel</a></li>
					<li><a href='#' id='profile_button' class='dropbtn'
						onclick="toggleShowDiv('#profile_menu')">Profile</a>
						<div id='profile_menu' class='dropdown_content'>
							<ul id='profile_menu_ul' class="nav navbar-nav">
								<li><a href='#'>
									<img class="user_thumbnail" src="images/profile_photo.png">
									${user.email}
								</a></li>
								<br>
								<li><a href="./login">Log Out</a></li>
							</ul>
						</div></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav> 

	<!-- Page Content -->
	<div class="container">

		<div class="row">
			<button onclick='viewMap(${projectSprintIssuesMap})'>test</button>
			<h2 id="project_name"></h2>
			<!-- project's issues column -->
			<div class="col-md-8">
				<hr>
				<div id='sprints_container'>
					<a href='#' onclick="addSprintBacklog('#sprints_container')">add sprint</a>
				</div>

				<hr>

				<h4>Issues</h4>
				<div id='issues_container'>
					<a href="#" onclick="addIssueBacklog('#issues_container')">add issue</a>
				</div>
			</div>

			<!-- User Sidebar Column -->
			<div class="col-md-4">

				<!-- Issue Information Well -->
				<div id='issue_info_well' class="well">
					
				</div>
					
			</div>

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

	
	<!-- page initializers -->
	
	


	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>



	<!-- custom javascript -->
	<script src="js/user_panel.js"></script>
	<script src="js/backlog.js"></script>




<!---->
		<!--container-->

	

</body>
</html>