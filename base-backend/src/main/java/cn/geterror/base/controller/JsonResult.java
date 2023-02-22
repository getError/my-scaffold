package com.sankuai.blue.infra.web;

import com.meituan.servicecatalog.api.annotations.FieldDoc;
import com.meituan.servicecatalog.api.annotations.TypeDoc;
import com.sankuai.blue.feature.common.base.IJsonResult;

/**
 * @author zhanghongyu
 */
@TypeDoc(
        description = "通用返回结果对象"
)
public class JsonResult {

    public final static int SUCCESS_CODE = 0;

    public final static String SUCCESS_MESSAGE = "ok";

    @FieldDoc(
            description = "返回值代码",
            defaultValue = "0"
    )
    private int code = SUCCESS_CODE;

    @FieldDoc(
            description = "返回值信息",
            defaultValue = "ok"
    )
    private String message = SUCCESS_MESSAGE;

    @FieldDoc(
            description = "返回数据JSON对象",
            defaultValue = "{}"
    )
    private Object data;

    public JsonResult() {
    }

    public JsonResult(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public JsonResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult data(Object data) {
        this.data = null != data ? data : IJsonResult.EMPTY;
        return this;
    }
}
