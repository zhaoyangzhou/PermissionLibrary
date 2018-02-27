
package com.zzy.learn.aspectj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

 /**
  * Package: com.zzy.learn.aspectj.annotation
  * Class: DebugTrace
  * Description: 调试注解
  * Author: zhaoyangzhou
  * Email: zhaoyangzhou@126.com
  * Created on: 2017/12/18 18:26
  */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface DebugTrace {}
