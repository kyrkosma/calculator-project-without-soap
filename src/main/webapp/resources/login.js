$(document).ready(function(){

    var input = document.getElementById("password");
    input.addEventListener("keyup", function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            document.getElementById("btnLogin").click();
        }
    });

    /*$('#btnLogin').on('click', function() {

        $.ajax({
            type: "POST",
            url: "/spring-mvc-showcase/login/check",
            contentType: "text/plain",
            data: {
                lgn: $("#login").val(),
                pwd : $("#password").val()
            },
            success: function (result) {
                console.log(result);
            },
            error: function (xhr) {
                console.log("Error getting response in AJAX");
            }
        });

    });*/

});