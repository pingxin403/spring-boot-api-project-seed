package com.company.project.persistence.beans;

import com.company.project.business.enums.StatusEnum;
import com.company.project.business.vo.permission.PermissionRespNode;
import com.company.project.framework.object.AbstractDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
public class SysRole extends AbstractDO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    private String name;

    private String description;

    /**
     * 状态(1:正常0:弃用)
     */
    private StatusEnum status;


    private transient List<PermissionRespNode> permissionRespNodes;


}
