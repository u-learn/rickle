package com.ulearn.rickle.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Sunand on 11/04/18.
 */
@Data
public class AuditableEntity {
  private String createdBy;
  private LocalDateTime createdDate;
  private String modifiedBy;
  private LocalDateTime modifiedDate;

  private LocalDateTime sysModDate;
}
