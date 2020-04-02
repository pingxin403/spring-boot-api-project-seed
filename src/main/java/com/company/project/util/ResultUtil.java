package com.company.project.util;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ResponseVO;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 接口返回工具类，支持ModelAndView、ResponseVO、PageResult
 */
public class ResultUtil {

    public static ModelAndView view(String view) {
        return new ModelAndView(view);
    }

    public static ModelAndView view(String view, Map<String, Object> model) {
        return new ModelAndView(view, model);
    }

    public static ModelAndView redirect(String view) {
        return new ModelAndView("redirect:" + view);
    }

    public static ResponseVO error(int code, String message) {
        return vo(code, message, null);
    }

    public static ResponseVO error(BaseResponseCode status) {
        return vo(status.getCode(), status.getMsg(), null);
    }

    public static ResponseVO error(String message) {
        return vo(BaseResponseCode.ERROR.getCode(), message, null);
    }

    public static ResponseVO success(String message, Object data) {
        return vo(BaseResponseCode.ERROR.getCode(), message, data);
    }

    public static ResponseVO success(String message) {
        return success(message, null);
    }

    public static ResponseVO success(BaseResponseCode status) {
        return vo(status.getCode(), status.getMsg(), null);
    }

    public static ResponseVO vo(int code, String message, Object data) {
        return new ResponseVO<>(code, message, data);
    }

    public static PageResult tablePage(Long total, List<?> list) {
        return new PageResult(total, list);
    }

    public static PageResult tablePage(Page info) {
        if (info == null) {
            return new PageResult(0L, new ArrayList());
        }
        return new PageResult(info.getTotal(), info.getCurrent(), info.getPages(), info.getSize(), info.getRecords().size(), info.getRecords());
    }

}
