<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Calculator</title>
	<%--<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />--%>
	<!-- Required meta tags -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- JQuery -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

	<!-- Bootstrap CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.css"/>
	<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.js"></script>
	<link href="<c:url value="/resources/style.css" />" rel="stylesheet"  type="text/css" />
</head>
<body>
<div class="wrapper fadeInDown">
	<div id="formContent">
		<form action="<c:url value="/sign-in/"/>" method="POST">
			<input type="text" id="username" class="fadeIn second" name="username" minlength="5" maxlength="15" placeholder="Login" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Login'">
			<input type="password" id="password" class="fadeIn third" name="password" minlength="5" maxlength="20" placeholder="Password" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Password'">
			<input type="submit" id="btnLogin" class="fadeIn fourth" value="Log In">
		</form>

		<%--<div id="formFooter">
			<a class="underlineHover" href="#">Forgot Password?</a>
		</div>--%>

	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.6.0/dist/umd/popper.min.js" integrity="sha384-KsvD1yqQ1/1+IA7gi3P0tyJcT3vR+NdBTt13hSJ2lnve8agRGXTTyNaBYmCR/Nwi" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/js/bootstrap.min.js" integrity="sha384-nsg8ua9HAw1y0W1btsyWgBklPnCUAFLuTMS2G72MMONqmOymq585AcH49TLBQObG" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/resources/login.js" />"></script>
<script>

</script>
</body>
</html>
