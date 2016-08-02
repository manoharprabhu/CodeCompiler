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
            row.push("");
            result.push(row);
        });
        return result;
    };

    var drawData = function(data) {
        var result = transformAjaxData(data);
        $('#submissions').DataTable({
            data: result,
            columns: [
                { "title": "Queue Id" },
                { "title": "Status" },
                { "title": "Submitted At" },
                { "title": "Output" }
            ],
            "columnDefs": [{
                "className": "dt-body-center",
                "targets": [0, 1, 2, 3]
            }],
            "order": [
                [2, "desc"]
            ],
            "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                var programResultPageURL = "/result.html?queueId=" + aData[0];
                if (aData[1] === 6) {
                    $("td:eq(3)", nRow).html("<a href='" + programResultPageURL + "' class='valid-output'>View Result</a>");
                } else if (aData[1] === 0 || aData[1] === 3 || aData[1] === 4 || aData[1] === 5) {
                    $("td:eq(3)", nRow).html("<a href='" + programResultPageURL + "' class='error-output'>View Result</a>");
                }

                if (aData[1] === 6) {
                    $("td:eq(1)", nRow).html("<span class='glyphicon glyphicon-ok tick-green'></span>");
                } else if (aData[1] === 1 || aData[1] === 2) {
                    $("td:eq(1)", nRow).html("<span class='glyphicon glyphicon-time'></span>");
                } else if (aData[1] === 3 || aData[1] === 4 || aData[1] === 5) {
                    $("td:eq(1)", nRow).html("<span class='glyphicon glyphicon-remove cross-red'></span>");
                } else {
                    $("td:eq(1)", nRow).html("<span class='glyphicon glyphicon-warning-sign warning-yellow'></span>");
                }
            }
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
                sweetAlert({
                        title: "Error",
                        text: "Error while getting the recent submissions. Please try again later.",
                        type: "error",
                        confirmButtonText: "Reload",
                        showCancelButton: true
                    },
                    function() {
                        window.location = "/submissions.html";
                    });
            });
    };

    return {
        "loadDataForPage": loadDataForPage
    };
}());