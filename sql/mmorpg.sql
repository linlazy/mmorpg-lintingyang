/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : mmorpg

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2018-12-14 18:39:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `actorId` bigint(20) NOT NULL,
  `itemId` bigint(64) NOT NULL,
  `count` int(11) DEFAULT NULL,
  `ext` text,
  PRIMARY KEY (`actorId`,`itemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for scene
-- ----------------------------
DROP TABLE IF EXISTS `scene`;
CREATE TABLE `scene` (
  `actorId` bigint(20) NOT NULL,
  `sceneId` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`actorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for scene_entity
-- ----------------------------
DROP TABLE IF EXISTS `scene_entity`;
CREATE TABLE `scene_entity` (
  `sceneId` int(11) NOT NULL,
  `entities` text,
  PRIMARY KEY (`sceneId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `actorId` bigint(20) NOT NULL,
  `skills` text,
  PRIMARY KEY (`actorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `actorId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `mp` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `MPNextResumeTime` bigint(13) DEFAULT NULL,
  `HPNextResumeTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`actorId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;