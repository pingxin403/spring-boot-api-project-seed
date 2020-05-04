package com.company.project.persistence.beans;

import com.company.project.business.enums.CreateWhereEnum;
import com.company.project.business.enums.GenderEnum;
import com.company.project.business.enums.StatusEnum;
import com.company.project.business.enums.UserTypeEnum;
import com.company.project.framework.object.AbstractDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysUser extends AbstractDO {

    private static final long serialVersionUID = 1L;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 用户密码密文
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;


    /**
     * 真实名称
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private StatusEnum status;

    /**
     * 性别(1.男 2.女)
     */
    private GenderEnum sex;

    private String userType;
    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    private CreateWhereEnum createWhere;


}
