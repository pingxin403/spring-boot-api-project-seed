package com.company.project.persistence.beans;

import com.company.project.framework.object.AbstractDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysConfig extends AbstractDO {
    private String configKey;
    private String configValue;
}
