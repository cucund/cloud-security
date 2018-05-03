/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 50622
Source Host           : 127.0.0.1:3306
Source Database       : permission

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2018-05-03 18:05:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for permission_list
-- ----------------------------
DROP TABLE IF EXISTS `permission_list`;
CREATE TABLE `permission_list` (
  `PERMISSION_LIST_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERMISSION_LIST_CODE` varchar(30) DEFAULT NULL COMMENT '代码',
  `PERMISSION_CODE` varchar(8) DEFAULT NULL COMMENT '系统权限代码',
  `APPMANAGE_ICODE` varchar(30) DEFAULT NULL COMMENT 'app code',
  `PERMISSION_LIST_CLASS` varchar(80) DEFAULT NULL,
  `PERMISSION_LIST_ACTION` varchar(250) DEFAULT NULL COMMENT 'action',
  `PERMISSION_LIST_METHOD` varchar(250) DEFAULT NULL COMMENT 'method',
  `PERMISSION_LIST_SORT` int(11) DEFAULT '0' COMMENT '类别： 0：操作员权限判断    1：判断是否登陆(SYSPOP_CODE=-2) 2：公共权限不用判断(SYSPOP_CODE=-1) \r\n            ',
  `PERMISSION_LIST_TYPE` int(11) DEFAULT '0' COMMENT '类型 ： 0：正常 ',
  `PERMISSION_LIST_NAME` varchar(30) DEFAULT NULL COMMENT '功能名称',
  `PERMISSION_LOG_START` int(11) DEFAULT '0' COMMENT '标志 ： 0：不记录 1 记录',
  `PERMISSION_LOG_END` int(11) DEFAULT '0' COMMENT '标志 ： 0：不记录 1 记录',
  `PERMISSION_LOG_SNO` varchar(250) DEFAULT NULL COMMENT '业务号码解析 a.b.c',
  `PERMISSION_LOG_NNO` varchar(250) DEFAULT NULL COMMENT '业务号码解析 a.b.c',
  `GMT_CREATE` datetime DEFAULT NULL COMMENT '创建时间',
  `GMT_MODIFIED` datetime DEFAULT NULL COMMENT '修改时间',
  `MEMO` varchar(250) DEFAULT NULL COMMENT '备注',
  `DATA_STATE` int(11) DEFAULT NULL COMMENT '状态',
  `PERMISSION_LIST_FLAG` int(11) DEFAULT NULL,
  `TENANT_CODE` varchar(32) DEFAULT NULL,
  `PERMISSION_LIST_CACHE` int(2) DEFAULT '0' COMMENT '是否需要缓存',
  `PERMISSION_LIST_AUTH_LOGIN` int(2) DEFAULT NULL,
  PRIMARY KEY (`PERMISSION_LIST_ID`),
  UNIQUE KEY `UDX_UP_PERMISSION_LIST` (`PERMISSION_LIST_ACTION`,`PERMISSION_LIST_METHOD`,`APPMANAGE_ICODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
