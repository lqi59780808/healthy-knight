package com.chh.healthy.backend.pojo.code;
/**
 * @file:  ExamErrorCode.java
 * @author: chh
 * @date:   2020/9/15 9:29
 * @copyright: 2020-2023 www.bosssoft.com.cn Inc. All rights reserved.
 */
import com.boss.xtrain.core.common.exception.code.IErrorCode;

/**
 * @class ExamErrorCode
 * @classdesc 考试服务的错误码
 * @author chh
 * @date 2020/9/15  9:29
 * @version 1.0.0
 * @see
 * @since
 */
public enum ErrorCode implements IErrorCode {
    /**
     * 200001考试发布记录新增失败
     */
    REGISTER_EXCEPTION(200001,"注册失败，检查注册信息是否规范"),
    /**
     * 200002考试发布记录新增失败
     */
    REGISTER_REPEAT_EXCEPTION(200002,"注册失败，用户名重复"),
    /**
     * 在以上增加其它错误码
     */
    UNDEFINED(900000, "未定义");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param: code
     * @return: 错误信息
     * @see
     * @since
     */
    public static String msg(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode.message;
            }
        }
        return UNDEFINED.message;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
