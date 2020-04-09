package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.service.IShiroService;
import com.company.project.business.service.ISysPermissionService;
import com.company.project.business.service.ISysRolePermissionService;
import com.company.project.business.vo.permission.PermissionAddReqVO;
import com.company.project.business.vo.permission.PermissionPageReqVO;
import com.company.project.business.vo.permission.PermissionUpdateReqVO;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 系统资源管理
 */
@RestController
@RequestMapping("/resources")
@Api(tags = "系统模块-菜单权限管理")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService resourcesService;
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    @Autowired
    private IShiroService shiroService;

    @RequiresPermissions("sys:role:allotResource")
    @PostMapping("/role")
    @ApiOperation(value = "根据role id获取权限接口")
    @BussinessLog(value = "菜单权限管理", action = "根据role id获取")
    public ResponseVO resourcesWithSelected(Long rid) {
        return ResultUtil.success(null, rolePermissionService.getPermissionIdsByRoleId(rid));
    }

    @PostMapping("/")
    @ApiOperation(value = "新增菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "新增菜单权限")
    @RequiresPermissions("sys:permission:add")
    public ResponseVO addPermission(@RequestBody @Valid PermissionAddReqVO vo) {
        return ResultUtil.success(resourcesService.addPermission(vo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "删除菜单权限")
    @RequiresPermissions("sys:permission:deleted")
    public ResponseVO deleted(@PathVariable("id") Long id) {
        resourcesService.deleted(id);
        return ResultUtil.success("删除菜单");
    }

    @PutMapping("/")
    @ApiOperation(value = "更新菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "更新菜单权限")
    @RequiresPermissions("sys:permission:update")
    public ResponseVO updatePermission(@RequestBody @Valid PermissionUpdateReqVO vo) {
        resourcesService.updatePermission(vo);
        return ResultUtil.success("更新菜单");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "查询菜单权限")
    @RequiresPermissions("sys:permission:detail")
    public ResponseVO detailInfo(@PathVariable("id") Long id) {
        return ResultUtil.success(resourcesService.detailInfo(id));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "分页查询菜单权限")
    @RequiresPermissions("sys:permission:list")
    public ResponseVO pageInfo(@RequestBody PermissionPageReqVO vo) {
        return ResultUtil.success(resourcesService.pageInfo(vo));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有菜单权限接口")
    @BussinessLog(value = "菜单权限管理", action = "获取所有菜单权限")
    @RequiresPermissions("sys:permission:list")
    public ResponseVO getAllMenusPermission() {
        System.out.println("fsfs89896666666");
        return ResultUtil.success(resourcesService.selectAll());
    }

    @GetMapping("/tree")
    @ApiOperation(value = "获取所有目录菜单树接口")
    @BussinessLog(value = "菜单权限管理", action = "获取所有目录菜单树")
    @RequiresPermissions(value = {"sys:permission:update", "sys:permission:add"}, logical = Logical.OR)
    public ResponseVO getAllMenusPermissionTree(@RequestParam(required = false) Long permissionId) {
        return ResultUtil.success(resourcesService.selectAllMenuByTree(permissionId));
    }

    @GetMapping("/tree/list")
    @ApiOperation(value = "获取所有目录菜单树接口")
    @BussinessLog(value = "菜单权限管理", action = "获取所有目录菜单树")
    @RequiresPermissions(value = {"sys:role:update", "sys:role:add"}, logical = Logical.OR)
    public ResponseVO getAllPermissionTree() {
        return ResultUtil.success(resourcesService.selectAllByTree());
    }
}
