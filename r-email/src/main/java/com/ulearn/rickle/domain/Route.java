package com.ulearn.rickle.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 12/04/18.
 */
@Data
@Document(collection = "routes")
public class Route {
  @Id
  private String id;
  private String name;
  private String context;
  private String from;
  private String to;
}
