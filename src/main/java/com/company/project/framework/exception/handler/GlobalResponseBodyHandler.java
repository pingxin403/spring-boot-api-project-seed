package com.company.project.framework.exception.handler;

import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collections;


@ControllerAdvice(annotations = RestController.class)
public class GlobalResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType, Class converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (null == body) {
            return ResultUtil.success(Collections.emptyMap());
        } else if (!(body instanceof ResponseVO)) {
            ResponseVO responseResult = ResultUtil.success(body);
            //因为handler处理类的返回类型是String，为了保证一致性，这里需要将ResponseResult转回去
            return responseResult.toJson();
        }
        return body;
    }

}


