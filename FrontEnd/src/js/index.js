/*global ace, sweetAlert, CODECOMPILER_CONFIG, Ladda */
var app = (function() {
    "use strict";
    var programEditor;
    var inputEditor;
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
    };

    var initializeEditor = function() {
        sweetAlert({
            "title": "Loading",
            "type": "info",
            "text": "Please wait...",
            "showConfirmButton": false
        });
        ace.config.set("basePath", "js");
        programEditor = ace.edit("program-editor");
        programEditor.setTheme("ace/theme/terminal");
        inputEditor = ace.edit("input-editor");
        inputEditor.setTheme("ace/theme/terminal");
        $programLanguage.on("change", function() {
            setEditorLanguage(getSelectedLanguage());
        });
        $programLanguage.trigger("change");

        programEditor.renderer.on("afterRender", function() {
            setTimeout(function() { sweetAlert.close() }, 1000);
        });
    };

    var programSubmitSuccess = function(data) {
        ladda.stop();
        sweetAlert({
            title: "Success",
            type: "success",
            text: "Your program ID is <br /><strong>" + data.data.queueId + "</strong>",
            html: true
        }, function() {
            window.location = "result.html?queueId=" + data.data.queueId;
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
            url: CODECOMPILER_CONFIG.serverProgramSubmitURL,
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
