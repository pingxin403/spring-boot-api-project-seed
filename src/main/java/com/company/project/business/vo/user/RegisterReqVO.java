package com.company.project.business.vo.user;

import com.company.project.business.enums.CreateWhereEnum;
import com.company.project.business.enums.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterReqVO {
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "性别(1.男 2.女) ")
    private GenderEnum sex;
    @ApiModelProperty(value = "创建来源(1.web 2.android 3.ios )")
    private CreateWhereEnum createWhere;
}
