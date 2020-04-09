package com.company.project.business.vo.user;

import com.company.project.business.enums.CreateWhereEnum;
import com.company.project.business.enums.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class UserAddReqVO {
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "手机号码")
    private String phone;
    @ApiModelProperty(value = "创建来源(1.web 2.android 3.ios )")
    private CreateWhereEnum createWhere;
    @ApiModelProperty(value = "性别(1.男 2.女)")
    private GenderEnum sex;
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "所拥有的角色")
    private List<Long> roleIds;
}
