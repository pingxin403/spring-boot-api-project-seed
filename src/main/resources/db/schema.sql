-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
  `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
  `user_id`     bigint(20)  NULL DEFAULT NULL COMMENT '已登录用户ID',
  `type`        tinyint(4)  NOT NULL DEFAULT 1 COMMENT '日志类型（系统操作日志，用户访问日志，异常记录日志）',
  `log_level`   tinyint(4)  NOT NULL DEFAULT 3  COMMENT '日志级别',
  `content`     varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志内容（业务操作）',
  `params`      varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数（业务操作）',
  `spider_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬虫类型（当访问者被鉴定为爬虫时该字段表示爬虫的类型）',
  `ip`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户的ip',
  `ua`          varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户的user_agent',
  `os`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论时的系统类型',
  `browser`     varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论时的浏览器类型',
  `request_url` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的路径',
  `referer`     varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求来源地址',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time`     datetime                     DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
  `deleted`     tinyint(4)    DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`) USING BTREE
);

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission`
(
  `id`          bigint(20) NOT NULL COMMENT '主键',
  `code`        varchar(64)  DEFAULT NULL COMMENT '权限编码',
  `name`        varchar(300) DEFAULT NULL COMMENT '权限名称',
  `perms`       varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)',
  `pid`         bigint(20)  DEFAULT NULL COMMENT '父级菜单权限名称',
  `url`         varchar(100) DEFAULT NULL COMMENT '访问地址URL',
  `order_num`   int(11)      DEFAULT '0' COMMENT '排序',
  `type`        tinyint(4)   DEFAULT NULL COMMENT '菜单权限类型(1:目录;2:菜单;3:按钮;4:资源element(rest-api))',
  `method`      varchar(10)  DEFAULT NULL COMMENT '资源请求类型',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `status`      tinyint(4)   DEFAULT '1' COMMENT '状态1:正常 2：禁用',
  `create_time` datetime     DEFAULT NOW() COMMENT '创建时间',
  `update_time` datetime     DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     tinyint(4)   DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
);
--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role`
(
  `id`           bigint(20) NOT NULL COMMENT '主键',
  `name`        varchar(255) DEFAULT NULL COMMENT '角色名称',
  `description` varchar(300) DEFAULT NULL,
  `status`      tinyint(4)   DEFAULT '1' COMMENT '状态(1:正常2:弃用)',
  `create_time` datetime     DEFAULT NOW() COMMENT '创建时间',
  `update_time` datetime     DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     tinyint(4)   DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission`
(
  `id`             bigint(20)  NOT NULL COMMENT '主键',
  `role_id`        bigint(20) DEFAULT NULL COMMENT '角色id',
  `permission_id`  bigint(20) DEFAULT NULL COMMENT '菜单权限id',
  `create_time`   datetime    DEFAULT NOW() COMMENT '创建时间',
  `update_time`   datetime    DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`       tinyint(4)  DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user`
(
  `id`           bigint(20)   NOT NULL COMMENT '用户id',
  `username`     varchar(50)  NOT NULL COMMENT '账户名称',
  `salt`         varchar(40) DEFAULT NULL COMMENT '加密盐值',
  `password`     varchar(200) NOT NULL COMMENT '用户密码密文',
  `phone`        varchar(20) DEFAULT NULL COMMENT '手机号码',
  `real_name`    varchar(60) DEFAULT NULL COMMENT '真实名称',
  `nick_name`    varchar(60) DEFAULT NULL COMMENT '昵称',
  `email`        varchar(50) DEFAULT NULL COMMENT '邮箱(唯一)',
  `status`       tinyint(4)  DEFAULT '1' COMMENT '账户状态(1.正常 2.锁定 )',
  `sex`          tinyint(4)  DEFAULT '1' COMMENT '性别(1.男 2.女)',
  `deleted`      tinyint(4)  DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  `create_id`    bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_id`    bigint(20) DEFAULT NULL COMMENT '更新人',
  `create_where` tinyint(4)  DEFAULT '1' COMMENT '创建来源(1.web 2.android 3.ios )',
  `create_time`  datetime    DEFAULT NOW() COMMENT '创建时间',
  `update_time`  datetime    DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
  `id`          bigint(20)  NOT NULL COMMENT '用户id',
  `user_id`     bigint(20) DEFAULT NULL,
  `role_id`     bigint(20) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime    DEFAULT NOW() COMMENT '创建时间',
  `update_time` datetime    DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
  `deleted`     tinyint(4)  DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
  `id`           int(20)  NOT NULL AUTO_INCREMENT,
  `config_key`   varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置关键字',
  `config_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '配置项内容',
  `create_time` datetime    DEFAULT NOW() COMMENT '创建时间',
  `update_time` datetime    DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
  `deleted`     tinyint(4)   DEFAULT '1' COMMENT '是否删除(1未删除；0已删除)',
  PRIMARY KEY (`id`)
);


