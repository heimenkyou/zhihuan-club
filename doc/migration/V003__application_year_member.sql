-- V003：报名表支持分年度批次并新增成员表。
-- 适用范围：已按旧结构建表（application 含 interests、reason 且唯一键为 uk_application_student_id）的库。
-- 新库请直接使用 doc/schema.sql，无需执行本脚本；执行前请备份数据库。

-- 1. application 增加年份与第一阶段方向字段（年份先允许空，回填后再收紧）
ALTER TABLE `application`
  ADD COLUMN `application_year` SMALLINT UNSIGNED NULL COMMENT '招新批次年份' AFTER `student_id`,
  ADD COLUMN `initial_directions` JSON NULL COMMENT '第一阶段尝试方向' AFTER `second_department`;

-- 2. 存量数据回填：年份取创建年份，方向取原兴趣字段
UPDATE `application` SET `application_year` = YEAR(`create_time`) WHERE `application_year` IS NULL;
UPDATE `application` SET `initial_directions` = `interests` WHERE `initial_directions` IS NULL;

-- 3. 清理旧字段并收紧约束
ALTER TABLE `application`
  MODIFY COLUMN `application_year` SMALLINT UNSIGNED NOT NULL COMMENT '招新批次年份',
  MODIFY COLUMN `initial_directions` JSON NOT NULL COMMENT '第一阶段尝试方向',
  DROP COLUMN `interests`,
  DROP COLUMN `reason`,
  DROP INDEX `uk_application_student_id`,
  ADD UNIQUE KEY `uk_application_student_year` (`student_id`, `application_year`);

-- 4. 成员表
CREATE TABLE `member` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `student_id` VARCHAR(20) NOT NULL COMMENT '学号',
  `class_name` VARCHAR(50) NULL COMMENT '班级名称',
  `major` VARCHAR(50) NOT NULL COMMENT '专业名称',
  `phone` VARCHAR(15) NOT NULL DEFAULT '' COMMENT '联系电话',
  `qq_number` VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'QQ账号',
  `department` VARCHAR(50) NOT NULL COMMENT '所属部门',
  `join_year` SMALLINT UNSIGNED NOT NULL COMMENT '加入年份',
  `status` VARCHAR(16) NOT NULL DEFAULT 'active' COMMENT '成员状态：active/inactive',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_member_student_id` (`student_id`),
  KEY `idx_member_join_year` (`join_year`),
  KEY `idx_member_department` (`department`),
  KEY `idx_member_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='社团成员';
