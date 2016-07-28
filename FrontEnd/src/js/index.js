/*globals ace:false */
(function() {
	"use strict";
    var editor;
    
    var setEditorLanguage = function(language) {
        if(language === "c") {
            editor.getSession().setMode("ace/mode/c_cpp");
        }
    };
    
    var initializeEditor = function() {
        ace.config.set("basePath", "/js");
        editor = ace.edit("editor");
        editor.setTheme("ace/theme/twilight");   
        setEditorLanguage("c");
    };
    
    initializeEditor();
}());