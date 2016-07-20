package com.dockerconsumercompiler.configuration;

import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Manohar Prabhu on 7/20/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoConfigurationTest {
    @Test
    public void testValidMongoConfiguration() throws Exception {
        MongoConfiguration mongoConfiguration = new MongoConfiguration();
        MongoConfiguration.HOST = "2.3.4.5";
        MongoConfiguration.DATABASE_NAME = "codecompiler";
        Assert.assertEquals(mongoConfiguration.getDatabaseName(), "codecompiler");
        Assert.assertEquals(mongoConfiguration.mongo().getClass(), MongoClient.class);
    }

}
