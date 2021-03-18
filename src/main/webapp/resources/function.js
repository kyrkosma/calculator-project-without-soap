$(document).ready(function(){
    var t = $('#historyTable').DataTable({
        columnDefs: [
            {
                "width": "25%", "targets": "_all",
                className: 'dt-body-center',
            }
        ],
        select: true
    });

    $(document).keydown(function(e) {
        if (e.keyCode == 46){
            t.row('.selected').remove().draw(false);
        }
        console.log(e.which);
    });

/*    $('#buttonCalculate').on('click', function(){
        var n1 = $("#number1").val();
        var op = $("#operation").val();
        var n2 = $("#number2").val();
        var res;
        res = calculateResult(n1,op,n2);
        if(op == "*"){
            op = "×";
        } else if(op == "/"){
            op = "÷";
        }
        /!*document.getElementById("result").innerHTML = res;*!/
        t.row.add([n1, op, n2, res]).draw(false);	/!* use .draw() to display the new row in the table, false keeps the current paging *!/
    });

    function calculateResult(v1, op, v2) {
        return eval(v1+op+v2);
    }*/

    $('#buttonCalculate').on('click', function(){
        var n1 = $("#number1").val();
        var op = $("#operation").val();
        var n2 = $("#number2").val();
        $.ajax({
            type: "GET",
            url: "/spring-mvc-showcase/views/submitToDB",
            contentType: "text/plain",
            dataType: "text",
            data: {
                num1: n1.toString(),
                operation: op,
                num2: n2.toString()
            },
            success: function (result) {
                t.row.add([n1, op, n2, result]).draw(false);
                console.log(result);



            },
            error: function (xhr) {
                /*MvcUtil.showErrorResponse(xhr.responseText, button);*/
            }
        });
        return false;
    });
});