package com.company.project.business.vo.user;

import com.company.project.business.enums.GenderEnum;
import com.company.project.business.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserUpdateReqVO {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;


    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "账户状态(1.正常 2.锁定 )")
    private StatusEnum status;

    @ApiModelProperty(value = "性别(1.男 2.女)")
    private GenderEnum sex;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "所拥有的角色")
    private List<Long> roleIds;

}
