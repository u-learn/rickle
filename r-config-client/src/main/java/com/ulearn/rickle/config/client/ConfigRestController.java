package com.ulearn.rickle.config.client;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RickleProperties rickleProperties;

    @RequestMapping("/mongo")
    public String getMongoConfigDetails() {
//        return "mongo://" + host + ":" + port + "/" + mongodb + "?connectionPool=" + pool + "&ssl=" + ssl;
        return "mongo://" + rickleProperties.getHost() + ":" + rickleProperties.getPort() + "/" + rickleProperties.getMongodb()
                + "?connectionPool=" + rickleProperties.getPool() + "&ssl=" + rickleProperties.isSsl();
    }

    @RequestMapping("/greet/{user}")
    public String greetUser(@PathVariable("user") String user) {
        return rickleProperties.getGreeting() + " " + user;
    }


}