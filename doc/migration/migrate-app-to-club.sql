-- 将旧 club-recruitment 数据库迁移到新的 club 数据库。
-- 前提：旧库仍保留 admin、application、award、major_mapping、message、project、project_detail、media_resource 表。
-- 本脚本不会修改旧 club-recruitment 库，也不会覆盖已有 club 库；请以具有 CREATE DATABASE 和跨库 SELECT 权限的 MySQL 8 用户执行。
-- 历史 MinIO 图片仅迁移 URL 元数据。对象二进制数据需在 MinIO 下线前另行复制到七牛云。

CREATE DATABASE `club` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `club`;

CREATE TABLE `admin` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` VARCHAR(128) NOT NULL COMMENT '登录用户名',
  `password_hash` CHAR(60) NOT NULL COMMENT '密码散列值',
  `role` VARCHAR(32) NOT NULL DEFAULT 'normal' COMMENT '管理员角色',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员账户';

CREATE TABLE `application` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `student_id` VARCHAR(20) NOT NULL COMMENT '学号',
  `class_name` VARCHAR(50) NOT NULL COMMENT '班级名称',
  `major` VARCHAR(50) NOT NULL COMMENT '专业名称',
  `phone` VARCHAR(15) NOT NULL DEFAULT '' COMMENT '联系电话',
  `qq_number` VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'QQ账号',
  `department` VARCHAR(50) NOT NULL COMMENT '第一意向部门',
  `second_department` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '第二意向部门',
  `interests` JSON NOT NULL COMMENT '兴趣方向',
  `introduction` TEXT NOT NULL COMMENT '个人介绍',
  `reason` TEXT NOT NULL COMMENT '加入原因',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户报名';

CREATE TABLE `award` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '奖项ID',
  `competition_name` VARCHAR(255) NOT NULL COMMENT '竞赛名称',
  `competition_track` VARCHAR(100) NULL COMMENT '赛道或组别',
  `competition_level` VARCHAR(50) NOT NULL COMMENT '竞赛级别',
  `award_level` VARCHAR(50) NOT NULL COMMENT '奖项级别',
  `winners` JSON NOT NULL COMMENT '获奖人',
  `year` INT NOT NULL COMMENT '获奖年份',
  `award_date` DATE NOT NULL COMMENT '获奖日期',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_award_year` (`year`),
  KEY `idx_award_competition_name` (`competition_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='奖项信息';

CREATE TABLE `major_mapping` (
  `code` CHAR(4) NOT NULL COMMENT '专业代号',
  `short_name` VARCHAR(20) NOT NULL COMMENT '专业简称',
  `full_name` VARCHAR(50) NOT NULL COMMENT '专业全称',
  PRIMARY KEY (`code`),
  KEY `idx_major_mapping_short_name` (`short_name`),
  KEY `idx_major_mapping_full_name` (`full_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='专业代号与名称映射';

CREATE TABLE `attachment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `object_key` VARCHAR(512) NOT NULL COMMENT '七牛云对象键',
  `legacy_url` VARCHAR(512) NULL COMMENT '历史迁移兼容的MinIO访问地址',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原文件名',
  `mime_type` VARCHAR(128) NOT NULL COMMENT 'MIME类型',
  `size` BIGINT UNSIGNED NOT NULL COMMENT '文件字节数',
  `status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT 'pending/ready',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_attachment_object_key` (`object_key`),
  KEY `idx_attachment_status_create_time` (`status`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图片库';

CREATE TABLE `message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `nickname` VARCHAR(50) NOT NULL COMMENT '用户昵称',
  `content` TEXT NOT NULL COMMENT '留言内容',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数量',
  `ip_address` VARCHAR(45) NULL COMMENT '用户IP地址',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户留言';

CREATE TABLE `project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `category` VARCHAR(50) NOT NULL COMMENT '项目分类',
  `cover_image` VARCHAR(512) NOT NULL COMMENT '封面图片URL',
  `title` VARCHAR(100) NOT NULL COMMENT '项目标题',
  `brief_intro` TEXT NULL COMMENT '简要介绍',
  `tech_stack_tags` JSON NULL COMMENT '技术栈标签',
  `team_members` JSON NULL COMMENT '团队成员',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目基本信息';

CREATE TABLE `project_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '项目详情ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `time_range` VARCHAR(100) NULL COMMENT '项目时间范围',
  `tech_stack_tags` JSON NULL COMMENT '详细技术栈标签',
  `description_md` TEXT NULL COMMENT 'Markdown项目介绍',
  `team_division` JSON NULL COMMENT '团队成员分工',
  `image_urls` JSON NULL COMMENT '轮播图片URL列表',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_detail_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目详情';

CREATE TABLE `project_award` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `award_id` BIGINT UNSIGNED NOT NULL COMMENT '奖项ID',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_award_project_id_award_id` (`project_id`, `award_id`),
  KEY `idx_project_award_project_id_sort_order` (`project_id`, `sort_order`),
  KEY `idx_project_award_award_id` (`award_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目奖项关联';

INSERT INTO `admin` SELECT * FROM `club-recruitment`.`admin`;
INSERT INTO `application` SELECT * FROM `club-recruitment`.`application`;
INSERT INTO `award` SELECT * FROM `club-recruitment`.`award`;
INSERT INTO `major_mapping` SELECT * FROM `club-recruitment`.`major_mapping`;
INSERT INTO `message` SELECT * FROM `club-recruitment`.`message`;
INSERT INTO `project` SELECT * FROM `club-recruitment`.`project`;

INSERT INTO `attachment` (
  `id`, `object_key`, `legacy_url`, `original_name`, `mime_type`, `size`, `status`, `create_time`, `update_time`
)
SELECT
  `id`, CONCAT('legacy/', `id`), `url`, COALESCE(NULLIF(`title`, ''), CONCAT('legacy-', `id`)),
  'application/octet-stream', 0, 'ready', `create_time`, `update_time`
FROM `club-recruitment`.`media_resource`;

INSERT INTO `project_detail` (
  `id`, `project_id`, `time_range`, `tech_stack_tags`, `description_md`, `team_division`, `image_urls`, `create_time`, `update_time`
)
SELECT
  detail.`id`, detail.`project_id`, detail.`time_range`, detail.`tech_stack_tags`, detail.`description_md`, detail.`team_division`,
  COALESCE(images.`image_urls`, JSON_ARRAY()), detail.`create_time`, detail.`update_time`
FROM `club-recruitment`.`project_detail` AS detail
LEFT JOIN (
  SELECT `ref_id`, JSON_ARRAYAGG(`url`) AS `image_urls`
  FROM `club-recruitment`.`media_resource`
  WHERE `ref_type` = 'project' AND `ref_id` IS NOT NULL
  GROUP BY `ref_id`
) AS images ON images.`ref_id` = detail.`project_id`;

INSERT INTO `project_award` (`project_id`, `award_id`, `sort_order`)
SELECT detail.`project_id`, awards.`award_id`, MIN(awards.`sort_order`)
FROM `club-recruitment`.`project_detail` AS detail
JOIN JSON_TABLE(
  COALESCE(detail.`award_ids`, JSON_ARRAY()),
  '$[*]' COLUMNS (`sort_order` FOR ORDINALITY, `award_id` BIGINT PATH '$')
) AS awards ON TRUE
GROUP BY detail.`project_id`, awards.`award_id`;
