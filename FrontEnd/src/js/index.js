/*globals ace:false */
var app = (function() {
    "use strict";
    var programEditor;
    var inputEditor;
    var serverHost = "localhost";
    var serverPort = "8081";
    var serverSubmitEndpoint = "/codecompiler/submit";
    var serverProgramStatusEndpoint = "/codecompiler/status";
    var serverProgramSubmitURL = "http://" + serverHost + ":" + serverPort + serverSubmitEndpoint;
    var $programLanguage = $("#program-language");
    var ladda = Ladda.create(document.querySelector(".submit-button"));

    var setEditorLanguage = function(language) {
        if (language === "c") {
            programEditor.getSession().setMode("ace/mode/c_cpp");
        } else if (language === "js") {
            programEditor.getSession().setMode("ace/mode/javascript");
        }
    };

    var getSelectedLanguage = function() {
        return $programLanguage.find("option:selected").val();
    }

    var initializeEditor = function() {
        ace.config.set("basePath", "/js");
        programEditor = ace.edit("program-editor");
        programEditor.setTheme("ace/theme/terminal");
        inputEditor = ace.edit("input-editor");
        inputEditor.setTheme("ace/theme/terminal");
        $programLanguage.on("change", function() {
            setEditorLanguage(getSelectedLanguage());
        });
        $programLanguage.trigger("change");
    };

    var programSubmitSuccess = function(data) {
        ladda.stop();
        sweetAlert({
            title: "Success",
            type: "success",
            text: "Your program ID is <br /><strong>" + data.data.queueId + "</strong>",
            html: true
        }, function() {
            window.location = "/result.html?queueId=" + data.data.queueId;
        });
    };

    var programSubmitFailure = function() {
        ladda.stop();
        sweetAlert("Error", "Could not submit your request. Please try again later.", "error");
    };

    var validateInputs = function() {
        var programText = programEditor.getValue().trim();
        if (programText === "") {
            return false;
        }
        return true;
    };

    var submitProgramToServer = function() {
        if (!validateInputs()) {
            sweetAlert("Error", "You forgot to write the program.", "error");
            return;
        }
        var selectedLanguage = getSelectedLanguage();
        ladda.start();
        $.ajax({
            url: serverProgramSubmitURL,
            method: "POST",
            data: {
                program: programEditor.getValue(),
                input: inputEditor.getValue(),
                timeout: 2,
                language: selectedLanguage
            },
        }).done(function(data) {
            setTimeout(function() { programSubmitSuccess(data); }, 1000);
        }).fail(function() {
            setTimeout(function() { programSubmitFailure(); }, 1000);
        });
    };

    initializeEditor();

    return {
        "initializeEditor": initializeEditor,
        "submitProgramToServer": submitProgramToServer
    };
}());