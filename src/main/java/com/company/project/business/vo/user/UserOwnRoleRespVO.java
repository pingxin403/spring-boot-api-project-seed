package com.company.project.business.vo.user;

import com.company.project.persistence.beans.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserOwnRoleRespVO {
    @ApiModelProperty("所有角色集合")
    private List<SysRole> allRole;
    @ApiModelProperty(value = "用户所拥有角色集合")
    private List<Long> ownRoles;
}
