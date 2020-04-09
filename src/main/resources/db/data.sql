insert into `sys_user`
values (1, 'admin', '324ce32d86224b00a02b', 'ac7e435db19997a46e3b390e69cb148b',
        '13888888888', '平心', '平心', 'm13839441583@163.com', 1, 1, 0, null, 0, 1, '2019-09-22 19:38:05',
        '2019-11-09 21:20:58');

INSERT INTO `sys_role`
VALUES (1, '超级管理员', '拥有所有权限-不能删除', 1, '2019-11-01 19:26:29', '2019-11-09 22:46:24',
        1),
       (2, '普通用户角色', '只读', 1, '2019-11-09 22:49:18', '2020-01-01 19:59:46', 1),
       (3, 'test', '测试', 1, '2020-01-01 20:01:58', '2019-11-19 10:43:05', 1),
       (4, '后台管理员', '一般是程序员拥有，用来初始化菜单权限', 1, '2019-11-09 21:25:31', NULL, 1),
       (5, '超级权限', '', 1, '2019-11-19 10:34:48', '2019-11-19 10:49:39', 1);

insert into `sys_user_role`
values (1, 1, 1, NOW(), NOW());

INSERT INTO `sys_config`(`id`, `config_key`, `config_value`, `create_time`, `update_time`)
VALUES (3, 'domain', 'hanyunpeng0521.github.io', now(), now());
INSERT INTO `sys_config`(`id`, `config_key`, `config_value`, `create_time`, `update_time`)
VALUES (10, 'authorName', '平心', now(), now());
INSERT INTO `sys_config`(`id`, `config_key`, `config_value`, `create_time`, `update_time`)
VALUES (11, 'authorEmail', 'm13839441583#163.com', now(), now());


