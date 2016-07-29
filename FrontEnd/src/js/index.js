/*globals ace:false */
var app = (function() {
	"use strict";
    var programEditor;
    var inputEditor;
    var serverHost = "localhost";
    var serverPort = "8080";
    var serverSubmitEndpoint = "/codecompiler/submit";
    var serverProgramStatusEndpoint = "/codecompiler/status";
    var serverProgramSubmitURL = "http://" + serverHost + ":" + serverPort + serverSubmitEndpoint;
    var $programLanguage = $("#program-language");

    var setEditorLanguage = function(language) {
        if(language === "c") {
            programEditor.getSession().setMode("ace/mode/c_cpp");
        } else if(language === "js") {
            programEditor.getSession().setMode("ace/mode/javascript");
        }
    };

    var getSelectedLanguage = function() {
        return $programLanguage.find('option:selected').val();
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

    };

    var programSubmitFailure = function() {

    };

    var submitProgramToServer = function() {
        var selectedLanguage = getSelectedLanguage();
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
            programSubmitSuccess(data);
        }).fail(function() {
            programSubmitFailure();
        });
    };

    initializeEditor();

    return {
        "submitProgramToServer": submitProgramToServer 
    };
}());