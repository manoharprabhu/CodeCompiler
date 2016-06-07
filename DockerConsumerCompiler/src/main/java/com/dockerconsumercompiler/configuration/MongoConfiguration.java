package com.dockerconsumercompiler.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Manohar Prabhu on 5/30/2016.
 */
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

    public static String DATABASE_NAME = "codecompiler";
    public static String HOST = "127.0.0.1";

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(HOST);
    }

}
