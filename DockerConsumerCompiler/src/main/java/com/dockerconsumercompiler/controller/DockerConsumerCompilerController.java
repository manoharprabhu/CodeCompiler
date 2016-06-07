package com.dockerconsumercompiler.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Manohar Prabhu on 6/7/2016.
 */
@RestController
@RequestMapping("/dockerconsumercompiler")
public class DockerConsumerCompilerController {
    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public boolean check() {
        return true;
    }
}
