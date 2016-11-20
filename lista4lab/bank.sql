-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 20 Lis 2016, 11:26
-- Wersja serwera: 10.1.19-MariaDB
-- Wersja PHP: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `bank`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `accounts`
--

CREATE TABLE `accounts` (
  `name` varchar(255) NOT NULL,
  `accountNr` varchar(26) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `money` double UNSIGNED NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `accounts`
--

INSERT INTO `accounts` (`name`, `accountNr`, `pass`, `money`) VALUES
('john', '1010', 'p455', 361.12),
('paul', '12345', 'c7ab5249f92242d8b99be1b5b40fc29de6efff3efc76648c33d28b262decd0acea5ca614fb22ce0eecd5e935023f708cbb1111845b9d1e77453b57d770e0d1c8e5627728adac5f6a063d463a4415b01cc1cda53f6a6b2d6d2a64ace8b12981cf', 8883.88),
('steve', '54321', '694103246cef0f21732089ad3b95b50b6266ae0acb4a04fe077849e45ad8eafbed02ddde06d23936de44e0d4f2eab40cafd32241e2d198c53e9e5dd162baa44f313e66f05a6c1ab77aaa763ddb95a6ee34e2b3db00de1e1a47c9b276a0f21704', 755);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `history`
--

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `accountTo` varchar(26) NOT NULL,
  `value` double NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `history`
--

INSERT INTO `history` (`id`, `name`, `accountTo`, `value`, `title`) VALUES
(89, 'paul', '1010', 12, 'nowy tytul'),
(90, 'paul', '1010', 20.12, 'przelew do steve''a'),
(91, 'paul', '54321', 1, 'czysty'),
(92, 'paul', '1010', 33, 'na rozwój rozszerzenia');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`name`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `accountNr` (`accountNr`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `history`
--
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=93;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
