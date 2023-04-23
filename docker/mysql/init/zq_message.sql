create database if not exists teamup;
use teamup;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zq_message
-- ----------------------------
DROP TABLE IF EXISTS `zq_message`;
CREATE TABLE `zq_message`  (
  `id` char(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` json NOT NULL,
  `is_read` tinyint(1) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `receiver_id` bigint NULL DEFAULT NULL,
  `sender_id` bigint NULL DEFAULT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `zq_message_receiver_id_f34a07bd_fk_zq_user_id`(`receiver_id` ASC) USING BTREE,
  INDEX `zq_message_sender_id_a45eb526_fk_zq_user_id`(`sender_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zq_message
-- ----------------------------
INSERT INTO `zq_message` VALUES ('04150404543e4b569710eb8e31113ccd', '{\"type\": \"text\", \"content\": \"123\"}', 1, '2023-04-22 14:42:55.021000', 2, 2, 0);
INSERT INTO `zq_message` VALUES ('125dd4a299314b8889d5c365a7c5e4b1', '{\"type\": \"text\", \"content\": \"123\"}', 0, '2023-04-22 14:41:03.417000', 2, 2, 0);
INSERT INTO `zq_message` VALUES ('8b4c2eace2ad4da99154399d5b7a2894', '{\"type\": \"text\", \"content\": \"123\"}', 0, '2023-04-22 14:39:38.850000', 2, 2, 0);
INSERT INTO `zq_message` VALUES ('c689b03caa6c4b79a87cec373235e824', '{\"type\": \"text\", \"content\": \"123\"}', 0, '2023-04-22 14:54:21.905000', 3, 2, 0);
INSERT INTO `zq_message` VALUES ('ed2d2556f85e4352a1b703f08fbe1c56', '{\"type\": \"text\", \"content\": \"123\"}', 1, '2023-04-23 13:11:13.875000', 2, 3, 0);

SET FOREIGN_KEY_CHECKS = 1;
