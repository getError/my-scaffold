package cn.geterror.base.exception;

/**
 * 用于业务参数或者数据上的错误
 */
public class BusinessException extends RuntimeException {

    private Integer errorCode;

    private String errorMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode = -1;
        this.errorMsg = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = -1;
        this.errorMsg = message;
    }

    public BusinessException(String message, Object... args) {
        super(String.format(message, args));
        this.errorCode = -1;
        this.errorMsg = String.format(message, args);
    }

    public BusinessException(String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
        this.errorCode = -1;
        this.errorMsg = String.format(message, args);
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
