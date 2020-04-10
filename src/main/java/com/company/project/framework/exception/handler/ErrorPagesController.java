package com.company.project.framework.exception.handler;

import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重写BasicErrorController,主要负责系统的异常页面的处理以及错误信息的显示
 * <p>
 * 要注意，这个类里面的代码一定不能有异常或者潜在异常发生，否则可能会让程序陷入死循环。
 * <p/>
 *
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.framework.exception.handler
 * hyp create at 20-4-10
 **/
@Slf4j
@RestController
@RequestMapping("/error")
@EnableConfigurationProperties({ServerProperties.class})
public class ErrorPagesController implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    HttpServletRequest request;

    /**
     * 初始化ExceptionController
     *
     * @param errorAttributes
     */
    @Autowired
    public ErrorPagesController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/404")
    public ResponseVO errorHtml404(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        response.setStatus(HttpStatus.NOT_FOUND.value());

        return ResultUtil.error(BaseResponseCode.NOT_FOUND);
    }

    @RequestMapping("/403")
    public ResponseVO errorHtml403(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ResultUtil.error(BaseResponseCode.FORBIDDEN);
    }

    @RequestMapping("/400")
    public ResponseVO errorHtml400(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResultUtil.error(BaseResponseCode.DATA_ERROR);
    }

    @RequestMapping("/401")
    public ResponseVO errorHtml401(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResultUtil.error(BaseResponseCode.UNAUTHORIZED);
    }

    @RequestMapping("/500")
    public ResponseVO errorHtml500(HttpServletRequest request, HttpServletResponse response, WebRequest webRequest) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResultUtil.error(BaseResponseCode.ERROR);
    }

    /**
     * 实现错误路径,暂时无用
     *
     * @return
     */
    @Override
//    @RequestMapping("")
    public String getErrorPath() {
        return "";
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        switch (statusCode) {
//            case 400:
//                return "/error/400";
//            case 401:
//                return "/error/401";
//            case 403:
//                return "/error/403";
//            case 404:
//                return "/error/404";
//            default:
//                return "/error/500";
//        }
    }


}
