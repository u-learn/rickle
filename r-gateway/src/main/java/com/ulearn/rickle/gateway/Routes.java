package com.ulearn.rickle.gateway;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sunand on 09/04/18.
 */
@Document
@Data
public class Routes {
  private String name;
  private String context;
  private String from;
  private String to;
}
