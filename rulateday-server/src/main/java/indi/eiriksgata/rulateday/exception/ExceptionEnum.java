package indi.eiriksgata.rulateday.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ExceptionEnum {

    // 系统错误的指令
    DND5E_MM_NAME_DECODE_ERR(10001, "怪物名称编码出错"),

    //数据库连接异常
    DB_LINK_ERROR(10002, "数据库链接异常"),

    // system predefine
    WARING(4, "Your operation may be abnormal."),

    ATTONITY(3, "No operation content."),

    VERIFIED(2, "Waiting for your verification."),

    SUCCESS(0, "Operation Success."),

    ERROR(-1, "System Error."),

    UNKNOWN(-2, "Unknown Exception.");

    private final Integer errCode;
    private final String errMsg;

    /**
     * @param errCode 系统号(2bit) + 业务模块（2bit）+ 业务异常（2bit）
     * @param errMsg  异常消息
     */
    ExceptionEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    /**
     * find exception enum by err code
     *
     * @param errCode error code
     * @return exception enum
     */
    public static indi.eiriksgata.dice.exception.ExceptionEnum getExceptionEnumByCode(Integer errCode) {
        for (indi.eiriksgata.dice.exception.ExceptionEnum exceptionEnum : indi.eiriksgata.dice.exception.ExceptionEnum.values()) {
            if (exceptionEnum.getErrCode().equals(errCode)) {
                return exceptionEnum;
            }
        }
        return null;
    }
}
