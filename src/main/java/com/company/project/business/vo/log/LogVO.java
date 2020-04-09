package com.company.project.business.vo.log;


import com.company.project.business.enums.LogLevelEnum;
import com.company.project.business.enums.LogTypeEnum;
import com.company.project.framework.object.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class LogVO extends BaseVO {
    private Long userId;
    private LogLevelEnum logLevel;
    private LogTypeEnum type;
    private Boolean spider;
}

