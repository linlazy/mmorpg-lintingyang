/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : mmorpg

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2018-12-24 16:59:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `chatId` bigint(20) NOT NULL,
  `sourceId` bigint(20) DEFAULT NULL,
  `receiver` bigint(20) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `chatType` int(11) DEFAULT NULL,
  PRIMARY KEY (`chatId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for email
-- ----------------------------
DROP TABLE IF EXISTS `email`;
CREATE TABLE `email` (
  `emailId` bigint(64) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `senderId` bigint(64) DEFAULT NULL,
  `sendTime` bigint(20) DEFAULT NULL,
  `receiverId` bigint(64) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`emailId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill` (
  `actorId` bigint(20) NOT NULL,
  `skillId` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `dressed` bit(1) NOT NULL,
  `nextCDResumeTimes` bigint(20) NOT NULL,
  PRIMARY KEY (`actorId`,`skillId`)
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
  `sceneId` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `mp` int(11) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `MPNextResumeTime` bigint(13) DEFAULT NULL,
  `HPNextResumeTime` bigint(13) DEFAULT NULL,
  PRIMARY KEY (`actorId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
