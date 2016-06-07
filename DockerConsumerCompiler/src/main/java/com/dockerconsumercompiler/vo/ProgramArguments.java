package com.dockerconsumercompiler.vo;

import org.kohsuke.args4j.Option;

/**
 * Created by Manohar Prabhu on 6/7/2016.
 */
public class ProgramArguments {
    @Option(name = "-rmqhost", usage = "Address of RabbitMQ server", required = true)
    public String rmqHost;
    @Option(name = "-mongohost", usage = "Address of monogo DB", required = true)
    public String mongodbHost;
    @Option(name = "-mongodatabase", usage = "Database name", required = true)
    public String mongodbDatabase;
}
