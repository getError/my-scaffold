package cn.geterror.base.annotation;

import org.springframework.web.bind.annotation.ValueConstants;

/**
 * @author hanyecong02
 */
public @interface ResultCode {

    int code() default Integer.MIN_VALUE;

    String message() default ValueConstants.DEFAULT_NONE;
}
