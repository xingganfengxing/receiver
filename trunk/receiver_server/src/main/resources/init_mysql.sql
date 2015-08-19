/*==============================================================*/
/* init vod_bandwidth_server tables                                      */
/*==============================================================*/
CREATE TABLE `vod_bandwidth_server_20150127` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ptime` varchar(12) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '时间戳:一分钟粒度',
  `splatid` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '平台',
  `platid` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '子平台',
  `sip` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '服务器IP',
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '域名',
  `bandwidth` bigint(20) DEFAULT NULL COMMENT '带宽',
  PRIMARY KEY (`id`),
  KEY `ptime` (`ptime`) USING BTREE,
  KEY `splatid` (`splatid`) USING BTREE,
  KEY `platid` (`platid`) USING BTREE,
  KEY `host` (`host`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

