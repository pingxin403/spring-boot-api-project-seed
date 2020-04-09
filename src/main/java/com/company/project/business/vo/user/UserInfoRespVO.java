package com.company.project.business.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserInfoRespVO {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "真实姓名")
    private String realName;

}
