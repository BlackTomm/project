#mybatis plus mapper测试数据
DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '登录名',
  `password` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DELETE FROM tb_user WHERE ID > 0;

insert tb_user(name, password) values ("test1","111"),
                                      ("test2","222"),
                                      ("test3","333"),
                                      ("test4","444");

#分页测试数据
-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
                            `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
                            `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
                            `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '密码',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `tb_user` VALUES (2, 'test2', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (3, 'test3', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (4, 'test4', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (5, 'test5', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (6, 'test6', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (7, 'test7', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (8, 'test8', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (9, 'test9', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (10, 'test10', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (11, 'test11', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (12, 'test12', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (13, 'test13', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (14, 'test14', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (15, 'test15', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (16, 'test16', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (17, 'test17', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (18, 'test18', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (19, 'test19', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (20, 'admin2', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (21, 'admin3', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (22, 'admin4', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (23, 'admin5', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (24, 'admin6', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (25, 'admin7', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (26, 'admin8', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (27, 'admin9', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (28, 'admin10', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (29, 'admin11', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (30, 'admin12', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (31, 'admin13', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (32, 'admin14', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (33, 'admin15', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (34, 'admin16', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (35, 'admin17', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (36, 'admin18', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (37, 'admin19', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (38, 'admin011', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (39, 'admin02', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (40, 'admin03', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (41, 'admin04', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (42, 'admin05', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (43, 'admin06', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (44, 'admin07', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (45, 'admin08', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (46, 'admin09', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (47, 'admin010', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (48, 'admin011', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (49, 'admin012', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (50, 'admin013', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (51, 'admin014', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (52, 'admin015', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (53, 'admin016', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (54, 'admin017', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (55, 'admin018', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (56, 'admin019', '098f6bcd4621d373cade4e832627b4f6');
INSERT INTO `tb_user` VALUES (57, 'ZHENFENG13', '77c9749b451ab8c713c48037ddfbb2c4');
INSERT INTO `tb_user` VALUES (58, '213312', 'eqwfasdfa');
INSERT INTO `tb_user` VALUES (59, '14415143', '51435135');
INSERT INTO `tb_user` VALUES (60, 'shisan', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `tb_user` VALUES (61, 'zhangsan', 'fcea920f7412b5da7be0cf42b8c93759');
INSERT INTO `tb_user` VALUES (150, 'test-user1', '3d0faa930d336ba748607ab7076ebce2');
INSERT INTO `tb_user` VALUES (151, '3123213213', '6fdce2f14f4baf2d666fa13dfd8d1945');
INSERT INTO `tb_user` VALUES (152, 'lou2', '25f9e794323b453885f5181f1b624d0b');
INSERT INTO `tb_user` VALUES (153, 'lou3', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `tb_user` VALUES (154, 'lou1', 'e10adc3949ba59abbe56e057f20f883e');