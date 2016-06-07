package com.codecompiler.controllers;

import com.codecompiler.services.CodeCompilerService;
import com.codecompiler.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@RestController
@RequestMapping("/codecompiler")
public class CodeCompilerController {

    @Autowired
    private CodeCompilerService codeCompilerService;

    @RequestMapping(method = RequestMethod.POST, path = "/submit")
    public Response submitProgram(@RequestParam String program, @RequestParam String input) {
        return codeCompilerService.submitProgram(program, input);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/status")
    public Response checkProgramStatus(@RequestParam String queueId) {
        return codeCompilerService.checkProgramStatus(queueId);
    }

}
