var result = (function() {
    var serverHost = "localhost";
    var serverPort = "8081";
    var serverProgramStatusEndpoint = "/codecompiler/status";

    var getQueryVariable = function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
        return (false);
    };

    var serverProgramStatusURL = "http://" + serverHost + ":" + serverPort + serverProgramStatusEndpoint + "?queueId=" + getQueryVariable("queueId");

    var downloadVariableAsFile = function(v) {
        var hiddenElement = document.createElement('a');
        hiddenElement.href = 'data:attachment/text,' + encodeURI(v);
        hiddenElement.target = '_blank';
        hiddenElement.download = 'output.txt';
        hiddenElement.click();
    };

    var redirectToHome = function() {
        window.location = "/index.html"
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
            "url": serverProgramStatusURL,
            "method": "GET",
        }).done(function(data) {
            updateResultOnScreen(data);
        }).fail(function() {

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