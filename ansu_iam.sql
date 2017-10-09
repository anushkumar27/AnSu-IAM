-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 09, 2017 at 07:00 AM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ansu_iam`
--

-- --------------------------------------------------------

--
-- Table structure for table `app`
--

CREATE TABLE `app` (
  `appName` varchar(500) NOT NULL,
  `appId` varchar(500) NOT NULL,
  `expiryTime` int(100) NOT NULL DEFAULT '300'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `app`
--

INSERT INTO `app` (`appName`, `appId`, `expiryTime`) VALUES
('Text', '7ebe7894-4a80-47ce-b64f-3d9980a3ee45', 500),
('Text1', '0ef39447-a43d-4972-bcd7-e895e40f898d', 300);

-- --------------------------------------------------------

--
-- Table structure for table `token`
--

CREATE TABLE `token` (
  `token` varchar(500) NOT NULL,
  `uname` varchar(500) NOT NULL,
  `appId` varchar(500) NOT NULL,
  `toi` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `token`
--

INSERT INTO `token` (`token`, `uname`, `appId`, `toi`) VALUES
('d991e652-ab3c-4fa9-99d5-ee4f4053ecc8', 'sushi', '0ef39447-a43d-4972-bcd7-e895e40f898d', '2017-10-08 14:58:26'),
('9a10a0ba-7a5b-4cf7-877d-9c34d728f8e3', 'sushi', '7ebe7894-4a80-47ce-b64f-3d9980a3ee45', '2017-10-08 17:33:00');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `uname` varchar(500) NOT NULL,
  `uid` varchar(500) NOT NULL,
  `pass` varchar(500) NOT NULL,
  `appId` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uname`, `uid`, `pass`, `appId`) VALUES
('anush', 'b96ecc13-e227-4133-8746-98e445c3532b', '123', '0ef39447-a43d-4972-bcd7-e895e40f898d'),
('sushi', '80c7e75b-8a74-4542-847e-5263a87d255d', '123', '0ef39447-a43d-4972-bcd7-e895e40f898d'),
('sushi', 'a7038b29-c16b-4f2a-aeb3-74652c0524cb', '123', '7ebe7894-4a80-47ce-b64f-3d9980a3ee45');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `app`
--
ALTER TABLE `app`
  ADD PRIMARY KEY (`appName`),
  ADD UNIQUE KEY `appId` (`appId`);

--
-- Indexes for table `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`uname`,`appId`),
  ADD KEY `appId` (`appId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uname`,`appId`),
  ADD KEY `appId` (`appId`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `token`
--
ALTER TABLE `token`
  ADD CONSTRAINT `token_ibfk_1` FOREIGN KEY (`appId`) REFERENCES `app` (`appId`),
  ADD CONSTRAINT `token_ibfk_2` FOREIGN KEY (`uname`) REFERENCES `user` (`uname`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`appId`) REFERENCES `app` (`appId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
