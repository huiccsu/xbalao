/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : xbalao

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-06-08 19:05:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_ad
-- ----------------------------
DROP TABLE IF EXISTS `t_ad`;
CREATE TABLE `t_ad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `img` varchar(500) NOT NULL COMMENT '图片广告位',
  `url` varchar(500) NOT NULL COMMENT '跳转地址',
  `desc` varchar(500) NOT NULL COMMENT '描述',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '广告位置 1:首页右边的广告  2:轮播广告',
  `istop` int(3) NOT NULL COMMENT '排序位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shorterImg` varchar(255) DEFAULT NULL,
  `shortImg` varchar(255) DEFAULT NULL COMMENT '展示小图标',
  `title` varchar(500) NOT NULL COMMENT '标题',
  `keywrolds` varchar(255) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL COMMENT '描述',
  `ctime_str` varchar(30) DEFAULT NULL,
  `ctime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `viewers` int(11) DEFAULT '0' COMMENT '浏览人数',
  `_viewers` int(11) DEFAULT '0' COMMENT '最开始初始的次数',
  `content` mediumtext COMMENT '新闻内容',
  `fromer` varchar(100) DEFAULT NULL COMMENT '来源',
  `editer` varchar(30) DEFAULT NULL COMMENT '编辑',
  `status` int(11) DEFAULT NULL COMMENT '0未发布 1：发布',
  `type` varchar(50) DEFAULT NULL COMMENT '新闻类型',
  `istop` int(3) DEFAULT '0' COMMENT '是否推荐',
  `srcDomain` varchar(4000) DEFAULT NULL,
  `srcId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `title` (`title`) USING BTREE,
  KEY `ctime` (`ctime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=749 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_article_url
-- ----------------------------
DROP TABLE IF EXISTS `t_article_url`;
CREATE TABLE `t_article_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT '抓取的URL',
  `domain` varchar(255) DEFAULT NULL COMMENT '域名',
  `rule` text COMMENT '规则',
  `status` int(1) DEFAULT NULL COMMENT '1:正常启动 0：未启用',
  `timeFormate` varchar(100) DEFAULT NULL,
  `netName` varchar(255) DEFAULT NULL COMMENT '网站名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_imgs
-- ----------------------------
DROP TABLE IF EXISTS `t_imgs`;
CREATE TABLE `t_imgs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `src` varchar(500) DEFAULT NULL,
  `deSrc` varchar(500) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '1:处理完 0：未处理 -1：代表出问题 -2：代表删除',
  `ctime` bigint(13) DEFAULT NULL COMMENT '创建时间',
  `dtime` bigint(13) DEFAULT NULL COMMENT '处理时间',
  `domain` varchar(255) DEFAULT NULL,
  `srcId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1447 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_type
-- ----------------------------
DROP TABLE IF EXISTS `t_type`;
CREATE TABLE `t_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '标签类型',
  `status` int(1) NOT NULL COMMENT '状态 0：正常 1：显示',
  `sorted` int(3) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
