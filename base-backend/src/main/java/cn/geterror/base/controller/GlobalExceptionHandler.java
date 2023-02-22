package com.sankuai.blue.infra.web;

import com.dianping.cat.Cat;
import com.meituan.mtrace.Tracer;
import com.sankuai.blue.infra.exception.BusinessException;
import com.sankuai.blue.infra.exception.ContentBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.ServletException;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ContentBusinessException.class)
    public JsonResult contentBusinessException(ContentBusinessException e) {

        logger.error("ContentBusinessException: " + e.getBody(), e);
        Cat.logError("ContentBusinessException: " + e.getBody() + ", message: " + e.getMessage(), e);
        return new JsonResult(e.getErrorCode(), e.getErrorMsg(), e.getBody());
    }

    @ExceptionHandler(value = BusinessException.class)
    public JsonResult businessExceptionHandler(BusinessException e) {
        logger.error("BusinessException: ", e);
        Cat.logError(e.getMessage(), e);
        return new JsonResult(e.getErrorCode(), e.getErrorMsg(), null);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public JsonResult illegalArgumentExceptionHandler(Exception e) {
        logger.error("IllegalArgumentException: ", e);
        Cat.logEvent("非法参数异常", e.getMessage());
        return new JsonResult(-2, e.getMessage(), null);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public JsonResult maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {

        logger.error("MaxUploadSizeExceededException: ", e);
        Cat.logError(e.getMessage(), e);

        String message = "上传文件过大";
        if (e.getMaxUploadSize() > 0) {
            message += "，超过" + e.getMaxUploadSize() + "B";
        }

        return new JsonResult(-1, message, null);
    }

    @ExceptionHandler(ServletException.class)
    public JsonResult servletExceptionHandler(ServletException e) {
        return new JsonResult(-1, "请求错误: " + e.getLocalizedMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult argExceptionHandler(MethodArgumentNotValidException e) {
        return new JsonResult(-1, "参数异常: " + e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("、")), null);
    }

    @ExceptionHandler(value = Exception.class)
    public JsonResult exceptionHandler(Exception e) {
        logger.error("Exception: ", e);
        Cat.logError(e.getMessage(), e);

        String traceId = Tracer.id();
        String traceTail = !StringUtils.isEmpty(traceId) ? ", traceId: " + traceId : "";

        return new JsonResult(-1, "系统异常" + traceTail, null);
    }
}
