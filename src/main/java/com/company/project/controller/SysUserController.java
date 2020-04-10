package com.company.project.controller;


import cn.hutool.core.lang.Assert;
import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.ISysUserRoleService;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.vo.user.UserAddReqVO;
import com.company.project.business.vo.user.UserPageReqVO;
import com.company.project.business.vo.user.UserUpdateReqVO;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.JwtTokenUtil;
import com.company.project.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "系统模块-用户管理")
public class SysUserController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserRoleService userRoleService;

    @PutMapping("/")
    @ApiOperation(value = "更新用户信息接口")
    @BussinessLog(value = "用户管理", action = "更新用户信息")
    @RequiresPermissions("sys:user:update")
    public ResponseVO updateUserInfo(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String jwt = request.getHeader(JwtConstant.ACCESS_TOKEN);
        Assert.notBlank(jwt);
        String id = JwtTokenUtil.getUserId(jwt);
        userService.updateUserInfo(vo, id);
        return ResultUtil.success("更新用户信息");
    }

    @PutMapping("/info")
    @ApiOperation(value = "更新用户信息接口")
    @BussinessLog(value = "用户管理", action = "更新用户信息")
    @RequiresPermissions("sys:user:update")
    public ResponseVO updateUserInfoById(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String jwt = request.getHeader(JwtConstant.ACCESS_TOKEN);
        Assert.notBlank(jwt);
        String id = JwtTokenUtil.getUserId(jwt);
        vo.setId(Long.valueOf(id));
        userService.updateUserInfo(vo, id);
        return ResultUtil.success("更新用户信息");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询用户详情接口")
    @BussinessLog(value = "用户管理", action = "查询用户详情")
    @RequiresPermissions("sys:user:detail")
    public ResponseVO detailInfo(@PathVariable("id") Long id) {
        return ResultUtil.success(userService.detailInfo(id));
    }

    @GetMapping("/")
    @ApiOperation(value = "查询用户详情接口")
    @BussinessLog(value = "用户管理", action = "查询用户详情")
    @RequiresPermissions("sys:user:detail")
    public ResponseVO youSelfInfo(HttpServletRequest request) {
        String jwt = request.getHeader(JwtConstant.ACCESS_TOKEN);
        Assert.notBlank(jwt);
        String id = JwtTokenUtil.getUserId(jwt);
        return ResultUtil.success(userService.detailInfo(Long.parseLong(id)));

    }

    @PostMapping("/page")
    @ApiOperation(value = "分页获取用户列表接口")
    @RequiresPermissions("sys:user:list")
    @BussinessLog(value = "用户管理", action = "分页获取用户列表")
    public ResponseVO pageInfo(@RequestBody UserPageReqVO vo) {
        return ResultUtil.success(userService.pageInfo(vo));
    }

    @PostMapping("/")
    @ApiOperation(value = "新增用户接口")
    @RequiresPermissions("sys:user:add")
    @BussinessLog(value = "用户管理", action = "新增用户")
    public ResponseVO addUser(@RequestBody @Valid UserAddReqVO vo) {
        userService.addUser(vo);
        return ResultUtil.success("新增用户");
    }

    @DeleteMapping("/")
    @ApiOperation(value = "删除用户接口")
    @BussinessLog(value = "用户管理", action = "删除用户")
    @RequiresPermissions("sys:user:deleted")
    public ResponseVO deletedUser(@RequestBody @ApiParam(value = "用户id集合") List<Long> userIds, HttpServletRequest request) {
        String jwt = request.getHeader(JwtConstant.ACCESS_TOKEN);
        Assert.notBlank(jwt);
        String id = JwtTokenUtil.getUserId(jwt);
        userService.deletedUsers(userIds, id);
        return ResultUtil.success("删除用户");
    }

    @GetMapping("/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取所有角色接口")
    @BussinessLog(value = "用户管理", action = "赋予角色-获取所有角色接口")
    @RequiresPermissions("sys:user:role:detail")
    public ResponseVO getUserOwnRole(@PathVariable("userId") Long userId) {
        return ResultUtil.success(userService.getUserOwnRole(userId));
    }

    @PutMapping("/roles/{userId}")
    @ApiOperation(value = "赋予角色-用户赋予角色接口")
    @BussinessLog(value = "用户管理", action = "赋予角色-用户赋予角色接口")
    @RequiresPermissions("sys:user:update:role")
    public ResponseVO setUserOwnRole(@PathVariable("userId") Long userId, @RequestBody List<Long> roleIds) {
        userService.setUserOwnRole(userId, roleIds);
        return ResultUtil.success("用户赋予角色");
    }
}
