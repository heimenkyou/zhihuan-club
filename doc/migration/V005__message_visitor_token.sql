-- V005：留言作者身份由 IP 改为浏览器持久化访客令牌。
-- 适用范围：已按 V004 之前结构建表的存量库；执行前请备份数据库。
-- 新库请直接使用 doc/schema.sql，无需执行本脚本。

ALTER TABLE `message`
  ADD COLUMN `author_token_hash` BINARY(32) NULL COMMENT '留言作者匿名令牌摘要' AFTER `content`,
  ADD INDEX `idx_message_author_token_hash` (`author_token_hash`);

-- 存量留言没有访客令牌，author_token_hash 为空，仅管理员可删除；不影响历史数据展示。
