package cn.luowb.clubrecruitment.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定义全局返回对象｜方便接口参数返回约束，避免不同的参会定义混淆前端接收
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5679018624309023727L;

    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求ID
     */
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
