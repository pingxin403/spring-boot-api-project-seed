package com.company.project.business.aspect;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.enums.PlatformEnum;
import com.company.project.business.service.ISysLogService;
import com.company.project.util.AspectUtil;
import com.company.project.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP切面记录日志
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.business.aspect
 * hyp create at 20-3-19
 **/
@Aspect
@Component
@Slf4j
public class BussinessLogAspect {

    @Autowired
    private ISysLogService logService;

    @Pointcut(value = "@annotation(com.company.project.business.annotation.BussinessLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object writeLog(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        //先执行业务
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        try {
            handle(point, time);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    private void handle(ProceedingJoinPoint point, long time) throws Exception {
        Method currentMethod = AspectUtil.INSTANCE.getMethod(point);
        //获取操作名称
        BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
        boolean save = annotation.save();

        //请求的方法名


        PlatformEnum platform = annotation.platform();
        String bussinessName = AspectUtil.INSTANCE.parseParams(point.getArgs(), annotation.value()) + ";方法:" + currentMethod.toString() + "--耗时：" + time;
        String ua = RequestUtil.getUa();

        log.info("{} | {} - {} {} - {}", bussinessName, RequestUtil.getIp(), RequestUtil.getMethod(), RequestUtil.getRequestUrl(), ua);
        if (!save) {
            return;
        }

        logService.asyncSaveSystemLog(platform, bussinessName);
    }


}
