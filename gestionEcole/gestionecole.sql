-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 30, 2023 at 05:31 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestionecole`
--

-- --------------------------------------------------------

--
-- Table structure for table `cour`
--

CREATE TABLE `cour` (
  `codeCour` varchar(30) NOT NULL,
  `nomCour` varchar(50) DEFAULT NULL,
  `horaire` date DEFAULT NULL,
  `ensaignantRespo` varchar(50) DEFAULT NULL,
  `salle` varchar(30) DEFAULT NULL,
  `semestre` varchar(10) DEFAULT NULL,
  `idResponsable` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `etudiant`
--

CREATE TABLE `etudiant` (
  `cne` varchar(10) NOT NULL,
  `nomEtudiant` varchar(30) DEFAULT NULL,
  `prenomEtudiant` varchar(30) DEFAULT NULL,
  `dateNaissance` date DEFAULT NULL,
  `addresse` varchar(50) DEFAULT NULL,
  `numTele` varchar(10) DEFAULT NULL,
  `semestre` varchar(10) DEFAULT NULL,
  `idResponsable` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `etudiant`
--

INSERT INTO `etudiant` (`cne`, `nomEtudiant`, `prenomEtudiant`, `dateNaissance`, `addresse`, `numTele`, `semestre`, `idResponsable`) VALUES
('A123456789', 'test', 'test', '2000-01-11', 'test', '0604906525', 'S3', 0);

-- --------------------------------------------------------

--
-- Table structure for table `inscription`
--

CREATE TABLE `inscription` (
  `idinscription` int(11) NOT NULL,
  `dateInscription` date DEFAULT NULL,
  `semestreInscription` varchar(10) DEFAULT NULL,
  `idResponsable` int(11) NOT NULL,
  `codeCour` varchar(30) NOT NULL,
  `note` decimal(2,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `responsable`
--

CREATE TABLE `responsable` (
  `idResponsable` int(11) NOT NULL,
  `nomResponsable` varchar(50) NOT NULL,
  `motPass` varchar(12) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `responsable`
--

INSERT INTO `responsable` (`idResponsable`, `nomResponsable`, `motPass`) VALUES
(1, 'test', '12345678');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cour`
--
ALTER TABLE `cour`
  ADD PRIMARY KEY (`codeCour`),
  ADD KEY `fk_idRespo` (`idResponsable`);

--
-- Indexes for table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`cne`),
  ADD KEY `fk_idRespoEt` (`idResponsable`);

--
-- Indexes for table `inscription`
--
ALTER TABLE `inscription`
  ADD PRIMARY KEY (`idinscription`),
  ADD KEY `idResponsable` (`idResponsable`),
  ADD KEY `codeCour` (`codeCour`);

--
-- Indexes for table `responsable`
--
ALTER TABLE `responsable`
  ADD PRIMARY KEY (`idResponsable`),
  ADD UNIQUE KEY `nomResponsable` (`nomResponsable`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cour`
--
ALTER TABLE `cour`
  ADD CONSTRAINT `fk_idRespo` FOREIGN KEY (`idResponsable`) REFERENCES `responsable` (`idResponsable`);

--
-- Constraints for table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `fk_idRespoEt` FOREIGN KEY (`idResponsable`) REFERENCES `responsable` (`idResponsable`);

--
-- Constraints for table `inscription`
--
ALTER TABLE `inscription`
  ADD CONSTRAINT `inscription_ibfk_1` FOREIGN KEY (`idResponsable`) REFERENCES `responsable` (`idResponsable`),
  ADD CONSTRAINT `inscription_ibfk_2` FOREIGN KEY (`codeCour`) REFERENCES `cour` (`codeCour`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
