package com.codecompiler.controllers;

import com.codecompiler.ProgramStatusResponse;
import com.codecompiler.ProgramSubmitResponse;
import com.codecompiler.Response;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@RestController
@RequestMapping("/codecompiler")
public class CodeCompilerController {

    @RequestMapping(method = RequestMethod.POST, path = "/submit")
    public Response submitProgram(@RequestBody String program, @RequestBody String imput) {
        return Response.createEmptyResponse();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/status")
    public Response checkProgramStatus(@RequestParam String programId) {
        return Response.createEmptyResponse();
    }

}
