SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
  `id`            int(11)      NOT NULL AUTO_INCREMENT,
  `username`      varchar(255) NOT NULL,
  `password`      varchar(255) NOT NULL,
  `nick_name`     varchar(255)      DEFAULT NULL,
  `sex`           int(1)            DEFAULT NULL,
  `register_date` datetime     NOT NULL,
  `create_time`   datetime     NULL DEFAULT NOW(),
  `modified_time` datetime     NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
  `deleted`       int               default 1,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `tb_user`
VALUES ('1', '12356@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('2', '2@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-2', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('3', '3@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-3', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('4', '4@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-4', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('5', '5@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-5', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('6', '6@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-6', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('7', '7@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-7', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('8', '8@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-8', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('9', '9@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-9', '1', '2017-06-23 14:24:23', null, null, 1);
INSERT INTO `tb_user`
VALUES ('10', '10@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-10', '1', '2017-06-23 14:24:23', null, null, 1);
SET FOREIGN_KEY_CHECKS = 1;
