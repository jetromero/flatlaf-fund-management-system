-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2024 at 03:25 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `units_fms`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `cat_id` int(11) NOT NULL,
  `cat_name` varchar(100) NOT NULL,
  `cat_fee` int(11) NOT NULL,
  `cat_des` varchar(255) NOT NULL,
  `cat_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`cat_id`, `cat_name`, `cat_fee`, `cat_des`, `cat_date`) VALUES
(13, 'Edu Tour Confirmation Fee', 1000, 'Initial deposit for the Educational Tour', '2024-11-28 02:38:54'),
(49, 'Snacks', 100, 'Refreshments after the meeting', '2024-11-28 06:37:23'),
(50, 'IT Jersey', 360, 'For the Intramurals', '2024-11-28 07:17:27');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `pay_id` int(11) NOT NULL,
  `pay_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `payee_name` varchar(100) NOT NULL,
  `pay_fee` varchar(100) NOT NULL,
  `pay_amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`pay_id`, `pay_date`, `payee_name`, `pay_fee`, `pay_amount`) VALUES
(9, '2024-11-28 02:39:01', 'Romero, Jet Venson G.', 'Edu Tour Confirmation Fee', 1000),
(11, '2024-11-28 02:39:04', 'Rante, Abdullah Adel', 'Edu Tour Confirmation Fee', 1000),
(12, '2024-12-02 12:57:43', 'Alegre, Christian Rey', 'IT Jersey', 360),
(13, '2024-12-02 12:57:50', 'Autes, Jesus', 'Snacks', 100),
(14, '2024-12-02 12:58:05', 'Bucasas, Ely Christian', 'Edu Tour Confirmation Fee', 1000),
(15, '2024-12-02 15:34:06', 'Dominggono, Justine Rose E.', 'Snacks', 100),
(16, '2024-12-03 09:54:20', 'Salvar, Samantha', 'Edu Tour Confirmation Fee', 1000),
(17, '2024-12-03 18:33:00', 'Bebanco, Marjorie G.', 'IT Jersey', 360),
(18, '2024-12-03 18:00:58', 'Osma, Sherwin Dave V.', 'Edu Tour Confirmation Fee', 1000),
(21, '2024-12-03 18:32:54', 'Cañon, Rian James A.', 'Snacks', 100),
(22, '2024-12-03 18:31:50', 'Noval, Nicole Shayne M.', 'Edu Tour Confirmation Fee', 1000),
(23, '2024-12-03 18:32:47', 'Marabiles, Nathan Dhale H.', 'IT Jersey', 360),
(24, '2023-12-31 18:53:23', 'Corcelles, Reca A.', 'Edu Tour Confirmation Fee', 1000),
(25, '2024-01-01 18:53:23', 'Olacao, Joylyn Mae P.', 'Edu Tour Confirmation Fee', 1000),
(26, '2024-02-02 18:53:23', 'Mabini, Lovely Jean M.', 'Edu Tour Confirmation Fee', 1000),
(27, '2024-05-07 18:53:23', 'Catingub, Jury Mae', 'IT Jersey', 360),
(28, '2024-08-21 18:53:23', 'Morga, Jelian Mae T.', 'Snacks', 100);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` int(11) NOT NULL,
  `student_name` varchar(255) NOT NULL,
  `student_email` varchar(255) NOT NULL,
  `year_level` varchar(50) NOT NULL,
  `student_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `student_name`, `student_email`, `year_level`, `student_date`) VALUES
(1, 'Romero, Jet Venson G.', 'romero.jetvenson@evsu.edu.ph', '4th Year', '2024-12-15 12:59:46'),
(6, 'Dominggono, Justine Rose E.', ' ', '3rd Year', '2024-11-27 12:01:26'),
(9, 'Donasco, Jerson Jr L.', ' ', '3rd Year', '2024-11-27 12:01:30'),
(10, 'Noval, Nicole Shayne M.', ' ', '3rd Year', '2024-11-27 12:02:14'),
(11, 'Salvar, Samantha', ' ', '3rd Year', '2024-11-27 12:02:27'),
(12, 'Elisan, Princess', ' ', '2nd Year', '2024-12-03 15:22:32'),
(13, 'Romol, Merrel', ' ', '3rd Year', '2024-11-27 12:02:58'),
(14, 'Alegre, Christian Rey', ' ', '1st Year', '2024-12-03 15:21:52'),
(15, 'Marabiles, Nathan Dhale H.', ' ', '3rd Year', '2024-11-27 12:03:41'),
(16, 'Parrilla, Pedrito M. III', ' ', '2nd Year', '2024-12-03 15:22:28'),
(17, 'Perez, Cristina T.', ' ', '3rd Year', '2024-11-27 12:04:05'),
(18, 'Yray, Hazel', ' ', '3rd Year', '2024-11-27 12:04:42'),
(19, 'Corcelles, Reca A.', ' ', '3rd Year', '2024-11-27 12:17:38'),
(20, 'Bebanco, Marjorie G.', ' ', '1st Year', '2024-12-03 15:21:47'),
(21, 'Morga, Jelian Mae T.', ' ', '2nd Year', '2024-12-03 15:22:24'),
(22, 'Autes, Jesus', ' ', '2nd Year', '2024-12-03 15:22:37'),
(23, 'Cañedo, Klenth Joeseph D.', ' ', '3rd Year', '2024-11-27 12:06:54'),
(24, 'Mabini, Lovely Jean M.', ' ', '3rd Year', '2024-11-27 12:07:05'),
(25, 'Catingub, Jury Mae', ' ', '3rd Year', '2024-11-27 12:07:14'),
(26, 'Bucasas, Ely Christian', ' ', '2nd Year', '2024-12-03 15:22:14'),
(27, 'Rante, Abdullah Adel', ' ', '2nd Year', '2024-12-03 15:22:19'),
(28, 'Osma, Sherwin Dave V.', ' ', '3rd Year', '2024-11-27 12:07:47'),
(29, 'Olacao, Joylyn Mae P.', ' ', '3rd Year', '2024-11-27 12:07:56'),
(30, 'Sicsic, EarlJohn L .', ' ', '3rd Year', '2024-11-27 12:08:11'),
(31, 'Maraasin, Richard Jhon S.', ' ', '3rd Year', '2024-11-27 12:08:23'),
(32, 'Gatela, Christian', ' ', '3rd Year', '2024-11-27 12:08:37'),
(33, 'Cañon, Rian James A.', ' ', '3rd Year', '2024-11-27 12:08:48');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'units', '743e4a3b9a9e32c0de40ae9690dafdd0', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`cat_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`pay_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `pay_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `student_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
