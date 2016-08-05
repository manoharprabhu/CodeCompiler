/*global $, sweetAlert, CODECOMPILER_CONFIG */
var result = (function() {
    var getQueryVariable = function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] === variable) {
                return pair[1];
            }
        }
        return (false);
    };

    var downloadVariableAsFile = function(v) {
        var hiddenElement = document.createElement("a");
        hiddenElement.href = "data:attachment/text," + encodeURI(v);
        hiddenElement.target = "_blank";
        hiddenElement.download = "output.txt";
        hiddenElement.click();
    };

    var redirectToHome = function() {
        window.location = "/index.html";
    };
    var updateResultOnScreen = function(data) {
        if (data.data.programStatus === 0) {
            sweetAlert({
                title: "Program Not Found",
                type: "error",
                text: "No such program exists.",
            }, redirectToHome.bind(this));
        } else if (data.data.programStatus === 1 || data.data.programStatus === 2) {
            sweetAlert({
                title: "Program Submitted Successfully",
                type: "info",
                text: "Please check the program status after some time.",
            }, redirectToHome.bind(this));
        } else if (data.data.programStatus === 6) {
            sweetAlert({
                title: "Success",
                type: "success",
                confirmButtonText: "Download Output",
                showCancelButton: true,
                cancelButtonText: "Return to Home"
            }, function(isConfirm) {
                if (isConfirm) {
                    downloadVariableAsFile(data.data.output);
                }
                redirectToHome();
            });
        } else {
            sweetAlert({
                title: "Error",
                type: "error",
                text: data.data.errorMessage,
            }, redirectToHome.bind(this));
        }
    };

    var initialize = function() {
        $.ajax({
            "url": CODECOMPILER_CONFIG.serverProgramStatusURL + "?queueId=" + getQueryVariable("queueId"),
            "method": "GET",
        }).done(function(data) {
            updateResultOnScreen(data);
        }).fail(function() {
            sweetAlert({
                title: "Error",
                text: "Error while getting the result. Please try again later.",
                type: "error",
                confirmButtonText: "Back to home"
            }, redirectToHome.bind(this));
        });
        sweetAlert({
            title: "Getting the result...",
            type: "info",
            text: "Please wait!",
            showConfirmButton: false
        });
    };

    return {
        "initialize": initialize
    };
}());