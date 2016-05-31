package com.codecompiler.services;

import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/30/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CodeCompilerServiceTest {
    @Mock private ProgramRepository programRepository;

    private CodeCompilerService codeCompilerService;

    @Before
    public void init() {
        this.codeCompilerService = new CodeCompilerService(this.programRepository);
        Mockito.when(programRepository.findByQueueId("1")).thenReturn(mockProgramEntity());
        Mockito.when(programRepository.findByQueueId("0")).thenReturn(null);
    }

    @Test
    public void testValidCheckProgramStatus() {
        Response<ProgramStatusResponse> response = codeCompilerService.checkProgramStatus("1");
        Assert.assertEquals(response.getData().getSubmittedDate().getTime(), 1464552167);
        Assert.assertEquals(response.getData().getProgramStatus(), ProgramStatusResponse.PROGRAM_IN_QUEUE_FOR_COMPILATION);
    }

    @Test
    public void testGenerateUniqueId() {
        String uniqueId1 = codeCompilerService.generateUniqueID();
        String uniqueId2 = codeCompilerService.generateUniqueID();

        Assert.assertNotEquals(uniqueId1, uniqueId2);
    }

    @Test
    public void testInvalidCheckProgramStatus() {
        Response<ProgramStatusResponse> response = codeCompilerService.checkProgramStatus("0");
        Assert.assertEquals(response.getData().getProgramStatus(), ProgramStatusResponse.PROGRAM_NOT_FOUND);
    }

    @Test
    public void testSubmitProgram() {
        Response<ProgramSubmitResponse> response = codeCompilerService.submitProgram("program", "input");
        Assert.assertNotNull(response.getData());
    }


    private ProgramEntity mockProgramEntity() {
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setQueueId("abcdefgh");
        programEntity.setQueuedTime(new Date(1464552167));
        programEntity.setExecutionTimeLimit(2);
        programEntity.setInput("INPUT");
        programEntity.setErrorMessage(null);
        programEntity.setId("1");
        programEntity.setOutput(null);
        programEntity.setProgram("PROGRAM");
        programEntity.setProgramStatus(ProgramStatusResponse.PROGRAM_IN_QUEUE_FOR_COMPILATION);
        return programEntity;
    }
}
