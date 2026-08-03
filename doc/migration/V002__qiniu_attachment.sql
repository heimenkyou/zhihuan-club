-- 将旧媒体资源表迁移为附件表。执行前请先备份数据库。
RENAME TABLE `media_resource` TO `attachment`;

ALTER TABLE `attachment`
  CHANGE COLUMN `url` `legacy_url` VARCHAR(512) NULL COMMENT '历史MinIO访问地址',
  CHANGE COLUMN `title` `original_name` VARCHAR(255) NULL COMMENT '原文件名',
  DROP COLUMN `description`,
  ADD COLUMN `object_key` VARCHAR(512) NULL COMMENT '七牛云对象键' AFTER `id`,
  ADD COLUMN `mime_type` VARCHAR(128) NULL COMMENT 'MIME类型' AFTER `type`,
  ADD COLUMN `size` BIGINT UNSIGNED NULL COMMENT '文件字节数' AFTER `mime_type`,
  ADD COLUMN `status` VARCHAR(16) NOT NULL DEFAULT 'ready' COMMENT 'pending/ready' AFTER `size`,
  ADD COLUMN `usage` VARCHAR(32) NULL COMMENT '业务用途' AFTER `ref_id`;

ALTER TABLE `attachment`
  DROP INDEX `idx_ref`,
  DROP INDEX `idx_temporary`,
  ADD UNIQUE KEY `uk_attachment_object_key` (`object_key`),
  ADD KEY `idx_attachment_status_create_time` (`status`, `create_time`),
  ADD KEY `idx_attachment_reference` (`ref_type`, `ref_id`);

-- 旧代码曾用 0 表示未引用，迁移后统一使用 NULL。
UPDATE `attachment` SET `ref_id` = NULL, `ref_type` = NULL WHERE `ref_id` = 0;

-- 历史 MinIO URL 无法可靠推导七牛 object_key，因此保留在 legacy_url 中并允许迁移记录的
-- object_key、mime_type、size、original_name 暂时为空。应用响应优先使用 legacy_url；新上传记录
-- 始终写入非空 object_key 并只按 object_key 操作七牛对象。历史数据迁移到七牛后，可补齐上述字段，
-- 清空 legacy_url，并按 doc/schema.sql 将这些字段调整为 NOT NULL。
