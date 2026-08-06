-- V004：新增首页高光配置表，支持项目/活动两类高光展示。
-- 适用范围：已有 project 表、按 V003 之前结构建表的库；执行前请备份数据库。
-- 新库请直接使用 doc/schema.sql，无需执行本脚本。

CREATE TABLE `homepage_highlight` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '高光ID',
  `item_type` VARCHAR(16) NOT NULL COMMENT '类型：project/activity',
  `project_id` BIGINT NULL COMMENT '项目ID（项目类型使用）',
  `title` VARCHAR(100) NULL COMMENT '标题（活动类型使用）',
  `description` VARCHAR(500) NULL COMMENT '简介（活动类型使用）',
  `cover_image` VARCHAR(512) NULL COMMENT '封面图片URL（活动类型使用）',
  `activity_date` DATE NULL COMMENT '活动日期（活动类型使用，可空）',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_homepage_highlight_project_id` (`project_id`),
  KEY `idx_homepage_highlight_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='首页高光配置';

-- 初始化现有项目，避免升级后首页高光区域为空。
INSERT INTO `homepage_highlight` (`item_type`, `project_id`, `sort_order`)
SELECT 'project', `id`, ROW_NUMBER() OVER (ORDER BY `create_time` DESC, `id` DESC) - 1
FROM `project`;
