/*globals ace:false */
(function() {
	"use strict";
	var editor = ace.edit("editor");
	ace.config.set("basePath", "/js");
	editor.setTheme("ace/theme/twilight");
    editor.getSession().setMode("ace/mode/c_cpp");
}());
