create database if not exists teamup;
use teamup;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zq_user
-- ----------------------------
DROP TABLE IF EXISTS `zq_user`;
CREATE TABLE `zq_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `email` varchar(254) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `is_staff` tinyint(1) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `is_superuser` tinyint(1) NOT NULL,
  `phone` varchar(13) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `nickname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `degree` int NOT NULL,
  `grade` int NOT NULL,
  `introduction` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `experience` json NOT NULL,
  `openid` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `union_id` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `avatar` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `contact` json NOT NULL,
  `student_id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `academy_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `openid`(`openid` ASC) USING BTREE,
  UNIQUE INDEX `union_id`(`union_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zq_user
-- ----------------------------
INSERT INTO `zq_user` VALUES (2, 'test_0', '', '', 0, 1, 0, '18312340000', '2023-04-19 21:52:19.000000', '2023-04-19 21:52:21.000000', '', 0, 0, '', '[]', NULL, 'b9b5b5b55b5b5b5b5b5b5b5b5b5b5b5b', 'test0', 'user\\avatar\\default.jpg', '[]', '2020000000000', NULL);
INSERT INTO `zq_user` VALUES (3, 'test_1', '', '', 0, 1, 0, '18312340001', '2023-04-19 21:52:19.000000', '2023-04-19 21:52:21.000000', '', 0, 0, '', '[]', NULL, 'c9c5c5c55c5c5c5c5c5c5c5c5c5c5c5c', 'test1', 'user\\avatar\\default.jpg', '[]', '2020000000001', NULL);
INSERT INTO `zq_user` VALUES (4, 'test_2', '', '', 0, 1, 0, '18312340002', '2023-04-19 21:52:19.000000', '2023-04-19 21:52:21.000000', '', 0, 0, '', '[]', NULL, 'd9d5d5d55d5d5d5d5d5d5d5d5d5d5d5d', 'test2', 'user\\avatar\\default.jpg', '[]', '2020000000002', NULL);
INSERT INTO `zq_user` VALUES (5, 'test_3', '', '', 0, 1, 0, '18312340003', '2023-04-19 21:52:19.000000', '2023-04-19 21:52:21.000000', '', 0, 0, '', '[]', NULL, 'e9e5e5e55e5e5e5e5e5e5e5e5e5e5e5e', 'test3', 'user\\avatar\\default.jpg', '[]', '2020000000003', NULL);

SET FOREIGN_KEY_CHECKS = 1;
