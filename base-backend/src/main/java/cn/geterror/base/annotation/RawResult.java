package com.sankuai.blue.infra.annotation;

import java.lang.annotation.*;

/**
 * @author hanyecong02
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RawResult {
}
