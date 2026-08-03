-- 旧库迁移，勿对 schema.sql 新库执行。仅适用于仍包含 media_resource 和 project_detail.award_ids 的 MySQL 8 数据库。
-- 执行前请备份数据库；历史 MinIO URL 仅保留在图片库中，不迁移任何业务关联。

RENAME TABLE `media_resource` TO `attachment`;

ALTER TABLE `attachment`
  DROP INDEX `idx_ref`,
  DROP INDEX `idx_temporary`;

ALTER TABLE `attachment`
  CHANGE COLUMN `url` `legacy_url` VARCHAR(512) NULL COMMENT '历史MinIO访问地址',
  CHANGE COLUMN `title` `original_name` VARCHAR(255) NULL COMMENT '原文件名',
  DROP COLUMN `type`,
  DROP COLUMN `ref_id`,
  DROP COLUMN `ref_type`,
  DROP COLUMN `description`,
  ADD COLUMN `object_key` VARCHAR(512) NULL COMMENT '七牛云对象键' AFTER `id`,
  ADD COLUMN `mime_type` VARCHAR(128) NULL COMMENT 'MIME类型' AFTER `original_name`,
  ADD COLUMN `size` BIGINT UNSIGNED NULL COMMENT '文件字节数' AFTER `mime_type`,
  ADD COLUMN `status` VARCHAR(16) NOT NULL DEFAULT 'ready' COMMENT 'pending/ready' AFTER `size`;

ALTER TABLE `attachment`
  ADD UNIQUE KEY `uk_attachment_object_key` (`object_key`),
  ADD KEY `idx_attachment_status_create_time` (`status`, `create_time`);

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

INSERT INTO `project_award` (`project_id`, `award_id`, `sort_order`)
SELECT pd.`project_id`, awards.`award_id`, MIN(awards.`sort_order`)
FROM `project_detail` pd
JOIN JSON_TABLE(
  COALESCE(pd.`award_ids`, JSON_ARRAY()),
  '$[*]' COLUMNS (
    `sort_order` FOR ORDINALITY,
    `award_id` BIGINT PATH '$'
  )
) AS awards ON TRUE
GROUP BY pd.`project_id`, awards.`award_id`;

ALTER TABLE `project_detail`
  DROP COLUMN `award_ids`,
  ADD COLUMN `image_urls` JSON NULL COMMENT '轮播图片URL列表' AFTER `team_division`;

-- 历史记录没有可靠的七牛对象键、MIME 类型和大小，因此允许其字段为 NULL；新建库以 schema.sql 为准。
