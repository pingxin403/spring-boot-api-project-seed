package com.company.project.business.vo.role;

import com.company.project.business.enums.StatusEnum;
import com.company.project.framework.object.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class RolePageReqVO extends BaseVO {

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "状态(1:正常0:弃用)")
    private StatusEnum status;
}
