package com.seasonfif.swiftmodule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间：2017年05月26日19:18 <br>
 * 作者：zhangqiang <br>
 * 描述：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetName {
  String value();
}
