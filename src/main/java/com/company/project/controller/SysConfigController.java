package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.service.ISysConfigService;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置
 */
@RestController
@RequestMapping("/config")
@Api(tags = "系统模块-系统配置管理")
public class SysConfigController {
    @Autowired
    private ISysConfigService sysConfigService;

    @RequiresRoles("role:admin")
    @GetMapping("/")
    public ResponseVO get() {
        return ResultUtil.success("ok", sysConfigService.getConfigs());
    }

    @RequiresRoles("role:admin")
    @PostMapping("/")
    @BussinessLog("修改系统配置")
    public ResponseVO save(@RequestBody Map<String, String> configs) {
        try {
            sysConfigService.saveConfig(configs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("系统配置修改失败");
        }
        return ResultUtil.success("系统配置修改成功");
    }

}
