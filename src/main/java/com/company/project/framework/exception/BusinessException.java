package com.company.project.framework.exception;


import com.company.project.framework.exception.code.ResponseCodeInterface;

public class BusinessException extends RuntimeException {

    /**
     * 异常编号
     */
    private final int code;

    /**
     * 对messageCode 异常信息进行补充说明
     */
    private final String detailMessage;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.detailMessage = message;
    }
    /**
     * 构造函数
     * @param code 异常码
     */
    public BusinessException(ResponseCodeInterface code) {
        this(code.getCode(), code.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

}
