package com.company.project.persistence.beans;

import com.company.project.business.enums.LogLevelEnum;
import com.company.project.business.enums.LogTypeEnum;
import com.company.project.framework.object.AbstractDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLog extends AbstractDO {
    private Long userId;
    private LogLevelEnum logLevel;
    private String ip;
    private String content;
    private String params;
    private LogTypeEnum type;
    private String ua;
    private String os;
    private String browser;
    private String spiderType;
    private String requestUrl;
    private String referer;
}
