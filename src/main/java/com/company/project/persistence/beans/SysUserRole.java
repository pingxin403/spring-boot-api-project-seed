package com.company.project.persistence.beans;

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
public class SysUserRole extends AbstractDO {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

}
