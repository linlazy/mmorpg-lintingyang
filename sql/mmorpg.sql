/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : mmorpg

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-28 14:19:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for arena
-- ----------------------------
DROP TABLE IF EXISTS `arena`;
CREATE TABLE `arena` (
  `arenaId` bigint(20) NOT NULL,
  `actorId` bigint(20) NOT NULL,
  `killNum` int(11) DEFAULT NULL,
  `killedNum` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`arenaId`,`actorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `chatId` bigint(20) NOT NULL,
  `sourceId` bigint(20) DEFAULT NULL,
  `receiver` bigint(20) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
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
  `readStatus` bit(1) DEFAULT NULL,
  `rewardStatus` bit(1) DEFAULT NULL,
  `rewards` text,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`emailId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for guild
-- ----------------------------
DROP TABLE IF EXISTS `guild`;
CREATE TABLE `guild` (
  `guildId` bigint(20) NOT NULL,
  `gold` bigint(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`guildId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for guild_offline
-- ----------------------------
DROP TABLE IF EXISTS `guild_offline`;
CREATE TABLE `guild_offline` (
  `guildId` bigint(20) NOT NULL,
  `receiverId` bigint(20) DEFAULT NULL,
  `senderId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`guildId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for guild_player
-- ----------------------------
DROP TABLE IF EXISTS `guild_player`;
CREATE TABLE `guild_player` (
  `guildId` bigint(20) NOT NULL,
  `actorId` bigint(20) NOT NULL,
  `authLevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`guildId`,`actorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for guild_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `guild_warehouse`;
CREATE TABLE `guild_warehouse` (
  `guildId` bigint(20) NOT NULL,
  `gold` int(11) DEFAULT NULL,
  `itemId` bigint(20) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `ext` text,
  PRIMARY KEY (`guildId`)
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
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
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
  `profession` int(11) DEFAULT NULL,
  `gold` int(11) DEFAULT NULL,
  PRIMARY KEY (`actorId`)
) ENGINE=InnoDB AUTO_INCREMENT=4194311 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `goodsId` bigint(20) NOT NULL,
  `actorId` bigint(20) NOT NULL,
  `hasBuyTimes` int(11) DEFAULT NULL,
  `nextResetTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`goodsId`,`actorId`)
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
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `taskId` bigint(20) NOT NULL,
  `actorId` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `data` text,
  PRIMARY KEY (`taskId`,`actorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
