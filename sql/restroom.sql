-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: rest.yining.site    Database: restroom
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.37-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `device_board`
--

DROP TABLE IF EXISTS `device_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_board` (
  `boar_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `rest_room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`boar_id`),
  KEY `FKrnu3bw1vwih1dbv1gbefyxqwk` (`rest_room_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_board`
--

LOCK TABLES `device_board` WRITE;
/*!40000 ALTER TABLE `device_board` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_camera`
--

DROP TABLE IF EXISTS `device_camera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_camera` (
  `camera_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `rest_room_id` int(11) DEFAULT NULL,
  `live_url` varchar(255) DEFAULT NULL,
  `rtsp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`camera_id`),
  KEY `FKld32fx6oj3j5fp2jf55vov8ta` (`rest_room_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_camera`
--

LOCK TABLES `device_camera` WRITE;
/*!40000 ALTER TABLE `device_camera` DISABLE KEYS */;
INSERT INTO `device_camera` (`camera_id`, `create_time`, `ip`, `password`, `remark`, `status`, `username`, `rest_room_id`, `live_url`, `rtsp`) VALUES (1,'2019-05-23 20:54:42','192.168.10.5','ab123456',NULL,1,'admin',1,'http://47.99.207.5:88/stream/hls_1/film.m3u8','rtsp://admin:ab123456@192.168.10.5/h264/ch1/main/av_stream'),(2,'2019-05-23 20:56:27','192.168.11.72','hik123456',NULL,1,'admin',32,'http://47.99.207.5:88/stream/hls_2/film.m3u8','rtsp://admin:hik123456@192.168.11.72/h264/ch1/main/av_stream'),(3,'2019-05-23 21:09:57','192.168.11.52','hik123456',NULL,1,'admin',33,'http://47.99.207.5:88/stream/hls_3/film.m3u8','rtsp://admin:hik123456@192.168.11.52/h264/ch1/main/av_stream'),(4,'2019-05-23 21:12:43','192.168.11.62','hik123456',NULL,1,'admin',34,'http://47.99.207.5:88/stream/hls_4/film.m3u8','rtsp://admin:hik123456@192.168.11.62/h264/ch1/main/av_stream'),(5,'2019-05-23 21:14:17','192.168.11.22','hik123456',NULL,1,'admin',35,'http://47.99.207.5:88/stream/hls_5/film.m3u8','rtsp://admin:hik123456@192.168.11.22/h264/ch1/main/av_stream'),(6,'2019-05-23 21:15:22','192.168.11.32','hik123456',NULL,1,'admin',36,'http://47.99.207.5:88/stream/hls_6/film.m3u8','rtsp://admin:hik123456@192.168.11.32/h264/ch1/main/av_stream'),(7,'2019-05-23 21:16:44','192.168.11.82','hik123456',NULL,1,'admin',37,'http://47.99.207.5:88/stream/hls_7/film.m3u8','rtsp://admin:hik123456@192.168.11.82/h264/ch1/main/av_stream'),(8,'2019-05-23 21:17:48','192.168.11.42','hik123456',NULL,1,'admin',38,'http://47.99.207.5:88/stream/hls_8/film.m3u8','rtsp://admin:hik123456@192.168.11.42/h264/ch1/main/av_stream'),(9,'2019-05-23 21:19:28','192.168.11.92','hik123456',NULL,1,'admin',39,'http://47.99.207.5:88/stream/hls_9/film.m3u8','rtsp://admin:hik123456@192.168.11.92/h264/ch1/main/av_stream');
/*!40000 ALTER TABLE `device_camera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_gas`
--

DROP TABLE IF EXISTS `device_gas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_gas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `gas_device_id` int(11) DEFAULT NULL,
  `gas_device_parent_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `rest_room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtq0fffr82k8wi4oxou0ntf15q` (`rest_room_id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_gas`
--

LOCK TABLES `device_gas` WRITE;
/*!40000 ALTER TABLE `device_gas` DISABLE KEYS */;
INSERT INTO `device_gas` (`id`, `create_time`, `gas_device_id`, `gas_device_parent_id`, `status`, `type`, `rest_room_id`) VALUES (1,'2019-05-23 20:55:16',200015,100001,1,0,1),(2,'2019-05-23 20:55:30',200012,100001,1,2,1),(3,'2019-05-23 20:55:42',200014,100001,1,1,1),(4,'2019-05-23 20:55:52',200013,100001,1,3,1),(5,'2019-05-23 20:57:04',200032,100004,1,0,32),(6,'2019-05-23 21:08:43',200031,100004,1,2,32),(7,'2019-05-23 21:09:00',200030,100004,1,1,32),(8,'2019-05-23 21:09:09',200025,100004,1,3,32),(9,'2019-05-23 21:11:54',200004,100005,1,0,33),(10,'2019-05-23 21:12:05',200001,100005,1,2,33),(11,'2019-05-23 21:12:14',200003,100005,1,1,33),(12,'2019-05-23 21:12:23',200002,100005,1,3,33),(13,'2019-05-23 21:13:25',200024,100003,1,0,34),(14,'2019-05-23 21:13:40',200029,100003,1,2,34),(15,'2019-05-23 21:13:48',200027,100003,1,1,34),(16,'2019-05-23 21:13:55',200022,100003,1,3,34),(17,'2019-05-23 21:14:37',200011,100006,1,0,35),(18,'2019-05-23 21:14:47',2000009,100006,1,2,35),(19,'2019-05-23 21:14:55',200010,100006,1,1,35),(20,'2019-05-23 21:15:04',200016,100006,1,3,35),(21,'2019-05-23 21:15:41',200017,100008,1,0,36),(22,'2019-05-23 21:15:56',200020,100008,1,2,36),(23,'2019-05-23 21:16:04',200018,100008,1,1,36),(24,'2019-05-23 21:16:12',200019,100008,1,3,36),(25,'2019-05-23 21:17:06',200023,100002,1,0,37),(26,'2019-05-23 21:17:15',200028,100002,1,2,37),(27,'2019-05-23 21:17:22',200021,100002,1,1,37),(28,'2019-05-23 21:17:28',200026,100002,1,3,37),(29,'2019-05-23 21:18:05',200038,10003,1,0,38),(30,'2019-05-23 21:18:12',200039,10003,1,2,38),(31,'2019-05-23 21:18:20',200040,10003,1,1,38),(32,'2019-05-23 21:18:31',200037,10003,1,3,38),(33,'2019-05-23 21:19:44',200036,100009,1,0,39),(34,'2019-05-23 21:19:53',200034,100009,1,2,39),(35,'2019-05-23 21:20:00',200035,100009,1,1,39),(36,'2019-05-23 21:20:09',200033,100009,1,3,39);
/*!40000 ALTER TABLE `device_gas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `info_gas`
--

DROP TABLE IF EXISTS `info_gas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `info_gas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `score` float DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `gas_id` int(11) DEFAULT NULL,
  `rest_room_id` int(11) DEFAULT NULL,
  `func_id` int(11) DEFAULT NULL,
  `gas_device_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK359x3y0kv326d8pwmk200cp7l` (`gas_id`),
  KEY `FKf1jk2ynlmyxbrwbgfuwb84ss2` (`gas_device_id`),
  KEY `FKs2be53uuq3gbm5c3199hevf8k` (`rest_room_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `info_gas`
--

LOCK TABLES `info_gas` WRITE;
/*!40000 ALTER TABLE `info_gas` DISABLE KEYS */;
/*!40000 ALTER TABLE `info_gas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `info_passenger_flow`
--

DROP TABLE IF EXISTS `info_passenger_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `info_passenger_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `rest_room_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfkoi6q7vup4iorfal4yw01qk5` (`rest_room_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `info_passenger_flow`
--

LOCK TABLES `info_passenger_flow` WRITE;
/*!40000 ALTER TABLE `info_passenger_flow` DISABLE KEYS */;
INSERT INTO `info_passenger_flow` (`id`, `channel_name`, `create_time`, `ip`, `number`, `update_time`, `rest_room_id`) VALUES (1,NULL,'2019-05-23 21:27:51','192.168.11.21',0,'2019-05-23 21:27:51',35),(2,NULL,'2019-05-23 21:27:51','192.168.11.31',0,'2019-05-23 21:27:51',36),(3,NULL,'2019-05-23 21:27:51','192.168.10.4',0,'2019-05-23 21:27:51',1),(4,NULL,'2019-05-23 21:27:51','192.168.11.41',0,'2019-05-23 21:27:51',38),(5,NULL,'2019-05-23 21:27:51','192.168.11.51',0,'2019-05-23 21:27:51',33),(6,NULL,'2019-05-23 21:27:51','192.168.11.61',0,'2019-05-23 21:27:51',34),(7,NULL,'2019-05-23 21:27:52','192.168.11.91',0,'2019-05-23 21:27:52',39),(8,NULL,'2019-05-23 21:27:52','192.168.11.71',0,'2019-05-23 21:27:52',32),(9,NULL,'2019-05-23 21:27:52','192.168.11.81',0,'2019-05-23 21:27:52',37);
/*!40000 ALTER TABLE `info_passenger_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level` (
  `level_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `level_name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`level_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` (`level_id`, `create_time`, `level_name`, `remark`, `status`) VALUES (1,'2018-12-20 22:19:26','普通用户','just so so',1),(2,'2018-12-20 22:19:26','管理员','管理员',1);
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rest_room`
--

DROP TABLE IF EXISTS `rest_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rest_room` (
  `rest_room_id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cleaner` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `rest_room_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `people_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`rest_room_id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rest_room`
--

LOCK TABLES `rest_room` WRITE;
/*!40000 ALTER TABLE `rest_room` DISABLE KEYS */;
INSERT INTO `rest_room` (`rest_room_id`, `address`, `cleaner`, `create_time`, `region`, `remark`, `rest_room_name`, `status`, `img`, `ip`, `latitude`, `longitude`, `update_time`, `people_num`) VALUES (1,'和邦大厦公厕','','2018-12-20 22:38:02','浙江省,宁波市,鄞州区','','和邦大厦公共厕所',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.10.4',29.8349,121.553,'2019-05-23 21:27:51',452),(36,'下应南路公厕','','2019-05-05 09:33:16','浙江省,宁波市,鄞州区','','下应南路公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.31',29.8022,121.584,'2019-05-23 21:27:51',112),(32,'慧和大厦公厕','','2019-05-05 09:11:53','浙江省,宁波市,鄞州区','','慧和大厦公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.71',29.8318,121.542,'2019-05-23 21:27:52',525),(33,'钟公庙公厕','','2019-05-05 09:20:00','浙江省,宁波市,鄞州区','','钟公庙公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.51',29.8192,121.536,'2019-05-23 21:27:51',131),(34,'宁南南路公厕','','2019-05-05 09:28:07','浙江省,宁波市,鄞州区','','宁南南路公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.61',29.8016,121.54,'2019-05-23 21:27:51',247),(35,'陈婆渡公厕','','2019-05-05 09:31:42','浙江省,宁波市,鄞州区','','陈婆渡公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.21',29.7936,121.542,'2019-05-23 21:27:51',628),(37,'潘火派出所公厕','','2019-05-05 09:34:44','浙江省,宁波市,鄞州区','','潘火派出所公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.81',29.815,121.604,'2019-05-23 21:27:52',134),(38,'紫城南公厕','','2019-05-05 09:36:02','浙江省,宁波市,鄞州区','','紫城南公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.41',29.8234,121.59,'2019-05-23 21:27:51',248),(39,'紫城北公厕','','2019-05-05 09:36:56','浙江省,宁波市,鄞州区','','紫城北公厕',1,'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/map/pic/item/a9d3fd1f4134970a4f3e0c1398cad1c8a7865db8.jpg','192.168.11.91',29.8313,121.596,'2019-05-23 21:27:52',189);
/*!40000 ALTER TABLE `rest_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `rel_name` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `user_head_url` varchar(255) DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `level_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK4do7xomli51oxpc6b5j97jap0` (`level_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `create_time`, `department`, `password`, `rel_name`, `salt`, `user_head_url`, `user_status`, `username`, `level_id`) VALUES (1,'2019-05-23 20:45:02','厕所管理者','a107ff87e3df1ebe342ef5b75a3b92247e396c7ae27707c1c46df2fbbb0e8e71','超级管理员','17b3ddf2-70eb-4f0b-9537-78da7ba90a2f','https://upload.jianshu.io/users/upload_avatars/6639127/07e46067-9c7b-4df9-9c1f-b590818e295b.jpg',1,'baymin',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-23 21:50:06
