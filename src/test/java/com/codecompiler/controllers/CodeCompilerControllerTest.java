package com.codecompiler.controllers;

import com.codecompiler.services.CodeCompilerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CodeCompilerControllerTest {

    @Mock
    private CodeCompilerService codeCompilerService;

    @InjectMocks
    private CodeCompilerController codeCompilerController = new CodeCompilerController();

    @Test
    public void testSubmitProgram() {
        codeCompilerController.submitProgram("program", "input", 2, "c");
        Mockito.verify(codeCompilerService, Mockito.times(1)).submitProgram("program", "input", 2 ,"c");
    }

    @Test
    public void testCheckProgramStatus() {
        codeCompilerController.checkProgramStatus("queueId");
        Mockito.verify(codeCompilerService, Mockito.times(1)).checkProgramStatus("queueId");
    }
}
