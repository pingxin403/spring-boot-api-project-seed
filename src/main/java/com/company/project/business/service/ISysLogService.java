package com.company.project.business.service;


import com.company.project.business.enums.PlatformEnum;
import com.company.project.business.vo.log.LogVO;
import com.company.project.framework.object.IService;
import com.company.project.framework.object.PageResult;
import com.company.project.persistence.beans.SysLog;

public interface ISysLogService extends IService<SysLog> {

    /**
     * 分页查询
     *
     * @param vo
     * @return
     */
    PageResult<SysLog> findPageBreakByCondition(LogVO vo);

    void asyncSaveSystemLog(PlatformEnum platform, String bussinessName);
}
