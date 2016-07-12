package com.codecompiler;

import com.codecompiler.configuration.MongoConfiguration;
import com.codecompiler.vo.ProgramArguments;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.args4j.CmdLineException;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Test
    public void testArgumentParsing() throws CmdLineException {
        ProgramArguments programArguments = Application.parseArguments(new String[]{"-mongohost","127.0.0.1","-mongodatabase","codecompiler"});
        Assert.assertEquals(programArguments.mongodbDatabase, "codecompiler");
        Assert.assertEquals(programArguments.mongodbHost, "127.0.0.1");
    }

    @Test(expected = CmdLineException.class)
    public void testInvalidArgumentParsing() throws CmdLineException {
        ProgramArguments programArguments = Application.parseArguments(new String[]{"-invalid","127.0.0.1","-mongodatabase","codecompiler"});
    }

    @Test
    public void testSetProgramParams() {
        ProgramArguments arguments = new ProgramArguments();
        arguments.mongodbDatabase = "database";
        arguments.mongodbHost = "mongohost";

        Application.setProgramParameters(arguments);
        Assert.assertTrue(MongoConfiguration.DATABASE_NAME.equals(arguments.mongodbDatabase));
        Assert.assertTrue(MongoConfiguration.HOST.equals(arguments.mongodbHost));
    }
}
