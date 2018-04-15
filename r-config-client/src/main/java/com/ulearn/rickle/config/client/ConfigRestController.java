package com.ulearn.rickle.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sunand on 14/04/18.
 */
@RefreshScope
@RestController
@RequestMapping(("/config"))
class ConfigRestController {

    @Value("${root.mongo.db}")
    private String mongodb;
    @Value("${root.mongo.host}")
    private String host;
    @Value("${root.mongo.port}")
    private int port;
    @Value("${root.mongo.connection.pool}")
    private int pool;
    @Value("${root.mongo.ssl.enable}")
    private boolean ssl;

    @Value("${greeting}")
    private String greeting;

    @RequestMapping("/mongo")
    public String getMongoConfigDetails() {
        return "mongo://" + host + ":" + port + "/" + mongodb + "?connectionPool=" + pool + "&ssl=" + ssl;
    }

    @RequestMapping("/greet/{user}")
    public String greetUser(@PathVariable("user") String user) {
        return greeting + " " + user;
    }


}