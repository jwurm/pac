CREATE DATABASE  IF NOT EXISTS `conference` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `conference`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: conference
-- ------------------------------------------------------
-- Server version	5.5.28

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
-- Table structure for table `conference`
--

DROP TABLE IF EXISTS `conference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conference` (
  `id` int(11) NOT NULL,
  `description` varchar(200) NOT NULL,
  `end` date NOT NULL,
  `name` varchar(45) NOT NULL,
  `start` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conference`
--

LOCK TABLES `conference` WRITE;
/*!40000 ALTER TABLE `conference` DISABLE KEYS */;
INSERT INTO `conference` VALUES (1,'Java für Alle','2014-02-01','JAX','2014-01-01'),(15,'Spielemesse','2014-03-15','Gamescon','2014-03-01'),(20,'Automesse','2014-05-09','IAA','2014-05-02');
/*!40000 ALTER TABLE `conference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (33);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `capacity` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (2,12,'E785'),(12,5,'E504'),(14,50,'ME701'),(19,12,'M201'),(31,500,'Kantine');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `speaker`
--

DROP TABLE IF EXISTS `speaker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `speaker` (
  `id` int(11) NOT NULL,
  `description` varchar(200) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `speaker`
--

LOCK TABLES `speaker` WRITE;
/*!40000 ALTER TABLE `speaker` DISABLE KEYS */;
INSERT INTO `speaker` VALUES (3,'Hat Ahnung','Darko'),(4,'weiss nix','Ralf'),(21,'WoT lead designer','Sergej');
/*!40000 ALTER TABLE `speaker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `talk`
--

DROP TABLE IF EXISTS `talk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `talk` (
  `id` int(11) NOT NULL,
  `datetime` datetime NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `duration` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `conference_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3634ACBB14300E` (`room_id`),
  KEY `FK3634AC43FB1E4D` (`conference_id`),
  CONSTRAINT `FK3634AC43FB1E4D` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`id`),
  CONSTRAINT `FK3634ACBB14300E` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `talk`
--

LOCK TABLES `talk` WRITE;
/*!40000 ALTER TABLE `talk` DISABLE KEYS */;
INSERT INTO `talk` VALUES (5,'2014-01-01 15:00:00','OpenJPA und seine grottigen Exceptions',60,'OpenJPA sucks',1,2),(7,'2014-01-02 15:00:00','All the new stuff about java 7',60,'Java 7',1,2),(16,'2014-03-01 16:00:00','WoT for XBox is coming soon',100,'WoT',15,2),(24,'2014-01-01 16:00:00','Let\'s talk about REST again',60,'JAX-RX rockt!',1,2),(25,'2014-01-02 16:00:00','MyBatis is fun!',66,'MyBatis',1,2),(32,'2014-03-01 17:00:00','I\'m really hungry!',15,'Daily Meeting',15,31);
/*!40000 ALTER TABLE `talk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `talk_speaker_assignment`
--

DROP TABLE IF EXISTS `talk_speaker_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `talk_speaker_assignment` (
  `id` int(11) NOT NULL,
  `speaker_id` int(11) DEFAULT NULL,
  `talk_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK199F7180C7756042` (`speaker_id`),
  KEY `FK199F7180D611FE8D` (`talk_id`),
  CONSTRAINT `FK199F7180C7756042` FOREIGN KEY (`speaker_id`) REFERENCES `speaker` (`id`),
  CONSTRAINT `FK199F7180D611FE8D` FOREIGN KEY (`talk_id`) REFERENCES `talk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `talk_speaker_assignment`
--

LOCK TABLES `talk_speaker_assignment` WRITE;
/*!40000 ALTER TABLE `talk_speaker_assignment` DISABLE KEYS */;
INSERT INTO `talk_speaker_assignment` VALUES (6,3,5),(8,4,7),(17,4,16),(26,21,16),(27,4,25),(28,4,24),(30,3,16);
/*!40000 ALTER TABLE `talk_speaker_assignment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-23 18:49:02
