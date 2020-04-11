-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission`
VALUES (1, '', '获取系统配置', 'sys:config:list', 3, '/sys/config/', 0, 3, 'GET', NULL, 1, '2020-04-10 09:05:55',
        '2020-04-10 11:42:44', 1);
INSERT INTO `sys_permission`
VALUES (2, '', '更新系统配置', 'sys:config:edit', 3, '/sys/config/', 0, 3, 'POST', NULL, 1, '2020-04-10 09:07:50',
        '2020-04-10 11:44:12', 1);
INSERT INTO `sys_permission`
VALUES (3, '', '分页查询系统操作日志', 'sys:log:page', 3, '/sys/logs/page', 0, 3, 'POST', NULL, 1, '2020-04-10 09:10:13',
        '2020-04-10 11:44:14', 1);
INSERT INTO `sys_permission`
VALUES (4, '', '删除系统操作日志', 'sys:log:deleted', 3, '/sys/logs/', 0, 3, 'DELETE', NULL, 1, '2020-04-10 09:10:34',
        '2020-04-10 11:48:55', 1);
INSERT INTO `sys_permission`
VALUES (5, '', '查询系统操作日志接口', 'sys:log:list', 3, '/sys/logs/', 0, 3, 'GET', NULL, 1, '2020-04-10 09:12:59',
        '2020-04-10 11:49:03', 1);
INSERT INTO `sys_permission`
VALUES (6, '', '更新用户信息', 'sys:user:update', 3, '/sys/user/', 0, 3, 'PUT', NULL, 1, '2020-04-10 10:49:47',
        '2020-04-10 11:44:19', 1);
INSERT INTO `sys_permission`
VALUES (7, '', '查询用户详情', 'sys:user:detail', 3, '/sys/user/*', 0, 3, 'GET', NULL, 1, '2020-04-10 10:54:16',
        '2020-04-10 11:44:21', 1);
INSERT INTO `sys_permission`
VALUES (8, '', '分页获取用户列表', 'sys:user:list', 3, '/sys/user/page', 0, 3, 'POST', NULL, 1, '2020-04-10 10:55:06',
        '2020-04-10 11:44:22', 1);
INSERT INTO `sys_permission`
VALUES (9, '', '新增用户', 'sys:user:add', 3, '/sys/user/', 0, 3, 'POST', NULL, 1, '2020-04-10 10:55:48',
        '2020-04-10 11:44:24', 1);
INSERT INTO `sys_permission`
VALUES (10, '', '删除用户', 'sys:user:deleted', 3, '/sys/user/*', 0, 3, 'DELETE', NULL, 1, '2020-04-10 10:57:35',
        '2020-04-10 11:44:26', 1);
INSERT INTO `sys_permission`
VALUES (11, '', '赋予角色-获取所有角色', 'sys:user:role:detail', 3, '/sys/user/roles/*', 0, 3, 'GET', NULL, 1,
        '2020-04-10 10:57:53', '2020-04-10 11:44:27', 1);
INSERT INTO `sys_permission`
VALUES (12, '', '赋予角色-用户赋予角色', 'sys:user:update:role', 3, '/sys/user/roles/*', 0, 3, 'PUT', NULL, 1,
        '2020-04-10 10:59:08', '2020-04-10 11:44:29', 1);
INSERT INTO `sys_permission`
VALUES (13, '', '根据用户ID查询角色菜单权限', 'sys:user:allotRole', 3, '/sys/roles/user', 0, 3, 'GET', NULL, 1,
        '2020-04-10 11:00:16', '2020-04-10 11:44:31', 1);
INSERT INTO `sys_permission`
VALUES (14, '', '修改或者新增角色菜单权限', 'sys:role:update,sys:role:add', 3, '/sys/roles/permission', 0, 3, 'POST', NULL, 1,
        '2020-04-10 11:39:15', '2020-04-10 11:44:33', 1);
INSERT INTO `sys_permission`
VALUES (15, '', '新增角色', 'sys:role:add', 3, '/sys/roles/', 0, 3, 'POST', NULL, 1, '2020-04-10 11:45:25',
        '2020-04-10 11:45:25', 1);
INSERT INTO `sys_permission`
VALUES (16, '', '删除角色', 'sys:role:deleted', 3, '/sys/roles/*', 0, 3, 'DELETE', NULL, 1, '2020-04-10 11:46:17',
        '2020-04-10 11:46:17', 1);
INSERT INTO `sys_permission`
VALUES (17, '', '更新角色信息', 'sys:role:update', 3, '/sys/roles/', 0, 3, 'PUT', NULL, 1, '2020-04-10 11:46:30',
        '2020-04-10 11:46:57', 1);
INSERT INTO `sys_permission`
VALUES (18, '', '查询角色详情', 'sys:role:detail', 3, '/sys/roles/*', 0, 3, 'GET', NULL, 1, '2020-04-10 11:47:32',
        '2020-04-10 11:47:32', 1);
INSERT INTO `sys_permission`
VALUES (19, '', '分页获取角色信息', 'sys:role:list', 3, '/sys/roles/page', 0, 3, 'POST', NULL, 1, '2020-04-10 11:48:09',
        '2020-04-10 11:48:09', 1);
INSERT INTO `sys_permission`
VALUES (20, '', '根据role id获取', 'sys:role:allotResource', 3, '/sys/resources/role', 0, 3, 'POST', NULL, 1,
        '2020-04-10 11:49:56', '2020-04-10 11:49:56', 1);
INSERT INTO `sys_permission`
VALUES (21, '', '新增权限', 'sys:permission:add', 3, '/sys/resources/', 0, 3, 'POST', NULL, 1, '2020-04-10 11:50:43',
        '2020-04-10 11:50:59', 1);
INSERT INTO `sys_permission`
VALUES (22, '', '删除权限', 'sys:permission:deleted', 3, '/sys/resources/*', 0, 3, 'DELETE', NULL, 1, '2020-04-10 11:51:38',
        '2020-04-10 11:51:38', 1);
INSERT INTO `sys_permission`
VALUES (23, '', '更新权限', 'sys:permission:update', 3, '/sys/resources/', 0, 3, 'PUT', NULL, 1, '2020-04-10 11:52:20',
        '2020-04-10 11:52:20', 1);
INSERT INTO `sys_permission`
VALUES (24, '', '查询权限', 'sys:permission:detail', 3, '/sys/resources/*', 0, 3, 'GET', NULL, 1, '2020-04-10 11:52:42',
        '2020-04-10 11:52:55', 1);
INSERT INTO `sys_permission`
VALUES (25, '', '分页查询权限', 'sys:permission:list', 3, '/sys/resources/page', 0, 3, 'POST', NULL, 1, '2020-04-10 11:53:39',
        '2020-04-10 11:53:59', 1);
INSERT INTO `sys_permission`
VALUES (26, '', '获取所有权限', 'sys:permission:list', 3, '/sys/resources/list', 0, 3, 'GET', NULL, 1, '2020-04-10 11:54:26',
        '2020-04-10 11:54:43', 1);
INSERT INTO `sys_permission`
VALUES (27, '', '获取所有目录树', 'sys:permission:update,sys:permission:add', 3, '/sys/resources/tree', 0, 3, 'GET', NULL, 1,
        '2020-04-10 11:54:55', '2020-04-10 11:56:52', 1);
INSERT INTO `sys_permission`
VALUES (28, '', '获取所有目录树', 'sys:permission:update,sys:permission:add', 3, '/sys/resources/tree/list', 0, 3, 'GET', NULL,
        1, '2020-04-10 11:55:50', '2020-04-10 11:56:55', 1);
COMMIT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role`
VALUES (1, 'admin', '管理员角色', 1, '2020-04-10 08:32:39', '2020-04-10 08:48:46', 1);
INSERT INTO `sys_role`
VALUES (2, 'user', '用户角色', 1, '2020-04-10 08:48:09', '2020-04-10 08:48:09', 1);
COMMIT;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permission`
VALUES (1, 1, 1, '2020-04-10 11:58:06', '2020-04-10 11:58:06', 1);
INSERT INTO `sys_role_permission`
VALUES (2, 1, 2, '2020-04-10 11:58:15', '2020-04-10 11:58:15', 1);
INSERT INTO `sys_role_permission`
VALUES (3, 1, 3, '2020-04-10 11:58:24', '2020-04-10 11:58:24', 1);
INSERT INTO `sys_role_permission`
VALUES (4, 1, 4, '2020-04-10 11:58:32', '2020-04-10 11:58:32', 1);
INSERT INTO `sys_role_permission`
VALUES (5, 1, 5, '2020-04-10 11:58:39', '2020-04-10 11:58:39', 1);
INSERT INTO `sys_role_permission`
VALUES (6, 1, 6, '2020-04-10 11:58:42', '2020-04-10 11:58:48', 1);
INSERT INTO `sys_role_permission`
VALUES (7, 1, 7, '2020-04-10 11:59:19', '2020-04-10 11:59:19', 1);
INSERT INTO `sys_role_permission`
VALUES (8, 1, 8, '2020-04-10 11:59:27', '2020-04-10 11:59:27', 1);
INSERT INTO `sys_role_permission`
VALUES (9, 1, 9, '2020-04-10 11:59:29', '2020-04-10 11:59:34', 1);
INSERT INTO `sys_role_permission`
VALUES (10, 1, 10, '2020-04-10 11:59:41', '2020-04-10 11:59:41', 1);
INSERT INTO `sys_role_permission`
VALUES (11, 1, 11, '2020-04-10 11:59:46', '2020-04-10 11:59:46', 1);
INSERT INTO `sys_role_permission`
VALUES (12, 1, 12, '2020-04-10 11:59:53', '2020-04-10 11:59:53', 1);
INSERT INTO `sys_role_permission`
VALUES (13, 1, 13, '2020-04-10 12:00:00', '2020-04-10 12:00:00', 1);
INSERT INTO `sys_role_permission`
VALUES (14, 1, 14, '2020-04-10 12:00:07', '2020-04-10 12:00:07', 1);
INSERT INTO `sys_role_permission`
VALUES (15, 1, 15, '2020-04-10 12:00:15', '2020-04-10 12:00:15', 1);
INSERT INTO `sys_role_permission`
VALUES (16, 1, 16, '2020-04-10 12:00:22', '2020-04-10 12:00:22', 1);
INSERT INTO `sys_role_permission`
VALUES (17, 1, 17, '2020-04-10 12:00:28', '2020-04-10 12:00:28', 1);
INSERT INTO `sys_role_permission`
VALUES (18, 1, 18, '2020-04-10 12:00:44', '2020-04-10 12:00:44', 1);
INSERT INTO `sys_role_permission`
VALUES (19, 1, 19, '2020-04-10 12:01:10', '2020-04-10 12:01:10', 1);
INSERT INTO `sys_role_permission`
VALUES (20, 1, 20, '2020-04-10 12:01:16', '2020-04-10 12:01:16', 1);
INSERT INTO `sys_role_permission`
VALUES (21, 1, 21, '2020-04-10 12:01:21', '2020-04-10 12:01:21', 1);
INSERT INTO `sys_role_permission`
VALUES (22, 1, 22, '2020-04-10 12:01:27', '2020-04-10 12:01:27', 1);
INSERT INTO `sys_role_permission`
VALUES (23, 1, 23, '2020-04-10 12:01:33', '2020-04-10 12:01:33', 1);
INSERT INTO `sys_role_permission`
VALUES (24, 1, 24, '2020-04-10 12:01:40', '2020-04-10 12:01:40', 1);
INSERT INTO `sys_role_permission`
VALUES (25, 1, 25, '2020-04-10 12:01:46', '2020-04-10 12:01:46', 1);
INSERT INTO `sys_role_permission`
VALUES (26, 1, 26, '2020-04-10 12:01:52', '2020-04-10 12:01:52', 1);
INSERT INTO `sys_role_permission`
VALUES (27, 1, 27, '2020-04-10 12:01:59', '2020-04-10 12:01:59', 1);
INSERT INTO `sys_role_permission`
VALUES (28, 1, 28, '2020-04-10 12:02:08', '2020-04-10 12:02:08', 1);
COMMIT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

insert into `sys_user`
values (1, 'admin', '324ce32d86224b00a02b', 'ac7e435db19997a46e3b390e69cb148b',
        '13888888888', '平心', '平心', 'm13839441583@163.com', 1, 1, 0, null, 0, 1, '2019-09-22 19:38:05',
        '2019-11-09 21:20:58');

INSERT INTO `sys_user_role`
VALUES (1, 1, 1, '2020-04-10 12:03:10', '2020-04-10 12:03:10', 1);

INSERT INTO `sys_config`(`id`, `key`, `value`, `create_time`, `update_time`)
VALUES (3, 'domain', 'hanyunpeng0521.github.io', now(), now());
INSERT INTO `sys_config`(`id`, `key`, `value`, `create_time`, `update_time`)
VALUES (10, 'authorName', '平心', now(), now());
INSERT INTO `sys_config`(`id`, `key`, `value`, `create_time`, `update_time`)
VALUES (11, 'authorEmail', 'm13839441583#163.com', now(), now());



