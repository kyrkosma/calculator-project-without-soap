<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>My HTML View</title>
    <%--<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />--%>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Calculator</title>
    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.css"/>
    <!-- Hide arrows in number input -->
    <style>/* Chrome, Safari, Edge, Opera */
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    /* Firefox */
    input[type=number] {
        -moz-appearance: textfield;
    }</style>
    <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.23/sl-1.3.1/datatables.min.js"></script>
</head>
<body>
<!-- Navigation bar -->
<nav class="navbar fixed-top navbar-expand-lg navbar-light bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand text-light">
            <%--<img src="icon.png" alt="" width="30" height="30" class="d-inline-block">--%>
            Calculator</a>
        <button class="navbar-toggler bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            </ul>
            <a class="text-light nav-item nav-link" href="<c:url value="/views/home" />">Home</a>
            <a class="text-light nav-item nav-link" href="<c:url value="/logout" />">Log out</a>
        </div>
    </div>
</nav>

<%--<div class="row mt-3 mb-3">
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
</div>--%>

<!-- App title -->
<div class="row mt-3 mb-3">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-6 col-xl-4" align="center">
        <h2>Calculator</h2>
    </div>
    <div class="col-sm-3 col-xl-4"></div>
</div>

<div class="row mt-3 mb-3">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-6 col-xl-4" align="center">
        <h2>Data History</h2>
    </div>
    <div class="col-sm-3 col-xl-4"></div>
</div>

<div class="row mt-2">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-4 col-xl-2">
        <label id="labelUser" class="form-label">User</label>
        <select id="selectUser" class="form-select">

        </select>
    </div>
    <div class="col-sm-5 col-xl-6"></div>
</div>

<div class="row mt-2">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-4 col-xl-2">
        <label class="form-label">Type of Value</label>
        <select id="searchType" class="form-select">
            <option value="all"> </option>
            <option value="FIRST_NUMBER">X</option>
            <option value="OPERATION">Operation</option>
            <option value="SECOND_NUMBER">Y</option>
            <option value="APOTELESMA">Result</option>
            <option value="HMEROMHNIA_">Date</option>
        </select>
    </div>
    <div class="col-sm-5 col-xl-6"></div>
</div>

<div class="row mt-2">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-4 col-xl-2">
        <label id="searchLabel" class="form-label">Value</label>
        <input id="searchValue1" type="number" placeholder="Search" class="form-control" />
        <select id="searchValue2" class="form-select">
            <option value="+">+</option>
            <option value="-">-</option>
            <option value="*">*</option>
            <option value="/">/</option>
        </select>
        <input id="searchValue3" type="date" class="form-control" />
        <input id="searchValue4" type="date" class="form-control" />
    </div>
    <div class="col-sm-5 col-xl-6"></div>
</div>

<div class="row mt-2">
    <div class="col-sm-3 col-xl-4"></div>
    <div class="col-sm-2 col-xl-1">
        <button id="searchButton" type="button" class="btn btn-dark">Search</button>
    </div>
    <div class="col-sm-2 col-xl-1">
        <button id="clearButton" type="button" class="btn btn-dark">Clear</button>
    </div>
    <div class="col-sm-5 col-xl-6"></div>
</div>

<!-- Datatable -->
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
                <th>Date</th>
                <th>Time</th>
                <th>User</th>
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
<script type="text/javascript" src="<c:url value="/resources/history.js" />"></script>
<script>

</script>
</body>
</html>
