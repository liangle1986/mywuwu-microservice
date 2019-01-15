package com.mywuwu.annotation;

import java.lang.annotation.*;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/1/15 15:04
 * @Description:
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    boolean required() default true;
}
