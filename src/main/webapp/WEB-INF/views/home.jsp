<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>My HTML View</title>
	<%--<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />--%>
	<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Bootstrap5 Calculator</title>
	<!-- JQuery -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

	<!-- Bootstrap CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.css"/>
	<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.js"></script>
</head>
<body>
<!-- Navigation bar -->
<nav class="navbar fixed-top navbar-expand-lg navbar-light bg-dark">
	<div class="container-fluid">
		<a class="navbar-brand text-light" href="#">
			<%--<img src="icon.png" alt="" width="30" height="30" class="d-inline-block">--%>
			Bootstrap5 Calculator</a>
		<button class="navbar-toggler bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarText">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
			</ul>
			<c:if test = "${role == 'admin'}">
				<a class="text-light nav-item nav-link" href="<c:url value="/views/history" />">History</a>
			</c:if>
			<c:if test = "${role == 'user'}">
				<a class="text-light nav-item nav-link" href="<c:url value="/views/home" />">Home</a>
			</c:if>
			<a class="text-light nav-item nav-link" href="<c:url value="/sign-in/sign-out" />">Log out</a>
		</div>
	</div>
</nav>

<div class="row mt-3 mb-3">
	<div class="col-sm-1 col-xl-1"></div>
	<div class="col-sm-6 col-xl-4">
		<h4>Username: ${username}</</h4>
	</div>
	<div class="col-sm-5 col-xl-7"></div>
</div>

<div class="row mt-3 mb-3">
	<div class="col-sm-1 col-xl-1"></div>
	<div class="col-sm-6 col-xl-4">
		<h4>Role ID: ${role}</</h4>
	</div>
	<div class="col-sm-5 col-xl-7"></div>
</div>

<!-- App title -->
<div class="row mt-3 mb-3">
	<div class="col-sm-3 col-xl-4"></div>
	<div class="col-sm-6 col-xl-4" align="center">
		<h2>Bootstrap5 Calculator</h2>
	</div>
	<div class="col-sm-3 col-xl-4"></div>
</div>

<form>
	<div class="row mt-2">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-4 col-xl-2">
			<label for="number1" class="form-label">First number</label>
		</div>
		<div class="col-sm-5 col-xl-7"></div>
	</div>
	<div class="row">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-4 col-xl-2">
			<input type="number" class="form-control" id="number1" aria-describedby="number1Help" placeholder="Number" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Number'" step="0.01">
		</div>
		<div class="col-sm-5 col-xl-6"></div>
	</div>

	<div class="row">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-5 col-xl-3">
			<div id="number1Help" class="form-text">Insert the first number of the operation.</div>
		</div>
		<div class="col-sm-4 col-xl-5"></div>
	</div>

	<div class="row mt-2">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-4 col-xl-2">
			<label for="operation" class="form-label">Operation</label>
			<select id="operation" class="form-select">
				<option value="+">+</option>
				<option value="-">-</option>
				<option value="*">*</option>
				<option value="/">/</option>
			</select>
		</div>
		<div class="col-sm-5 col-xl-6"></div>
	</div>

	<div class="row">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-5 col-xl-3">
			<div id="operationHelp" class="form-text">Choose between + , - , * , / .</div>
		</div>
		<div class="col-sm-4 col-xl-5"></div>
	</div>

	<div class="row mt-2">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-4 col-xl-2">
			<label for="number1" class="form-label">Second number</label>
		</div>
		<div class="col-sm-5 col-xl-7"></div>
	</div>
	<div class="row">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-4 col-xl-2">
			<input type="number" class="form-control" id="number2" aria-describedby="number2Help" placeholder="Number" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Number'" step="0.01">
		</div>
		<div class="col-sm-5 col-xl-6"></div>
	</div>

	<div class="row mb-2">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-5 col-xl-3">
			<div id="number2Help" class="form-text">Insert the second number of the operation.</div>
		</div>
		<div class="col-sm-4 col-xl-5"></div>
	</div>

	<div class="row">
		<div class="col-sm-3 col-xl-4"></div>
		<div class="col-sm-5 col-xl-3">
			<button id="buttonCalculate" type="button" class="btn btn-dark">Calculate</button>
		</div>
		<div class="col-sm-4 col-xl-5"></div>
	</div>
</form>
<div class="row mt-5">
	<div class="col-sm-3 col-lg-3 col-xl-4"></div>
	<div class="col-sm-5 col-lg-6 col-xl-4" align="center" id="historyTableCol">
		<table id="historyTable">
			<thead>
			<tr>
				<th>X</th>
				<th>Operation</th>
				<th>Y</th>
				<th>Result</th>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<div class="col-sm-4 col-lg-3 col-xl-4"></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js" integrity="sha384-KsvD1yqQ1/1+IA7gi3P0tyJcT3vR+NdBTt13hSJ2lnve8agRGXTTyNaBYmCR/Nwi" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.min.js" integrity="sha384-nsg8ua9HAw1y0W1btsyWgBklPnCUAFLuTMS2G72MMONqmOymq585AcH49TLBQObG" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/resources/function.js" />"></script>
<script>

</script>
</body>
</html>
