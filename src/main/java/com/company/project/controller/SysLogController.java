package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.service.ISysLogService;
import com.company.project.business.vo.log.LogVO;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ResponseVO;
import com.company.project.persistence.beans.SysLog;
import com.company.project.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@RequestMapping("/sys/logs")
@Api(tags = "系统模块-系统操作日志管理")
@RestController
public class SysLogController {


    @Autowired
    private ISysLogService logService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询系统操作日志接口")
    @BussinessLog(value = "系统操作日志管理", action = "分页查询系统操作日志")
    @RequiresPermissions("sys:log:list")
    @ApiResponses(
            {
                    @ApiResponse(code = 500, message = "服务器内部错误", response = ResponseVO.class)
            }
    )
    public ResponseVO pageInfo(@RequestBody LogVO vo) {
        PageResult<SysLog> sysLogPageVO = logService.findPageBreakByCondition(vo);
        return ResultUtil.success("ok", sysLogPageVO);
    }

    @GetMapping("/")
    public ResponseVO list() {
        return ResultUtil.success("ok", logService.list());
    }

    @DeleteMapping("/")
    @ApiOperation(value = "删除日志接口")
    @BussinessLog(value = "系统操作日志管理", action = "删除系统操作日志")
    @RequiresPermissions("sys:log:deleted")
    public ResponseVO deleted(@RequestBody List<Long> logIds) {
        logService.removeByIds(logIds);
        return ResultUtil.success(BaseResponseCode.SUCCESS);
    }
}
