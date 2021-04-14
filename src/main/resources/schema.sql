create database if not exists convert_app;
use convert_app;


DROP TABLE IF EXISTS `cpt_system_user` ;
CREATE TABLE IF NOT EXISTS `cpt_system_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uk` varchar(128) NOT NULL DEFAULT '' COMMENT '用户uk',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '用户中文名',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_login_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最近一次登录时间',
  PRIMARY KEY (`id`),
  KEY `uk_index` (`uk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
