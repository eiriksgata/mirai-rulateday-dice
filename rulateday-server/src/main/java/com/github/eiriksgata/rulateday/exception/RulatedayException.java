package com.github.eiriksgata.rulateday.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
//改成必须捕捉异样并处理，如果不符合指令的全部抛出异常，用于用户处理
public class RulatedayException extends Exception {

    private static final long serialVersionUID = -6301630219349537830L;

    private Integer errCode;
    private String errMsg;

    public RulatedayException(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public RulatedayException(String errMsg) {
        this.errCode = ExceptionEnum.UNKNOWN.getErrCode();
        this.errMsg = errMsg;
    }

    public RulatedayException(ExceptionEnum exceptionEnum) {
        this.errCode = exceptionEnum.getErrCode();
        this.errMsg = exceptionEnum.getErrMsg();
    }

    public RulatedayException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(cause);
        this.errCode = exceptionEnum.getErrCode();
        this.errMsg = exceptionEnum.getErrMsg();
    }


}
