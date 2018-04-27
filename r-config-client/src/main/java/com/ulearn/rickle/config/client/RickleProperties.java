package com.ulearn.rickle.config.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by Sunand on 26/04/18.
 */
//@Configuration
@RefreshScope
@Component
@Data
public class RickleProperties {
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
}
