$(document).ready(function(){

    document.getElementById("searchValue4").valueAsDate = new Date();

    document.getElementById("searchValue1").style.display = "block";
    document.getElementById("searchValue2").style.display = "none";
    document.getElementById("searchValue3").style.display = "none";
    document.getElementById("searchValue4").style.display = "none";
    var t = $('#historyTable').DataTable({
        columnDefs: [
            {
                "width": "25%", "targets": "_all",
                className: 'dt-body-center'
            }
        ],
        select: true
    });
    /* Database TABLE to DataTable */
    $.ajax({
        type: "GET",
        url: "/spring-mvc-showcase/views/getHistory",
        contentType: "application/json",
        success: function (result) {
            let i;
            console.log(result);
            console.log(typeof result);
            for (i = 0; i < result.length; i++) {
                t.row.add([result[i].firstNumber, result[i].operation, result[i].secondNumber, result[i].apotelesma, result[i].hmeromhnia_, result[i].wra_]).draw(false);
            }
        },
        error: function (xhr) {
            console.log("Error getting response in AJAX");
        }
    });

    document.getElementById("searchType").onchange = function() {
        if($("#searchType").val() === "FIRST_NUMBER" || $("#searchType").val() === "SECOND_NUMBER" || $("#searchType").val() === "APOTELESMA"){
            document.getElementById("searchValue1").style.display = "block";
            document.getElementById("searchValue2").style.display = "none";
            document.getElementById("searchValue3").style.display = "none";
            document.getElementById("searchValue4").style.display = "none";
        } else if ($("#searchType").val() === "OPERATION"){
            document.getElementById("searchValue1").style.display = "none";
            document.getElementById("searchValue2").style.display = "block";
            document.getElementById("searchValue3").style.display = "none";
            document.getElementById("searchValue4").style.display = "none";
        } else if ($("#searchType").val() === "HMEROMHNIA_"){
            document.getElementById("searchValue1").style.display = "none";
            document.getElementById("searchValue2").style.display = "none";
            document.getElementById("searchValue3").style.display = "block";
            document.getElementById("searchValue4").style.display = "block";
        }
    };

    $('#searchButton').on('click', function(){
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
                    t.row.add([result[i].firstNumber, result[i].operation, result[i].secondNumber, result[i].apotelesma, result[i].hmeromhnia_, result[i].wra_]).draw(false);
                }
            },
            error: function (xhr) {
                console.log("Error getting response in AJAX");
            }
        });
        return false;
    });
});