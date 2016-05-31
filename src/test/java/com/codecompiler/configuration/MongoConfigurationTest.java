package com.codecompiler.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoConfigurationTest {
    @Test
    public void testValidMongoConfiguration() throws Exception {
        MongoConfiguration mongoConfiguration = new MongoConfiguration();
        MongoConfiguration.HOST = "2.3.4.5";
        Assert.assertEquals(mongoConfiguration.getDatabaseName(), "codecompiler");
        Assert.assertEquals(mongoConfiguration.mongo().getClass(), MongoClient.class);
    }

}
