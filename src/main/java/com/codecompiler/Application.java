package com.codecompiler;

import com.codecompiler.configuration.MongoConfiguration;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 5/27/2016.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    private static Logger logger = Logger.getLogger(Application.class.toString());

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("mongohost", "Hostname of MongoDB server");
        options.addOption("mongodatabase", "Database to use");

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine command = null;
        try {
            command = commandLineParser.parse(options, args);
        } catch(ParseException e) {
            logger.info("Invalid option passed.");
            return;
        }

        if(command.hasOption("mongohost")) {
            MongoConfiguration.HOST = command.getOptionValue("mongohost");
        }

        if(command.hasOption("mongodatabase")) {
            MongoConfiguration.DATABASE_NAME = command.getOptionValue("mongodatabase");
        }

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
