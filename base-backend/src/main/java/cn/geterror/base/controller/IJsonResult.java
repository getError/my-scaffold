package com.sankuai.blue.feature.common.base;

import com.sankuai.blue.infra.web.JsonResult;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public interface IJsonResult {

    Map EMPTY = Collections.emptyMap();

    default JsonResult ok() {
        return new JsonResult(0, EMPTY);
    }

    default JsonResult okWithItems(List<?> items) {
        return ok().data(Collections.singletonMap("items", Optional.ofNullable(items).orElse(Collections.emptyList())));
    }

    default JsonResult ok(String message) {
        return new JsonResult(0, message, EMPTY);
    }

    default JsonResult fail(int code, Object data) {
        return new JsonResult(code, data);
    }

    default JsonResult fail(int code, Object data, String message) {
        return new JsonResult(code, message, data);
    }

    default JsonResult fail(int code, String message) {
        return new JsonResult(code, message, null);
    }

    default JsonResult badRequest(String message) {
        return new JsonResult(BAD_REQUEST.value(), message, EMPTY);
    }

    default JsonResult notFound(String message) {
        return new JsonResult(NOT_FOUND.value(), message);
    }

    default JsonResult unauthorized() {
        return new JsonResult(UNAUTHORIZED.value(), null);
    }

    default JsonResult internalError(Object data) {
        return new JsonResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), data);
    }

}
