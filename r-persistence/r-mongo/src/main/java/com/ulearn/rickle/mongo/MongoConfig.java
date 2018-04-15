package com.ulearn.rickle.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Created by Sunand on 12/04/18.
 */
@Configuration
@EnableMongoAuditing
@EnableReactiveMongoRepositories(basePackages = "com.ulearn.rickle", repositoryBaseClass = CustomReactiveMongoRepository.class)
public class MongoConfig {
}
