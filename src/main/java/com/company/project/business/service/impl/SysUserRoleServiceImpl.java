package com.company.project.business.service.impl;


import com.company.project.business.service.ISysUserRoleService;
import com.company.project.business.vo.user.UserRoleOperationReqVO;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysUser;
import com.company.project.persistence.beans.SysUserRole;
import com.company.project.persistence.mapper.SysUserMapper;
import com.company.project.persistence.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public int removeByRoleId(Long roleId) {
        return sysUserRoleMapper.removeByRoleId(roleId);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return sysUserRoleMapper.getRoleIdsByUserId(userId);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserRoleInfo(UserRoleOperationReqVO vo) {
        if (vo.getRoleIds() == null || vo.getRoleIds().isEmpty()) {
            return;
        }
        List<SysUserRole> list = new ArrayList<>();
        for (Long roleId : vo.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setCreateTime(LocalDateTime.now());
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        sysUserRoleMapper.removeByUserId(vo.getUserId());
        int count = sysUserRoleMapper.batchUserRole(list);
        if (count == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByUserId(Long userId) {

        return sysUserRoleMapper.removeByUserId(userId);
    }

    @Override
    public List<Long> getUserIdsByRoleIds(List<Long> roleIds) {

        return sysUserRoleMapper.getUserIdsByRoleIds(roleIds);
    }

    @Override
    public List<SysUser> selectUserInfoByRoleId(Long roleId) {
        ArrayList<Long> list = new ArrayList<>();
        list.add(roleId);
        List<Long> userIds = sysUserRoleMapper.getUserIdsByRoleIds(list);
        List<SysUser> sysUsers = userMapper.selectBatchIds(userIds);
        return sysUsers;
    }
}
