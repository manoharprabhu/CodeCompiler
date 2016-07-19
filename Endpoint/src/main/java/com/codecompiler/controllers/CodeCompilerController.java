package com.codecompiler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codecompiler.services.CodeCompilerService;
import com.codecompiler.vo.Response;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@RestController
@RequestMapping("/codecompiler")
public class CodeCompilerController {

    @Autowired
    private CodeCompilerService codeCompilerService;

    @RequestMapping(method = RequestMethod.POST, path = "/submit")
    public Response submitProgram(@RequestParam String program, @RequestParam String input, @RequestParam int timeout, @RequestParam String language) {
        return codeCompilerService.submitProgram(program, input, timeout, language);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/status")
    public Response checkProgramStatus(@RequestParam String queueId) {
        return codeCompilerService.checkProgramStatus(queueId);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/recent")
    public Response getRecentSubmissions(@RequestParam int pageNumber, @RequestParam int rowSize) {
    	return codeCompilerService.getRecentSubmissions(pageNumber, rowSize);
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/ping")
    public String ping() {
    	return "pong";
    }

}
