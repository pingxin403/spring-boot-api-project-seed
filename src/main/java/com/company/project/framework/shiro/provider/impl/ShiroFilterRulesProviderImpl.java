package com.company.project.framework.shiro.provider.impl;


import com.company.project.business.enums.PermissionTypeEnum;
import com.company.project.framework.shiro.provider.ShiroFilterRulesProvider;
import com.company.project.framework.shiro.rule.RolePermRule;
import com.company.project.persistence.mapper.SysRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tomsun28
 * @date 16:46 2018/3/7
 */
@Service("ShiroFilterRulesProvider")
public class ShiroFilterRulesProviderImpl implements ShiroFilterRulesProvider {

    @Autowired
    private SysRolePermissionMapper authResourceMapper;

    @Override
    public List<RolePermRule> loadRolePermRules() {

        return authResourceMapper.selectRoleRules(PermissionTypeEnum.REST_API.getCode());
    }

}
