var submissions = (function() {
    var serverHost = "localhost";
    var serverPort = "8081";
    var serverRecentSubmissionEndpoint = "/codecompiler/recent";
    var serverRecentSubmissionURL = "http://" + serverHost + ":" + serverPort + serverRecentSubmissionEndpoint;
    var $submissionsWrapper = $("#submissions-wrapper");
    
    var drawData = function(data) {
        $submissionsWrapper.empty();
        /*TODO: Fill datatable here*/
    };

    var loadDataForPage = function(page) {
        $.ajax({
            "url": serverRecentSubmissionURL,
            "method": "GET",
            "data": {
                "pageNumber": page,
                "rowSize": 10
            }
        }).done(function(data) {
            drawData(data);
        })
        .fail(function() {
            alert("Error while fetching the recent submissions");
        });
    };

    return {
        "loadDataForPage": loadDataForPage
    }
}());