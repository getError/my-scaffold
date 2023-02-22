package cn.geterror.base.exception;

import lombok.Getter;

/**
 * @author hanyecong02
 */
public class ContentBusinessException extends BusinessException {

    @Getter
    private Object body;

    public ContentBusinessException(Integer code, String message, Object body) {
        super(message);
        setErrorCode(code);
        this.body = body;
    }

    @Override
    public String toString() {
        return "ContentBusinessException："+getErrorMsg() +
                "content=" + getBody() +
                '}';
    }
}
