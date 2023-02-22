package cn.geterror.base.controller;




public class JsonResult{

    public final static int SUCCESS_CODE = 0;

    public final static String SUCCESS_MESSAGE = "ok";

    private int code = SUCCESS_CODE;


    private String message = SUCCESS_MESSAGE;


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
