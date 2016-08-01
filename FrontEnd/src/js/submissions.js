var submissions = (function() {
    var serverHost = "localhost";
    var serverPort = "8081";
    var serverRecentSubmissionEndpoint = "/codecompiler/recent";
    var serverRecentSubmissionURL = "http://" + serverHost + ":" + serverPort + serverRecentSubmissionEndpoint;
    $.fn.dataTable.moment("M/D/YYYY H:mm");
    
    var transformAjaxData = function(data) {
        var result = [];
        data.data.submissions.forEach(function(item) {
            var row = [];
            row.push(item.queueId);
            row.push(item.status);
            row.push(moment(item.submittedTime).format("M/D/YYYY H:mm"));
            result.push(row);
        });
        return result;
    };
    
    var drawData = function(data) {
        var result = transformAjaxData(data);
        $('#submissions').DataTable({
            data: result,
            columns: [
                {"title": "Queue Id"},
                {"title": "Status"},
                {"title": "Submitted At"}
            ],
            "order": [[ 2, "desc" ]]
        });
    };

    var loadDataForPage = function(page) {
        $.ajax({
            "url": serverRecentSubmissionURL,
            "method": "GET",
            "data": {
                "pageNumber": page,
                "rowSize": 100
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
    };
}());