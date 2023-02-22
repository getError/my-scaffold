package com.sankuai.blue.infra.web;

import com.google.common.reflect.TypeToken;
import com.sankuai.blue.infra.annotation.RawResult;
import com.sankuai.blue.infra.annotation.ResultCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author hanyecong02
 */
@ControllerAdvice
@SuppressWarnings("UnstableApiUsage")
public class JsonResultAdvice implements ResponseBodyAdvice<Object> {

    private static final TypeToken<?> supportConvertorToken = new TypeToken<HttpMessageConverter<? super JsonResult>>() {};

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

        if (!supportConvertorToken.isSupertypeOf(converterType)) {
            return false;
        }

        if (returnType.getMethodAnnotation(RawResult.class) != null ||
                returnType.getContainingClass().getAnnotation(RawResult.class) != null) {
            return false;
        }

        return !JsonResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        int code = JsonResult.SUCCESS_CODE;
        String message = JsonResult.SUCCESS_MESSAGE;

        ResultCode resultCode = returnType.getMethodAnnotation(ResultCode.class);

        if (resultCode != null) {

            if (Integer.MIN_VALUE != resultCode.code()) {
                code = resultCode.code();
            }

            if (!ValueConstants.DEFAULT_NONE.equals(resultCode.message())) {
                message = resultCode.message();
            }
        }

        return new JsonResult(code, message, body);
    }
}
