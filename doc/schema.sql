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
  `application_year` SMALLINT UNSIGNED NOT NULL COMMENT '招新批次年份',
  `class_name` VARCHAR(50) NOT NULL COMMENT '班级名称',
  `major` VARCHAR(50) NOT NULL COMMENT '专业名称',
  `phone` VARCHAR(15) NOT NULL DEFAULT '' COMMENT '联系电话',
  `qq_number` VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'QQ账号',
  `department` VARCHAR(50) NOT NULL COMMENT '第一意向部门',
  `second_department` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '第二意向部门',
  `initial_directions` JSON NOT NULL COMMENT '第一阶段尝试方向',
  `introduction` TEXT NOT NULL COMMENT '个人介绍',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_student_year` (`student_id`, `application_year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户报名';

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

INSERT INTO `major_mapping` (`code`, `short_name`, `full_name`) VALUES
('0109', '环科', '环境科学'), ('0111', '环科', '环境科学'), ('0112', '环科', '环境科学'),
('0113', '科工', '环境科学与工程'), ('0114', '海洋', '海洋资源与环境'), ('0206', '人资', '人力资源管理'),
('0207', '会展', '会展经济与管理'), ('0208', '新闻', '新闻学'), ('0209', '旅游', '旅游管理与服务教育'),
('0210', '旅游', '旅游管理与服务教育'), ('0301', '环工', '环境工程'), ('0311', '环工', '环境工程'),
('0313', '设备', '环保设备工程'), ('0314', '环工', '环境工程'), ('0315', '工管', '工程管理'),
('0317', '水质', '水质科学与技术'), ('0318', '环工', '环境工程'), ('0401', '旅游', '旅游管理与服务教育'),
('0404', '食品', '食品质量与安全'), ('0405', '园林', '园林'), ('0408', '食品', '食品质量与安全'),
('0409', '生态', '环境生态工程'), ('0410', '园林', '园林'), ('0414', '食品', '食品质量与安全'),
('0415', '食品', '食品质量与安全'), ('0511', '室内', '室内艺术设计'), ('0513', '产品', '产品设计'),
('0515', '环设', '环境设计'), ('0517', '产品', '产品设计'), ('0518', '环设', '环境设计'),
('0519', '视传', '视觉传达设计'), ('0520', '产品', '产品设计'), ('0613', '物工', '物联网工程'),
('0614', '软工', '软件工程'), ('0616', '软工', '软件工程'), ('0617', '物工', '物联网工程'),
('0707', '物流', '物流管理'), ('0708', '营销', '市场营销'), ('0709', '物流', '物流管理'),
('0710', '国商', '国际商务'), ('0711', '营销', '市场营销'), ('0712', '造价', '工程造价'),
('1903', '大数据', '数据科学与大数据技术');

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
