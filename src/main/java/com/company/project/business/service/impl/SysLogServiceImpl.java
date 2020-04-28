package com.company.project.business.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.business.annotation.RedisCache;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.enums.LogLevelEnum;
import com.company.project.business.enums.LogTypeEnum;
import com.company.project.business.enums.PlatformEnum;
import com.company.project.business.service.ISysLogService;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.vo.log.LogVO;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysLog;
import com.company.project.persistence.beans.SysUser;
import com.company.project.persistence.mapper.SysLogMapper;
import com.company.project.util.*;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private ISysUserService userService;

    @Override
    public PageResult<SysLog> findPageBreakByCondition(LogVO vo) {
        Page<SysLog> page = new Page<SysLog>(vo.getPageNumber(), vo.getPageSize());

        SysLog sysLog = BeanConvertUtil.doConvert(vo,SysLog.class);
        sysLog.setLogLevel(vo.getLogLevel());
        sysLog.setType(vo.getType());

        Page<SysLog> logPage = sysLogMapper.selectPage(page, Wrappers.query(sysLog));


        return ResultUtil.tablePage(logPage);
    }

    /**
     * 异步存储日志信息，根据自己的要求进行重写
     *
     * @param platform
     * @param bussinessName
     */
    @Async
    @Override
    public void asyncSaveSystemLog(PlatformEnum platform, String bussinessName) {

        String ua = RequestUtil.getUa();
        SysLog sysLog = new SysLog();
        sysLog.setLogLevel(LogLevelEnum.INFO);
        sysLog.setType(platform.equals(PlatformEnum.WEB) ? LogTypeEnum.VISIT : LogTypeEnum.SYSTEM);
        //设置IP地址
        sysLog.setIp(RequestUtil.getIp());
        sysLog.setReferer(RequestUtil.getReferer());
        sysLog.setRequestUrl(RequestUtil.getRequestUrl());
        sysLog.setUa(ua);
        sysLog.setSpiderType(WebSpiderUtils.parseUa(ua));
        sysLog.setParams(JSONObject.toJSONString(RequestUtil.getParametersMap()));

        //获取token
        SysUser user = null;
        String token = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);
        if (null != token && !"".equals(token)) {
            Long userId = Long.parseLong(JwtTokenUtil.getUserId(token));
            user = userService.getById(userId);
        }


        if (user != null) {
            sysLog.setUserId(user.getId());
            sysLog.setContent(String.format("用户: [%s] | 操作: %s", user.getUsername(), bussinessName));
        } else {
            sysLog.setContent(String.format("访客: [%s] | 操作: %s", sysLog.getIp(), bussinessName));
        }

        try {
            UserAgent agent = UserAgent.parseUserAgentString(ua);
            sysLog.setBrowser(agent.getBrowser().getName());
            sysLog.setOs(agent.getOperatingSystem().getName());
            this.save(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
