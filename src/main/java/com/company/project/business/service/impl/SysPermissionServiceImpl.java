package com.company.project.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.business.enums.DeletedEnum;
import com.company.project.business.enums.PermissionTypeEnum;
import com.company.project.business.service.ISysPermissionService;
import com.company.project.business.service.ISysRolePermissionService;
import com.company.project.business.service.ISysUserRoleService;
import com.company.project.business.service.RedisService;
import com.company.project.business.vo.permission.PermissionAddReqVO;
import com.company.project.business.vo.permission.PermissionPageReqVO;
import com.company.project.business.vo.permission.PermissionRespNode;
import com.company.project.business.vo.permission.PermissionUpdateReqVO;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.object.PageResult;
import com.company.project.framework.object.ServiceImpl;
import com.company.project.persistence.beans.SysPermission;
import com.company.project.persistence.mapper.SysPermissionMapper;
import com.company.project.util.BeanConvertUtil;
import com.company.project.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

import static com.company.project.business.enums.PermissionTypeEnum.DIRECTORY;
import static com.company.project.business.enums.PermissionTypeEnum.MENU;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pingxin
 * @since 2020-03-03
 */
@Service
@Slf4j
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    /**
     * 根据用户查询拥有的权限
     * 先查出用户拥有的角色
     * 再去差用户拥有的权限
     * 也可以多表关联查询
     */
    @Override
    public List<SysPermission> getPermission(Long userId) {
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        List<Long> permissionIds = rolePermissionService.getPermissionIdsByRoles(roleIds);
        if (permissionIds.isEmpty()) {
            return null;
        }
        List<SysPermission> result = sysPermissionMapper.selectBatchIds(permissionIds);
        return result;
    }

    /**
     * 新增菜单权限
     */
    @Override
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission = BeanConvertUtil.doConvert(vo, SysPermission.class);
        verifyForm(sysPermission);
        sysPermission.setCreateTime(LocalDateTime.now());
        int count = sysPermissionMapper.insert(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysPermission;
    }

    /**
     * 操作后的菜单类型是目录的时候 父级必须为目录
     * 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     */
    private void verifyFormPid(SysPermission sysPermission) {
        SysPermission parent = sysPermissionMapper.selectById(sysPermission.getPid());
        switch (sysPermission.getType()) {
            case DIRECTORY:
                if (parent != null) {
                    if (parent.getType() != DIRECTORY) {
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                } else if (!"0".equals(sysPermission.getPid())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case MENU:
                if (parent == null || parent.getType() != DIRECTORY) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }

                break;
            case BUTTON:
                if (parent == null || parent.getType() != MENU) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getPerms())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getMethod())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 编辑或者新增的时候检验
     */
    private void verifyForm(SysPermission sysPermission) {

        verifyFormPid(sysPermission);
        /**
         * id 不为空说明是编辑
         */
        if (!Objects.isNull(sysPermission.getId())) {
            List<SysPermission> list = sysPermissionMapper.selectChild(sysPermission.getId());
            if (!list.isEmpty()) {
                throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }

    }

    /**
     * 查询菜单权限详情
     */
    @Override
    public SysPermission detailInfo(Long permissionId) {

        return sysPermissionMapper.selectById(permissionId);
    }

    /**
     * 更新菜单权限
     */
    @Override
    public void updatePermission(PermissionUpdateReqVO vo) {

        SysPermission sysPermission = sysPermissionMapper.selectById(vo.getId());
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysPermission update = BeanConvertUtil.doConvert(vo, SysPermission.class);
        /**
         * 只有类型变更
         * 或者所属菜单变更
         */
        if (!sysPermission.getType().equals(vo.getType()) || !sysPermission.getPid().equals(vo.getPid())) {
            verifyForm(update);
        }
        update.setUpdateTime(LocalDateTime.now());
        int count = sysPermissionMapper.updateById(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    /**
     * 删除菜单权限
     * 判断是否 有角色关联
     * 判断是否有子集
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleted(Long permissionId) {
        SysPermission sysPermission = sysPermissionMapper.selectById(permissionId);
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", permissionId);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<SysPermission> childs = sysPermissionMapper.selectChild(permissionId);
        if (!childs.isEmpty()) {
            throw new BusinessException(BaseResponseCode.ROLE_PERMISSION_RELATION);
        }
        sysPermission.setDeleted(DeletedEnum.deleted);
        sysPermission.setUpdateTime(LocalDateTime.now());
        int count = sysPermissionMapper.updateById(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        /**
         * 删除和角色关联
         */
        rolePermissionService.removeByPermissionId(permissionId);
        List<Long> roleIds = rolePermissionService.getRoleIds(permissionId);
    }

    /**
     * 分页获取所有菜单权限
     */
    @Override
    public PageResult<SysPermission> pageInfo(PermissionPageReqVO vo) {
        Page<SysPermission> page = new Page<SysPermission>(vo.getPageNumber(), vo.getPageSize());

        SysPermission sysPermission = BeanConvertUtil.doConvert(vo, SysPermission.class);

        Page<SysPermission> permissionPage = sysPermissionMapper.selectPage(page, Wrappers.query(sysPermission));

        return ResultUtil.tablePage(permissionPage);
    }

    /**
     * 获取所有菜单权限
     */
    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> result = sysPermissionMapper.selectList(Wrappers.emptyWrapper());
        if (!result.isEmpty()) {
            for (SysPermission sysPermission : result) {
                SysPermission parent = sysPermissionMapper.selectById(sysPermission.getPid());
                if (parent != null) {
                    sysPermission.setPid(parent.getId());
                }
            }
        }
        return result;
    }

    /**
     * 获取权限标识
     */
    @Override
    public Set<String> getPermissionsByUserId(Long userId) {

        List<SysPermission> list = getPermission(userId);
        Set<String> permissions = new HashSet<>();
        if (null == list || list.isEmpty()) {
            return null;
        }
        for (SysPermission sysPermission : list) {
            if (!StringUtils.isEmpty(sysPermission.getPerms())) {
                permissions.add(sysPermission.getPerms());
            }

        }
        return permissions;
    }

    /**
     * 以树型的形式把用户拥有的菜单权限返回给客户端
     */
    @Override
    public List<PermissionRespNode> permissionTreeList(Long userId) {
        List<SysPermission> list = getPermission(userId);
        return getTree(list, true);
    }

    /**
     * 递归获取菜单树
     */
    private List<PermissionRespNode> getTree(List<SysPermission> all, boolean type) {

        List<PermissionRespNode> list = new ArrayList<>();
        if (all == null || all.isEmpty()) {
            return list;
        }
        for (SysPermission sysPermission : all) {
            if (0L == (sysPermission.getPid())) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());

                if (type) {
                    permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                } else {
                    permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                }
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 递归遍历所有
     */
    private List<PermissionRespNode> getChildAll(Long id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id)) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 只递归获取目录和菜单
     */
    private List<PermissionRespNode> getChildExcBtn(Long id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id) && sysPermission.getType() != PermissionTypeEnum.BUTTON) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 获取所有菜单权限按钮
     */
    @Override
    public List<PermissionRespNode> selectAllByTree() {

        List<SysPermission> list = selectAll();
        return getTree(list, false);
    }

    /**
     * 获取所有的目录菜单树排除按钮
     * 因为不管是新增或者修改
     * 选择所属菜单目录的时候
     * 都不可能选择到按钮
     * 而且编辑的时候 所属目录不能
     * 选择自己和它的子类
     */
    @Override
    public List<PermissionRespNode> selectAllMenuByTree(Long permissionId) {

        List<SysPermission> list = selectAll();
        if (!list.isEmpty() && !StringUtils.isEmpty(permissionId)) {
            for (SysPermission sysPermission : list) {
                if (sysPermission.getId().equals(permissionId)) {
                    list.remove(sysPermission);
                    break;
                }
            }
        }
        List<PermissionRespNode> result = new ArrayList<>();
        //新增顶级目录是为了方便添加一级目录
        PermissionRespNode respNode = new PermissionRespNode();
        respNode.setId(0L);
        respNode.setTitle("默认顶级菜单");
        respNode.setSpread(true);
        respNode.setChildren(getTree(list, true));
        result.add(respNode);
        return result;
    }
}
