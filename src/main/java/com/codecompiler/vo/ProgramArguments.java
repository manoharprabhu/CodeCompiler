package com.codecompiler.vo;

import org.kohsuke.args4j.Option;

/**
 * Created by Manohar Prabhu on 5/30/2016.
 */
public class ProgramArguments {
    @Option(name = "-mongohost", usage = "Address of the MongoDB Server")
    public String mongodbHost = "127.0.0.1";

    @Option(name = "-mongodatabase", usage = "Database name")
    public String mongodbDatabase = "codecompiler";

    @Option(name = "-rmqhost", usage = "Address of RabbitMQ server")
    public String rmqHost = "localhost";
}
