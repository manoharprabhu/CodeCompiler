package com.codecompiler.services;

import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@Service
public class CodeCompilerService {

    private ProgramRepository programRepository;

    public CodeCompilerService() {

    }

    @Autowired
    public CodeCompilerService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Response<ProgramSubmitResponse> submitProgram(String program, String input) {
        ProgramEntity programEntity = new ProgramEntity();
        ProgramSubmitResponse response;
        String uniqueId = generateUniqueID();
        programEntity.setProgram(program);
        programEntity.setInput(input);
        programEntity.setExecutionTimeLimit(2);
        programEntity.setQueuedTime(new Date());
        programEntity.setQueueId(uniqueId);
        programRepository.save(programEntity);
        response = new ProgramSubmitResponse(uniqueId);
        return new Response<ProgramSubmitResponse>(response);
    }

    public Response<ProgramStatusResponse> checkProgramStatus(String queueId) {
        ProgramEntity programEntity = programRepository.findByQueueId(queueId);
        ProgramStatusResponse response;
        if(programEntity == null) {
            response = new ProgramStatusResponse(ProgramStatusResponse.PROGRAM_NOT_FOUND, null, "Program not found", null);
        } else {
            response = new ProgramStatusResponse(programEntity.getProgramStatus(), programEntity.getOutput(), programEntity.getErrorMessage(), programEntity.getQueuedTime());
        }
        return new Response<ProgramStatusResponse>(response);
    }

    public String generateUniqueID() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString();
    }

}
