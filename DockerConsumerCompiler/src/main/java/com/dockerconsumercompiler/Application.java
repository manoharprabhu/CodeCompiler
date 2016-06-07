package com.dockerconsumercompiler;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 6/6/2016.
 */
@SpringBootApplication
public class Application {
    private static Logger logger = Logger.getLogger(Application.class.toString());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
