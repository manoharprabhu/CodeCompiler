package com.codecompiler.services;

import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.Response;
import org.springframework.stereotype.Service;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@Service
public class CodeCompilerService {
    public Response<ProgramSubmitResponse> submitProgram(String program, String input) {
        return Response.createEmptyResponse();
    }

    public Response<ProgramStatusResponse> checkProgramStatus(String programId) {
        return Response.createEmptyResponse();
    }
}
