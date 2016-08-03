var CODECOMPILER_CONFIG = (function() {
    var serverHost = "localhost";
    var serverPort = "8081";
    var serverSubmitEndpoint = "/codecompiler/submit";
    var serverProgramStatusEndpoint = "/codecompiler/status";
    var serverRecentSubmissionEndpoint = "/codecompiler/recent";

    var serverProgramSubmitURL = "http://" + serverHost + ":" + serverPort + serverSubmitEndpoint;
    var serverRecentSubmissionURL = "http://" + serverHost + ":" + serverPort + serverRecentSubmissionEndpoint;
    var serverProgramStatusURL = "http://" + serverHost + ":" + serverPort + serverProgramStatusEndpoint;

    return {
        "serverProgramSubmitURL": serverProgramSubmitURL,
        "serverRecentSubmissionURL": serverRecentSubmissionURL,
        "serverProgramStatusURL": serverProgramStatusURL
    };
}());