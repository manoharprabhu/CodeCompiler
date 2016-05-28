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

    public CodeCompilerController(CodeCompilerService codeCompilerService) {
        this.codeCompilerService = codeCompilerService;
    }

    public CodeCompilerController() {}

    @RequestMapping(method = RequestMethod.POST, path = "/submit")
    public Response submitProgram(@RequestBody String program, @RequestBody String input) {
        return codeCompilerService.submitProgram(program, input);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/status")
    public Response checkProgramStatus(@RequestParam String programId) {
        return codeCompilerService.checkProgramStatus(programId);
    }

}
