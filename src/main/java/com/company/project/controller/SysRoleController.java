package com.company.project.controller;


import com.company.project.business.annotation.BussinessLog;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.IShiroService;
import com.company.project.business.service.ISysRolePermissionService;
import com.company.project.business.service.ISysRoleService;
import com.company.project.business.vo.role.RoleAddReqVO;
import com.company.project.business.vo.role.RolePageReqVO;
import com.company.project.business.vo.role.RolePermissionOperationReqVO;
import com.company.project.business.vo.role.RoleUpdateReqVO;
import com.company.project.framework.object.ResponseVO;
import com.company.project.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * 系统角色管理
 */
@RestController
@RequestMapping("/roles")
@Api(tags = "系统模块-角色管理")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysRolePermissionService roleResourcesService;
    @Autowired
    private IShiroService shiroService;


    @RequiresPermissions("sys:user:allotRole")
    @GetMapping("/user")
    @ApiOperation(value = "根据用户ID查询角色菜单权限接口")
    @BussinessLog(value = "角色和菜单关联接口", action = "根据用户ID查询角色菜单权限")
    public ResponseVO rolesWithSelected(Long uid) {
        return ResultUtil.success("ok", roleService.getRoleInfoByUserId(uid));
    }

    @PostMapping("/permission")
    @ApiOperation(value = "修改或者新增角色菜单权限接口")
    @BussinessLog(value = "角色和菜单关联接口", action = "修改或者新增角色菜单权限")
    @RequiresPermissions(value = {"sys:role:update", "sys:role:add"}, logical = Logical.OR)
    public ResponseVO saveRoleResources(@RequestBody @Valid RolePermissionOperationReqVO vo) {
        if (Objects.isNull(vo)) {
            return ResultUtil.error("error");
        }
        roleResourcesService.addRolePermission(vo);
        // 重新加载所有拥有roleId的用户的权限信息
        shiroService.reloadAuthorizingByRoleId(vo.getRoleId());
        return ResultUtil.success("修改或者新增角色菜单权限");
    }

    @PostMapping("/")
    @ApiOperation(value = "新增角色接口")
    @BussinessLog(value = "角色管理", action = "新增角色")
    @RequiresPermissions("sys:role:add")
    public ResponseVO addRole(@RequestBody @Valid RoleAddReqVO vo) {
        roleService.addRole(vo);
        return ResultUtil.success("新增角色");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除角色接口")
    @BussinessLog(value = "角色管理", action = "删除角色")
    @RequiresPermissions("sys:role:deleted")
    public ResponseVO deleted(@PathVariable("id") Long id) {
        roleService.deletedRole(id);
        return ResultUtil.success("删除角色");
    }

    @PutMapping("/")
    @ApiOperation(value = "更新角色信息接口")
    @BussinessLog(value = "角色管理", action = "更新角色信息")
    @RequiresPermissions("sys:role:update")
    public ResponseVO updateDept(@RequestBody @Valid RoleUpdateReqVO vo, HttpServletRequest request) {
        roleService.updateRole(vo, request.getHeader(JwtConstant.ACCESS_TOKEN));
        return ResultUtil.success("更新角色信息");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询角色详情接口")
    @BussinessLog(value = "角色管理", action = "查询角色详情")
    @RequiresPermissions("sys:role:detail")
    public ResponseVO detailInfo(@PathVariable("id") Long id) {
        return ResultUtil.success(roleService.detailInfo(id));
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页获取角色信息接口")
    @BussinessLog(value = "角色管理", action = "分页获取角色信息")
    @RequiresPermissions("sys:role:list")
    public ResponseVO pageInfo(@RequestBody RolePageReqVO vo) {
        return ResultUtil.success(roleService.pageInfo(vo));
    }


}
