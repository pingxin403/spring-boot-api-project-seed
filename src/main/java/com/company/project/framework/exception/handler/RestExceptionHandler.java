package com.company.project.framework.exception.handler;


import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: RestExceptionHandler
 * controller 层全局异常统一处理类
 */
@RestControllerAdvice(basePackages = "com.company.project.controller")
@Slf4j
public class RestExceptionHandler {

    /**
     * 系统繁忙，请稍候再试"
     */
    @ExceptionHandler(Exception.class)
    public <T> ResponseVO<T> handleException(Exception e) {
        log.error("Exception,exception:{}", e);
        return ResultUtil.error(BaseResponseCode.SYSTEM_BUSY);
    }

    /**
     * 自定义全局异常处理
     */
    @ExceptionHandler(value = BusinessException.class)
    public <T> ResponseVO<T> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException,exception:{}", e);
        return ResultUtil.error(e.getCode(), e.getDetailMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public <T> ResponseVO<T> illegalArgumentExceptionHandler(IllegalArgumentException e)
    {
        log.error("IllegalArgumentException,exception:{}", e);
        return ResultUtil.error(BaseResponseCode.DATA_ERROR);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public <T> ResponseVO<T> handleNoHandlerError(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException,exception:{}", e);
        return ResultUtil.error(BaseResponseCode.NOT_FOUND);
    }

    /**
     * Shiro权限认证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {UnauthorizedException.class, AccountException.class})
    @ResponseBody
    public ResponseVO unauthorizedExceptionHandle(Throwable e) {
        e.printStackTrace(); // 打印异常栈
        return ResultUtil.error(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage());
    }

    /**
     * 处理 MissingServletRequestParameterException 异常
     * <p>
     * SpringMVC 参数不正确
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public <T> ResponseVO<T> missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException ex) {
        log.error("[missingServletRequestParameterExceptionHandler]", ex);
        // 包装 CommonResult 结果
        return ResultUtil.error(BaseResponseCode.DATA_ERROR);
    }

    /**
     * 没有权限 返回403视图
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public <T> ResponseVO<T> erroPermission(AuthorizationException e) {
        log.error("BusinessException,exception:{}", e);
        return ResultUtil.error(BaseResponseCode.UNAUTHORIZED_ERROR);

    }

    /**
     * 处理validation 框架异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> ResponseVO<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }


    private <T> ResponseVO<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}", msgs[i]);
            i++;
        }
        return ResultUtil.error(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), msgs[0]);
    }


}
