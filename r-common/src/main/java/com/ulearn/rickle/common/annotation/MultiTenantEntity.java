/*
 * Copyright (c) 2016 - 2017, Gainsight.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Gainsight.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with Gainsight.
 *
 * http://www.gainsight.com
 */

package com.ulearn.rickle.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Sunand on 9/7/2016.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface MultiTenantEntity {
  /**
   * (Optional) Specify the multi-tenant strategy to use.
   */
  MultiTenantEntityType value() default MultiTenantEntityType.SINGLE;

}
