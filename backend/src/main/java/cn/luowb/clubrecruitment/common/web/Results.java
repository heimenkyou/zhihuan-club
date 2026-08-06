package cn.luowb.clubrecruitment.common.web;

import cn.luowb.clubrecruitment.common.errorcode.BaseErrorCode;
import cn.luowb.clubrecruitment.common.exception.AbstractException;
import cn.luowb.clubrecruitment.common.result.Result;

import java.util.Optional;

/**
 * 构建全局返回对象构造器｜方便开发者构建全局返回对象
 */
public final class Results {

    /**
     * 构造成功响应
     */
    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    /**
     * 构建服务端失败响应
     */
    public static Result<Void> failure() {
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     */
    protected static Result<Void> failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    protected static Result<Void> failure(String errorCode, String errorMessage) {
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 构建带附加数据的失败响应，用于前端需要根据错误码定位关联数据的场景
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param data         附加数据
     * @param <T>          附加数据类型
     * @return 失败响应
     */
    public static <T> Result<T> failureWithData(String errorCode, String errorMessage, T data) {
        return new Result<T>()
                .setCode(errorCode)
                .setMessage(errorMessage)
                .setData(data);
    }
}
