package cn.luowb.clubrecruitment.common.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.luowb.clubrecruitment.common.errorcode.BaseErrorCode;
import cn.luowb.clubrecruitment.common.exception.AbstractException;
import cn.luowb.clubrecruitment.common.result.Result;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Optional;

/**
 * 全局异常处理器｜拦截指定异常并通过优雅构建方式返回前端信息
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Void> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr, ex);
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result<Void> abstractException(HttpServletRequest request, AbstractException ex) {
        log.error("[{}] {} 业务异常: {}", request.getMethod(), getUrl(request), ex.getErrorMessage(), ex);
        return Results.failure(ex);
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<Void> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {} 系统未知异常 | 异常类型: {} | 信息: {}",
                request.getMethod(), getUrl(request), throwable.getClass().getName(), throwable.getMessage(), throwable);
        return Results.failure();
    }

    /**
     * 拦截 404 异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handle404(HttpServletRequest request, NoResourceFoundException ex) {
        // 针对 404 这种非逻辑错误的异常，只打一行 Warn，不打印堆栈
        log.warn("[404] 资源不存在: {} {}", request.getMethod(), request.getRequestURI());
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), "资源不存在");
    }

    private String getUrl(HttpServletRequest request) {
        if (StringUtils.isBlank(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
