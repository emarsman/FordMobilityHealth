/*
SQLyog Community v11.51 (32 bit)
MySQL - 5.6.20 : Database - mh_openxc_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mh_openxc_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mh_openxc_db`;

/*Table structure for table `vehicleregistrationdata_tb` */

DROP TABLE IF EXISTS `vehicleregistrationdata_tb`;

CREATE TABLE `vehicleregistrationdata_tb` (
  `vehicleId` int(8) NOT NULL AUTO_INCREMENT,
  `vehRegnNo` varchar(100) NOT NULL,
  `vehChasisNo` varchar(100) NOT NULL,
  `vehMake` varchar(100) NOT NULL,
  `vehModel` varchar(100) NOT NULL,
  `contactNo` varchar(15) NOT NULL,
  `emailId` varchar(100) NOT NULL,
  `createdOn` datetime NOT NULL,
  PRIMARY KEY (`vehicleId`)
) ENGINE=InnoDB AUTO_INCREMENT=100046 DEFAULT CHARSET=utf8;

/*Data for the table `vehicleregistrationdata_tb` */

insert  into `vehicleregistrationdata_tb`(`vehicleId`,`vehRegnNo`,`vehChasisNo`,`vehMake`,`vehModel`,`contactNo`,`emailId`,`createdOn`) values (100032,'AH17FT2387','QW5TY192TY8008233','Honda','Jazz','9871252427','mary@mail.com','2014-09-23 11:49:42'),(100033,'AH18MN165','MK7575RT11','Audi','A7','9733774453','kriti@mail.com','2014-09-23 11:51:09'),(100034,'AH18MN777','SS5643DD876','Ford','Figo','9946435213','ankur@mail.com','2014-09-23 11:52:26'),(100035,'AH18TJ16','PQ5678RTY67','Figo','Sunny','9878965467','arti@mail.com','2014-09-23 11:53:56'),(100037,'AH218TJ161','7896541239','Skoda','Laura','7896541239','durgesh@mail.com','2014-09-23 11:56:56'),(100038,'AH218TJ176','AB6575RT23','Audi','A7','8998564577','shalini@mail.com','2014-09-23 11:58:45'),(100039,'AH218TJ775','CG658AD8355','Audi','A7','8113376019','tamanna@mail.com','2014-09-23 12:00:04'),(100041,'AH218TT177','CG658AD8454','BMW','X5','9774565544','shiv@mail.com','2014-09-23 12:01:05'),(100042,'AM218TJ187','XZ1234EC587','Skoda','Laura','9999888821','pawan@mail.com','2014-09-23 12:02:16'),(100043,'AH23SJH18','SS5643DD876','Ford','Figo','9946435213','ankur@mail.com','2014-09-23 12:28:16'),(100044,'hgc','hgchgvhgc','gc hgv','hgc\'jgvjh','5755755547','hfchgv@gffc.gvg','2014-09-23 13:45:26'),(100045,'hghgvhgv','bjhbmhgc','mhgvhg','mhgchgc','8545494557','hgc@cgfg.vhb','2014-09-23 13:50:35');

/*Table structure for table `vehicleruntimedatarecord_tb` */

DROP TABLE IF EXISTS `vehicleruntimedatarecord_tb`;

CREATE TABLE `vehicleruntimedatarecord_tb` (
  `vehRunTimeId` int(6) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(8) NOT NULL,
  `vehSpeed` float NOT NULL,
  `latitude` varchar(40) NOT NULL,
  `longitude` varchar(40) NOT NULL,
  `timeStamp` datetime NOT NULL,
  `address` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`vehRunTimeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3621 DEFAULT CHARSET=utf8;

/*Data for the table `vehicleruntimedatarecord_tb` */

insert  into `vehicleruntimedatarecord_tb`(`vehRunTimeId`,`vehicleId`,`vehSpeed`,`latitude`,`longitude`,`timeStamp`,`address`) values (3533,100033,55.37,'28.577114883333333','77.30266728333332','2014-09-23 12:21:51','Delhi Noida Direct Flyway, Rose Garden, Sector 15A, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3535,100033,66.5,'28.580129333333335','77.29830173333333','2014-09-23 12:22:21','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3537,100033,56.06,'28.579110066666665','77.29295478333333','2014-09-23 12:22:51','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3539,100033,39.47,'28.578093516666666','77.29064183333334','2014-09-23 12:23:08','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3545,100035,5.96,'28.563891899999998','77.20761288333333','2014-09-23 12:34:37','Sri Aurobindo Marg, Block C 2, Ansari Nagar East, New Delhi, Delhi 110016'),(3546,100035,18.73,'28.56354431666667','77.20752471666667','2014-09-23 12:34:58','Sri Aurobindo Marg, Block V, Ansari Nagar West, New Delhi, Delhi 110016'),(3547,100037,5.96,'28.563891899999998','77.20761288333333','2014-09-23 12:38:37','Sri Aurobindo Marg, Block C 2, Ansari Nagar East, New Delhi, Delhi 110016'),(3548,100037,33.6,'28.563038433333336','77.20752678333334','2014-09-23 12:39:07','Sri Aurobindo Marg, Block V, Ansari Nagar West, New Delhi, Delhi 110016'),(3557,100038,64.52,'28.580129333333335','77.29830173333333','2014-09-23 12:43:05','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3560,100038,64.52,'28.580129333333335','77.29830173333333','2014-09-23 12:43:05','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3563,100038,64.52,'28.580129333333335','77.29830173333333','2014-09-23 12:43:05','Delhi Noida Direct Flyway, New Friends Colony, New Delhi, Delhi 110096'),(3566,100041,55.37,'28.577114883333333','77.30266728333332','2014-09-23 12:49:48','Delhi Noida Direct Flyway, Rose Garden, Sector 15A, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3567,100042,56.78,'28.563071633333333','77.316779','2014-09-23 12:53:26','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3568,100042,51.62,'28.560272216666664','77.31900451666667','2014-09-23 12:53:49','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3596,100043,40.71,'28.52006715','77.16271118333334','2014-09-23 13:11:49','Aruna Asaf Ali Marg, Pocket 1, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3597,100043,24.89,'28.521038283333336','77.16572491666668','2014-09-23 12:57:50','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3598,100043,33.45,'28.521038283333336','77.16572491666668','2014-09-23 13:12:19','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3599,100043,24.89,'28.521038283333336','77.16572491666668','2014-09-23 12:57:50','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3600,100043,8.52,'28.52251371666667','77.16818973333334','2014-09-23 13:12:49','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3601,100043,24.89,'28.521038283333336','77.16572491666668','2014-09-23 12:57:50','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3602,100043,29.31,'28.522899133333333','77.16831048333333','2014-09-23 13:13:19','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3603,100043,24.89,'28.521038283333336','77.16572491666668','2014-09-23 12:57:50','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3604,100043,29.98,'28.522899133333333','77.16831048333333','2014-09-23 13:13:20','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3605,100043,24.89,'28.521038283333336','77.16572491666668','2014-09-23 12:57:50','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3607,100044,40.54,'28.559907133333333','77.31958608333333','2014-09-23 13:46:27','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3608,100044,45.16,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3609,100044,45.84,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3610,100044,45.84,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3611,100044,56.55,'28.563071633333333','77.316779','2014-09-23 13:48:20','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3612,100044,45.84,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3613,100044,49.06,'28.562491700000002','77.31719835','2014-09-23 13:48:24','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3614,100044,45.84,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3615,100044,49.06,'28.562491700000002','77.31719835','2014-09-23 13:48:24','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3616,100044,45.84,'28.55831921666667','77.32264078333333','2014-09-23 13:46:57','Noida-Greater Noida Expressway, Sector 38, New Okhla Industrial Development Area, Uttar Pradesh 201301'),(3617,100045,23.54,'28.518771833333336','77.16037809999999','2014-09-23 13:51:06','Aruna Asaf Ali Marg, Pocket 1, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3618,100045,40.72,'28.52006715','77.16271118333334','2014-09-23 13:51:36','Aruna Asaf Ali Marg, Pocket 1, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3619,100045,33.21,'28.521038283333336','77.16572491666668','2014-09-23 13:52:06','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067'),(3620,100045,9.37,'28.52251371666667','77.16818973333334','2014-09-23 13:52:36','Aruna Asaf Ali Marg, Pocket 4, Sector B, Vasant Kunj, New Delhi, Delhi 110067');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
