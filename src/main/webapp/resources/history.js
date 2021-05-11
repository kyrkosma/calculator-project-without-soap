$(document).ready(function(){

    var z = document.createElement("option");
    z.setAttribute("value", "all");
    var t = document.createTextNode(" ");
    z.appendChild(t);
    document.getElementById("selectUser").appendChild(z);

    document.getElementById("searchValue4").valueAsDate = new Date();

    document.getElementById("searchLabel").style.display = "none";
    document.getElementById("searchValue1").style.display = "none";
    document.getElementById("searchValue2").style.display = "none";
    document.getElementById("searchValue3").style.display = "none";
    document.getElementById("searchValue4").style.display = "none";
    document.getElementById("clearButton").style.display = "none";

    var t = $('#historyTable').DataTable({
        columnDefs: [
            {
                "width": "25%", "targets": "_all",
                className: 'dt-body-center'
            }
        ],
        select: true
    });

    //Get users from Database
    $.ajax({
        type: "GET",
        url: "/spring-mvc-showcase/views/getUsers",
        contentType: "application/json",
        success: function (result) {
            console.log(result);
            for (let i = 0; i < result.length; i++) {
                var z = document.createElement("option");
                z.setAttribute("value", result[i]);
                var t = document.createTextNode(result[i]);
                z.appendChild(t);
                document.getElementById("selectUser").appendChild(z);
            }
        },
        error: function (xhr) {
            console.log("Error getting response in AJAX");
        }
    });

    /* Database TABLE to DataTable */
    $.ajax({
        type: "GET",
        url: "/spring-mvc-showcase/views/getHistory",
        contentType: "application/json",
        success: function (result) {
            let i;
            /*console.log(result);
            console.log(typeof result);*/
            for (i = 0; i < result.length; i++) {
                t.row.add([result[i].firstNumber, result[i].operation, result[i].secondNumber, result[i].apotelesma, result[i].hm, result[i].wra_, result[i].userName]).draw(false);
            }
        },
        error: function (xhr) {
            console.log("Error getting response in AJAX");
        }
    });

    document.getElementById("selectUser").onchange = function() {
        document.getElementById("clearButton").style.display = "block";
    }
    document.getElementById("searchValue3").onchange = function() {
        if ($("#searchValue4").val() !== "" && $("#searchValue3").val() > $("#searchValue4").val()){
            alert("The start date can not be after the end date.");
            $("#searchValue3").val("");
        }
    }

    document.getElementById("searchValue4").onchange = function() {
        if ($("#searchValue3").val() !== "" && $("#searchValue3").val() > $("#searchValue4").val()){
            alert("The end date can not be before the start date.");
            $("#searchValue4").val("");
        }
    }

    document.getElementById("searchType").onchange = function() {
        document.getElementById("clearButton").style.display = "block";
        if($("#searchType").val() === "FIRST_NUMBER" || $("#searchType").val() === "SECOND_NUMBER" || $("#searchType").val() === "APOTELESMA"){
            $("#searchValue1").val("");
            $("#searchValue2").val("+");
            $("#searchValue3").val("");
            document.getElementById("searchValue4").valueAsDate = new Date();

            document.getElementById("searchLabel").style.display = "block";
            document.getElementById("searchValue1").style.display = "block";
            document.getElementById("searchValue2").style.display = "none";
            document.getElementById("searchValue3").style.display = "none";
            document.getElementById("searchValue4").style.display = "none";
        } else if ($("#searchType").val() === "OPERATION"){
            $("#searchValue1").val("");
            $("#searchValue2").val("+");
            $("#searchValue3").val("");
            document.getElementById("searchValue4").valueAsDate = new Date();

            document.getElementById("searchLabel").style.display = "block";
            document.getElementById("searchValue1").style.display = "none";
            document.getElementById("searchValue2").style.display = "block";
            document.getElementById("searchValue3").style.display = "none";
            document.getElementById("searchValue4").style.display = "none";
        } else if ($("#searchType").val() === "HMEROMHNIA_"){
            $("#searchValue1").val("");
            $("#searchValue2").val("+");
            $("#searchValue3").val("");
            document.getElementById("searchValue4").valueAsDate = new Date();

            document.getElementById("searchLabel").style.display = "block";
            document.getElementById("searchValue1").style.display = "none";
            document.getElementById("searchValue2").style.display = "none";
            document.getElementById("searchValue3").style.display = "block";
            document.getElementById("searchValue4").style.display = "block";
        } else if ($("#searchType").val() === "all"){
            $("#searchValue1").val("");
            $("#searchValue2").val("+");
            $("#searchValue3").val("");
            document.getElementById("searchValue4").valueAsDate = new Date();

            document.getElementById("searchLabel").style.display = "none";
            document.getElementById("searchValue1").style.display = "none";
            document.getElementById("searchValue2").style.display = "none";
            document.getElementById("searchValue3").style.display = "none";
            document.getElementById("searchValue4").style.display = "none";
        }
    };

    $('#searchButton').on('click', function(){
        var uName = $("#selectUser").val();
        var op = $("#searchType").val();
        var val1 = null;
        var val2 = null;
        if(document.getElementById("searchValue1").style.display === "block"){
            val1 = $("#searchValue1").val();
            val2 = "empty";
        } else if(document.getElementById("searchValue2").style.display === "block"){
            val1 = $("#searchValue2").val();
            val2 = "empty";
        } else if(document.getElementById("searchValue3").style.display === "block"){
            val1 = $("#searchValue3").val().toString();
            val2 = $("#searchValue4").val().toString();
        } else if($("#searchType").val() === "all"){
            val1 = "empty";
            val2 = "empty";
        }

        if (val1 === "" && $("#searchType").val() !== "all"){
            alert("You can not filter history by an empty/non-acceptable value.");
            return false;
        }

        /*var val = $("#searchValue").val();*/

        console.log(op, typeof op);
        console.log(val1, typeof val1);
        console.log(val2, typeof val2);

        $.ajax({
            type: "GET",
            url: "/spring-mvc-showcase/views/searchDB",
            contentType: "application/json",
            data: {
                userName: uName,
                operation: op,
                value1 : val1,
                value2 : val2
            },
            success: function (result) {
                t.clear().draw();
                let i;
                console.log(result);
                console.log(typeof result);
                for (i = 0; i < result.length; i++) {
                    t.row.add([result[i].firstNumber, result[i].operation, result[i].secondNumber, result[i].apotelesma, result[i].hm, result[i].wra_, result[i].userName]).draw(false);
                }
            },
            error: function (xhr) {
                console.log("Error getting response in AJAX (searchDB)");
            }
        });
        return false;
    });

    $('#clearButton').on('click', function(){
        document.getElementById("searchLabel").style.display = "none";
        document.getElementById("searchValue1").style.display = "none";
        document.getElementById("searchValue2").style.display = "none";
        document.getElementById("searchValue3").style.display = "none";
        document.getElementById("searchValue4").style.display = "none";
        document.getElementById("clearButton").style.display = "none";
        $("#selectUser").val("all");
        $("#searchType").val("all");
        $("#searchValue1").val("");
        $("#searchValue2").val("+");
        $("#searchValue3").val("");

        $.ajax({
            type: "GET",
            url: "/spring-mvc-showcase/views/getHistory",
            contentType: "application/json",
            success: function (result) {
                t.clear().draw();
                let i;
                console.log(result);
                console.log(typeof result);
                for (i = 0; i < result.length; i++) {
                    t.row.add([result[i].firstNumber, result[i].operation, result[i].secondNumber, result[i].apotelesma, result[i].hm, result[i].wra_, result[i].userName]).draw(false);
                }
            },
            error: function (xhr) {
                console.log("Error getting response in AJAX (searchDB)");
            }
        });
        return false;
    });
});