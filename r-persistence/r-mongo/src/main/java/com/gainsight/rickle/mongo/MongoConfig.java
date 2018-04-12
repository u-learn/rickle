package com.gainsight.rickle.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Created by Sunand on 12/04/18.
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.gainsight.rickle", repositoryBaseClass = CustomReactiveMongoRepository.class)
public class MongoConfig {
}
