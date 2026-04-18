-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 15 avr. 2026 à 01:19
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `univ_scheduler`
--

-- --------------------------------------------------------

--
-- Structure de la table `annee`
--

CREATE TABLE `annee` (
  `idAnnee` int(11) NOT NULL,
  `annee` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `annee`
--

INSERT INTO `annee` (`idAnnee`, `annee`) VALUES
(1, '2023-2024'),
(2, '2024-2025'),
(3, '2025-2026'),
(4, '2024-2025');

-- --------------------------------------------------------

--
-- Structure de la table `batiment`
--

CREATE TABLE `batiment` (
  `id_batiment` int(11) NOT NULL,
  `nom_batiment` varchar(100) NOT NULL,
  `localisation` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `batiment`
--

INSERT INTO `batiment` (`id_batiment`, `nom_batiment`, `localisation`) VALUES
(1, 'Batiment A', 'Campus Nord'),
(3, 'Batiment C', 'Campus Ouest'),
(5, 'Batiment D', 'Campus Est'),
(6, 'Batiment B', 'Campus Sud');

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `id_cours` int(11) NOT NULL,
  `matiere_id` int(11) DEFAULT NULL,
  `enseignant_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours`
--

INSERT INTO `cours` (`id_cours`, `matiere_id`, `enseignant_id`) VALUES
(90, 19, 18),
(91, 20, 16),
(92, 21, 17),
(93, 22, 23),
(94, 23, 19),
(95, 24, 22),
(96, 25, 16),
(97, 26, 21),
(98, 27, 18),
(99, 28, 19),
(100, 29, 4),
(101, 32, 22),
(102, 30, 20),
(103, 31, 21),
(104, 33, 16),
(105, 34, 4),
(106, 35, 17),
(107, 36, 4),
(108, 37, 20),
(109, 38, 19),
(110, 39, 18),
(111, 40, 4),
(112, 41, 16),
(113, 42, 17),
(114, 43, 21),
(115, 44, 22),
(116, 45, 23),
(117, 46, 19),
(118, 47, 20),
(119, 48, 18),
(120, 49, 17);

-- --------------------------------------------------------

--
-- Structure de la table `creneau_horaire`
--

CREATE TABLE `creneau_horaire` (
  `id_creneau` int(11) NOT NULL,
  `jour` varchar(20) NOT NULL,
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL
) ;

--
-- Déchargement des données de la table `creneau_horaire`
--

INSERT INTO `creneau_horaire` (`id_creneau`, `jour`, `heure_debut`, `heure_fin`) VALUES
(31, 'Lundi', '08:00:00', '10:00:00'),
(32, 'Lundi', '10:00:00', '12:00:00'),
(33, 'Lundi', '14:00:00', '16:00:00'),
(34, 'Mardi', '08:00:00', '10:00:00'),
(35, 'Mardi', '10:00:00', '12:00:00'),
(36, 'Mardi', '14:00:00', '16:00:00'),
(37, 'Mercredi', '08:00:00', '10:00:00'),
(38, 'Mercredi', '10:00:00', '12:00:00'),
(39, 'Mercredi', '14:00:00', '16:00:00'),
(40, 'Jeudi', '08:00:00', '10:00:00'),
(41, 'Jeudi', '10:00:00', '12:00:00'),
(42, 'Jeudi', '14:00:00', '16:00:00'),
(43, 'Vendredi', '08:00:00', '10:00:00'),
(44, 'Vendredi', '10:00:00', '12:00:00'),
(45, 'Vendredi', '14:00:00', '16:00:00'),
(46, 'Samedi', '08:00:00', '10:00:00'),
(47, 'Samedi', '10:00:00', '12:00:00'),
(48, 'Samedi', '14:00:00', '16:00:00'),
(49, 'Lundi', '16:00:00', '18:00:00'),
(50, 'Mardi', '16:00:00', '18:00:00'),
(51, 'Mercredi', '16:00:00', '18:00:00'),
(52, 'Jeudi', '16:00:00', '18:00:00'),
(53, 'Vendredi', '16:00:00', '18:00:00'),
(54, 'Samedi', '16:00:00', '18:00:00');

-- --------------------------------------------------------

--
-- Structure de la table `equipement`
--

CREATE TABLE `equipement` (
  `id_equipement` int(11) NOT NULL,
  `nom_equipement` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `equipement`
--

INSERT INTO `equipement` (`id_equipement`, `nom_equipement`, `description`) VALUES
(1, 'Vidéoprojecteur', 'Projecteur numérique haute définition'),
(2, 'Tableau Interactif', 'Tableau blanc interactif tactile'),
(3, 'Climatisation', 'Système de climatisation'),
(4, 'Tableau Blanc', 'Tableau blanc classique'),
(5, 'Ordinateurs', 'Postes informatiques pour TP'),
(6, 'Sono', 'Système de sonorisation'),
(7, 'Caméra', 'Caméra de surveillance et enregistrement'),
(8, 'WiFi', 'Accès réseau sans fil');

-- --------------------------------------------------------

--
-- Structure de la table `groupe`
--

CREATE TABLE `groupe` (
  `idGroupe` int(11) NOT NULL,
  `nomGroupe` varchar(100) NOT NULL,
  `effectif` int(11) NOT NULL,
  `annee_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `groupe`
--

INSERT INTO `groupe` (`idGroupe`, `nomGroupe`, `effectif`, `annee_id`) VALUES
(23, 'L1-MI-G1', 40, 1),
(24, 'L1-MI-G2', 38, 1),
(25, 'L1-MI-G3', 35, 1),
(26, 'L2-MI-G1', 35, 1),
(27, 'L2-MI-G2', 33, 1),
(28, 'L3-MI-G1', 30, 1),
(29, 'L3-MI-G2', 28, 1),
(30, 'L1-GI-G1', 38, 1),
(31, 'L1-GI-G2', 36, 1),
(32, 'L1-GI-G3', 34, 1),
(33, 'L2-GI-G1', 32, 1),
(34, 'L2-GI-G2', 30, 1),
(35, 'L3-GI-G1', 28, 1),
(36, 'L3-GI-G2', 26, 1),
(37, 'L1-PC-G1', 40, 1),
(38, 'L1-PC-G2', 38, 1),
(39, 'L1-PC-G3', 35, 1),
(40, 'L2-PC-G1', 33, 1),
(41, 'L2-PC-G2', 30, 1),
(42, 'L3-PC-G1', 28, 1),
(43, 'L3-PC-G2', 25, 1),
(44, 'M1-INFO', 25, 1),
(45, 'M2-INFO', 20, 1);

-- --------------------------------------------------------

--
-- Structure de la table `matiere`
--

CREATE TABLE `matiere` (
  `id_matiere` int(11) NOT NULL,
  `nom_matiere` varchar(100) NOT NULL,
  `volume_horaire` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `matiere`
--

INSERT INTO `matiere` (`id_matiere`, `nom_matiere`, `volume_horaire`) VALUES
(19, 'Algorithmique', 45),
(20, 'Programmation C', 45),
(21, 'Mathématiques Discrètes', 45),
(22, 'Anglais Technique', 30),
(23, 'Communication', 20),
(24, 'Programmation Java', 60),
(25, 'Base de Données', 45),
(26, 'Réseaux', 30),
(27, 'Systèmes d exploitation', 30),
(28, 'Programmation Web', 30),
(29, 'Génie Logiciel', 45),
(30, 'Intelligence Artificielle', 45),
(31, 'Machine Learning', 45),
(32, 'Sécurité Informatique', 30),
(33, 'Cloud Computing', 30),
(34, 'Gestion de Projet', 30),
(35, 'Analyse Mathématique', 60),
(36, 'Algèbre Linéaire', 45),
(37, 'Probabilités Statistiques', 45),
(38, 'Recherche Opérationnelle', 45),
(39, 'Calcul Numérique', 45),
(40, 'Architecture des Systèmes', 45),
(41, 'Systèmes Embarqués', 45),
(42, 'Electronique Numérique', 45),
(43, 'Traitement du Signal', 45),
(44, 'Robotique', 30),
(45, 'Mécanique', 60),
(46, 'Thermodynamique', 45),
(47, 'Chimie Organique', 45),
(48, 'Electrostatique', 45),
(49, 'Optique', 45),
(50, 'Robotique', 30),
(51, 'Mécanique', 60),
(52, 'Thermodynamique', 45),
(53, 'Chimie Organique', 45),
(54, 'Electrostatique', 45),
(55, 'Optique', 45);

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

CREATE TABLE `reservation` (
  `id_reservation` int(11) NOT NULL,
  `salle_id` int(11) DEFAULT NULL,
  `creneau_id` int(11) DEFAULT NULL,
  `date_reservation` date NOT NULL,
  `utilisateur_id` int(11) DEFAULT NULL,
  `statut` varchar(50) DEFAULT 'EN_ATTENTE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id_reservation`, `salle_id`, `creneau_id`, `date_reservation`, `utilisateur_id`, `statut`) VALUES
(4, 7, 31, '2026-03-16', 16, 'Annule'),
(5, 35, 34, '2026-03-31', 17, 'En_attente');

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `role`
--

INSERT INTO `role` (`id`, `nom`) VALUES
(1, 'Admin'),
(2, 'Enseignant'),
(3, 'Etudiant'),
(4, 'Gestionnaire');

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

CREATE TABLE `salle` (
  `id_salle` int(11) NOT NULL,
  `numero` varchar(50) DEFAULT NULL,
  `capacite` int(11) DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  `batiment_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `salle`
--

INSERT INTO `salle` (`id_salle`, `numero`, `capacite`, `type`, `batiment_id`) VALUES
(7, 'A1', 25, 'TP', 1),
(8, 'A2', 200, 'AMPHI', 1),
(9, 'B1', 200, 'AMPHI', 6),
(10, 'B2', 40, 'TD', 6),
(11, 'C1', 200, 'AMPHI', 3),
(12, 'C2', 25, 'TP', 3),
(13, 'C3', 40, 'TD', 3),
(14, 'D1', 200, 'AMPHI', 5),
(15, 'D2', 25, 'TP', 5),
(16, 'D3', 200, 'AMPHI', 5),
(17, 'A3', 200, 'AMPHI', 1),
(18, 'A4', 200, 'AMPHI', 1),
(19, 'B3', 25, 'TP', 6),
(20, 'B4', 200, 'AMPHI', 6),
(21, 'C4', 200, 'AMPHI', 3),
(23, 'A5', 200, 'AMPHI', 1),
(24, 'A6', 40, 'TD', 1),
(25, 'A7', 25, 'TP', 1),
(26, 'B5', 200, 'AMPHI', 6),
(27, 'B6', 40, 'TD', 6),
(28, 'B7', 25, 'TP', 6),
(29, 'C5', 200, 'AMPHI', 3),
(30, 'C6', 40, 'TD', 3),
(31, 'C7', 25, 'TP', 3),
(32, 'D4', 200, 'AMPHI', 5),
(33, 'D5', 40, 'TD', 5),
(34, 'A8', 200, 'AMPHI', 1),
(35, 'A9', 200, 'AMPHI', 1),
(36, 'B8', 200, 'AMPHI', 6),
(37, 'B9', 200, 'AMPHI', 6),
(40, 'A10', 200, 'AMPHI', 1),
(41, 'B10', 200, 'AMPHI', 6),
(42, 'C8', 200, 'AMPHI', 3),
(43, 'C9', 200, 'AMPHI', 3),
(44, 'C10', 200, 'AMPHI', 3),
(45, 'B11', 200, 'AMPHI', 6),
(47, 'C11', 200, 'AMPHI', 3);

-- --------------------------------------------------------

--
-- Structure de la table `salle_equipement`
--

CREATE TABLE `salle_equipement` (
  `id_salle` int(11) NOT NULL,
  `id_equipement` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `salle_equipement`
--

INSERT INTO `salle_equipement` (`id_salle`, `id_equipement`) VALUES
(7, 3),
(7, 4),
(7, 5),
(7, 8),
(8, 1),
(8, 3),
(8, 4),
(8, 6),
(9, 1),
(9, 3),
(9, 4),
(9, 6),
(10, 3),
(10, 4),
(10, 8),
(11, 1),
(11, 3),
(11, 4),
(11, 6),
(12, 3),
(12, 4),
(12, 5),
(12, 8),
(13, 3),
(13, 4),
(13, 8),
(14, 1),
(14, 3),
(14, 4),
(14, 6),
(15, 3),
(15, 4),
(15, 5),
(15, 8),
(16, 1),
(16, 3),
(16, 4),
(16, 6),
(17, 1),
(17, 3),
(17, 4),
(17, 6),
(18, 1),
(18, 3),
(18, 4),
(18, 6),
(19, 3),
(19, 4),
(19, 5),
(19, 8),
(20, 1),
(20, 3),
(20, 4),
(20, 6),
(21, 1),
(21, 3),
(21, 4),
(21, 6),
(23, 1),
(23, 3),
(23, 4),
(23, 6),
(24, 3),
(24, 4),
(24, 8),
(25, 3),
(25, 4),
(25, 5),
(25, 8),
(26, 1),
(26, 3),
(26, 4),
(26, 6),
(27, 3),
(27, 4),
(27, 8),
(28, 3),
(28, 4),
(28, 5),
(28, 8),
(29, 1),
(29, 3),
(29, 4),
(29, 6),
(30, 3),
(30, 4),
(30, 8),
(31, 3),
(31, 4),
(31, 5),
(31, 8),
(32, 1),
(32, 3),
(32, 4),
(32, 6),
(33, 3),
(33, 4),
(33, 8),
(34, 1),
(34, 3),
(34, 4),
(34, 6),
(35, 1),
(35, 3),
(35, 4),
(35, 6),
(36, 1),
(36, 3),
(36, 4),
(36, 6),
(37, 1),
(37, 3),
(37, 4),
(37, 6),
(40, 1),
(40, 3),
(40, 4),
(40, 6),
(41, 1),
(41, 3),
(41, 4),
(41, 6),
(42, 1),
(42, 3),
(42, 4),
(42, 6),
(43, 1),
(43, 3),
(43, 4),
(43, 6),
(44, 1),
(44, 3),
(44, 4),
(44, 6),
(45, 1),
(45, 3),
(45, 4),
(45, 6),
(47, 1),
(47, 3),
(47, 4),
(47, 6);

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

CREATE TABLE `seance` (
  `id_seance` int(11) NOT NULL,
  `cours_id` int(11) DEFAULT NULL,
  `salle_id` int(11) DEFAULT NULL,
  `creneau_id` int(11) DEFAULT NULL,
  `groupe_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id_seance`, `cours_id`, `salle_id`, `creneau_id`, `groupe_id`) VALUES
(1240, 90, 8, 31, 23),
(1241, 90, 17, 31, 24),
(1242, 90, 23, 31, 25),
(1243, 92, 8, 34, 23),
(1244, 92, 17, 34, 24),
(1245, 92, 23, 34, 25),
(1246, 106, 8, 37, 23),
(1247, 106, 17, 37, 24),
(1248, 106, 23, 37, 25),
(1249, 107, 8, 40, 23),
(1250, 107, 17, 40, 24),
(1251, 107, 23, 40, 25),
(1252, 93, 8, 43, 23),
(1253, 93, 17, 43, 24),
(1254, 93, 23, 43, 25),
(1255, 94, 8, 46, 23),
(1256, 94, 17, 46, 24),
(1257, 94, 23, 46, 25),
(1258, 91, 7, 32, 23),
(1259, 91, 25, 35, 24),
(1260, 91, 7, 38, 25),
(1261, 95, 18, 32, 26),
(1262, 95, 34, 32, 27),
(1263, 97, 18, 35, 26),
(1264, 97, 34, 35, 27),
(1265, 98, 18, 38, 26),
(1266, 98, 34, 38, 27),
(1267, 108, 18, 41, 26),
(1268, 108, 34, 41, 27),
(1269, 109, 18, 44, 26),
(1270, 109, 34, 44, 27),
(1271, 110, 18, 47, 26),
(1272, 110, 34, 47, 27),
(1273, 96, 7, 33, 26),
(1274, 96, 25, 36, 27),
(1275, 99, 35, 33, 28),
(1276, 99, 40, 33, 29),
(1277, 100, 35, 36, 28),
(1278, 100, 40, 36, 29),
(1279, 101, 35, 39, 28),
(1280, 101, 40, 39, 29),
(1281, 109, 35, 42, 28),
(1282, 109, 40, 42, 29),
(1283, 110, 35, 45, 28),
(1284, 110, 40, 45, 29),
(1285, 93, 35, 48, 28),
(1286, 93, 40, 48, 29),
(1287, 90, 9, 31, 30),
(1288, 90, 20, 31, 31),
(1289, 90, 26, 31, 32),
(1290, 92, 9, 34, 30),
(1291, 92, 20, 34, 31),
(1292, 92, 26, 34, 32),
(1293, 111, 9, 37, 30),
(1294, 111, 20, 37, 31),
(1295, 111, 26, 37, 32),
(1296, 113, 9, 40, 30),
(1297, 113, 20, 40, 31),
(1298, 113, 26, 40, 32),
(1299, 93, 9, 43, 30),
(1300, 93, 20, 43, 31),
(1301, 93, 26, 43, 32),
(1302, 94, 9, 46, 30),
(1303, 94, 20, 46, 31),
(1304, 94, 26, 46, 32),
(1305, 91, 19, 32, 30),
(1306, 91, 28, 35, 31),
(1307, 91, 19, 38, 32),
(1308, 95, 36, 32, 33),
(1309, 95, 37, 32, 34),
(1310, 97, 36, 35, 33),
(1311, 97, 37, 35, 34),
(1312, 98, 36, 38, 33),
(1313, 98, 37, 38, 34),
(1314, 112, 36, 41, 33),
(1315, 112, 37, 41, 34),
(1316, 114, 36, 44, 33),
(1317, 114, 37, 44, 34),
(1318, 111, 36, 47, 33),
(1319, 111, 37, 47, 34),
(1320, 96, 19, 33, 33),
(1321, 96, 28, 36, 34),
(1322, 99, 41, 33, 35),
(1323, 99, 45, 33, 36),
(1324, 100, 41, 36, 35),
(1325, 100, 45, 36, 36),
(1326, 101, 41, 39, 35),
(1327, 101, 45, 39, 36),
(1328, 115, 41, 42, 35),
(1329, 115, 45, 42, 36),
(1330, 114, 41, 45, 35),
(1331, 114, 45, 45, 36),
(1332, 93, 41, 48, 35),
(1333, 93, 45, 48, 36),
(1334, 90, 11, 31, 37),
(1335, 90, 21, 31, 38),
(1336, 90, 29, 31, 39),
(1337, 92, 11, 34, 37),
(1338, 92, 21, 34, 38),
(1339, 92, 29, 34, 39),
(1340, 118, 11, 37, 37),
(1341, 118, 21, 37, 38),
(1342, 118, 29, 37, 39),
(1343, 116, 11, 40, 37),
(1344, 116, 21, 40, 38),
(1345, 116, 29, 40, 39),
(1346, 93, 11, 43, 37),
(1347, 93, 21, 43, 38),
(1348, 93, 29, 43, 39),
(1349, 94, 11, 46, 37),
(1350, 94, 21, 46, 38),
(1351, 94, 29, 46, 39),
(1352, 116, 12, 32, 37),
(1353, 116, 31, 35, 38),
(1354, 116, 12, 38, 39),
(1355, 97, 42, 32, 40),
(1356, 97, 43, 32, 41),
(1357, 98, 42, 35, 40),
(1358, 98, 43, 35, 41),
(1359, 117, 42, 38, 40),
(1360, 117, 43, 38, 41),
(1361, 119, 42, 41, 40),
(1362, 119, 43, 41, 41),
(1363, 120, 42, 44, 40),
(1364, 120, 43, 44, 41),
(1365, 116, 42, 47, 40),
(1366, 116, 43, 47, 41),
(1367, 118, 12, 33, 40),
(1368, 118, 31, 36, 41),
(1369, 99, 44, 33, 42),
(1370, 99, 47, 33, 43),
(1371, 100, 44, 36, 42),
(1372, 100, 47, 36, 43),
(1373, 101, 44, 39, 42),
(1374, 101, 47, 39, 43),
(1375, 117, 44, 42, 42),
(1376, 117, 47, 42, 43),
(1377, 120, 44, 45, 42),
(1378, 120, 47, 45, 43),
(1379, 93, 44, 48, 42),
(1380, 93, 47, 48, 43),
(1381, 102, 14, 31, 44),
(1382, 103, 14, 34, 44),
(1383, 104, 14, 37, 44),
(1384, 105, 14, 40, 44),
(1385, 100, 14, 43, 44),
(1386, 101, 14, 46, 44),
(1387, 102, 16, 32, 45),
(1388, 103, 16, 35, 45),
(1389, 104, 16, 38, 45),
(1390, 105, 16, 41, 45),
(1391, 100, 16, 44, 45),
(1392, 101, 16, 47, 45);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `specialite` varchar(100) DEFAULT NULL,
  `departement` varchar(100) DEFAULT NULL,
  `matricule` varchar(50) DEFAULT NULL,
  `filiere` varchar(100) DEFAULT NULL,
  `niveau` varchar(50) DEFAULT NULL,
  `service` varchar(100) DEFAULT NULL,
  `groupe_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `mot_de_passe`, `role_id`, `specialite`, `departement`, `matricule`, `filiere`, `niveau`, `service`, `groupe_id`) VALUES
(1, 'Diallo', 'Mamadou', 'admin@univ.sn', 'admin123', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 'Sow', 'Fatou', 'gestionnaire@univ.sn', 'gest123', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(4, 'Diop', 'Moussa', 'enseignant@univ.sn', 'ens123', 2, 'Genie Logiciel', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(6, 'DIEYE', 'Amed', 'mouha@univ.sn', 'mouha123', 3, NULL, NULL, 'L2-2024-002', 'Math-Info', 'L2', NULL, 26),
(7, 'GUEYE', 'Dame', 'dame@univ.sn', 'dame123', 3, NULL, NULL, 'L2-2024-003', 'Math-Info', 'L1', NULL, 23),
(8, 'DIOP', 'Aliou', 'aliou@univ.sn', 'diop123', 4, NULL, NULL, NULL, NULL, NULL, '', NULL),
(9, 'THIAM', 'Fallou', 'fal@univ.sn', 'thiam123', 3, NULL, NULL, 'L1-2024-001', 'Math-Info', 'L1', NULL, 23),
(10, 'THIAM', 'Lahat', 'lat@gmail.com', 'lat123', 3, NULL, NULL, 'L1-2024-002', 'Math-Info', 'L1', NULL, 24),
(16, 'Ngom', 'Ibrahima', 'ens2@univ.sn', 'ens2@univ.sn', 2, 'Base de données', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(17, 'Diallo', 'Aliou', 'ens1@univ.sn', 'ens1123', 2, 'Systèmes d exploitation', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(18, 'Thiam', 'Issa', 'ens3@univ.sn', 'ens3123', 2, 'Algorithmique', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(19, 'Diop', 'Mamadou', 'ens4@univ.sn', 'ens4123', 2, 'Programmation Web', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(20, 'Sarr', 'Amadou', 'ens5@univ.sn', 'ens5123', 2, 'Mathématiques', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(21, 'Mbaye', 'Cheikh', 'ens6@univ.sn', 'ens6123', 2, 'Réseaux', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(22, 'Sene', 'idy', 'ens7@univ.sn', 'ens7@univ.sn', 2, 'Programmation Java', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(23, 'Sow', 'Babacar', 'ens8@univ.sn', 'ens8123', 2, 'Anglais', 'Informatique', NULL, NULL, NULL, NULL, NULL),
(24, 'DIALLO', 'Moussa', 'moussa.diallo@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-001', 'Math-Info', 'L1', NULL, 23),
(25, 'SECK', 'Fatou', 'fatou.seck@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-002', 'Math-Info', 'L1', NULL, 23),
(26, 'NDIAYE', 'Ibrahima', 'ibrahima.ndiaye@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-003', 'Math-Info', 'L1', NULL, 23),
(27, 'FALL', 'Aminata', 'aminata.fall@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-004', 'Math-Info', 'L1', NULL, 24),
(28, 'WADE', 'Cheikh', 'cheikh.wade@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-005', 'Math-Info', 'L1', NULL, 24),
(29, 'BARRY', 'Mariama', 'mariama.barry@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-006', 'Math-Info', 'L1', NULL, 24),
(30, 'DIOUF', 'Ousmane', 'ousmane.diouf@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-007', 'Math-Info', 'L1', NULL, 25),
(31, 'MBAYE', 'Rokhaya', 'rokhaya.mbaye@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-008', 'Math-Info', 'L1', NULL, 25),
(32, 'GUEYE', 'Pape', 'pape.gueye@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L1-009', 'Math-Info', 'L1', NULL, 25),
(33, 'SARR', 'Aissatou', 'aissatou.sarr@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-001', 'Math-Info', 'L2', NULL, 26),
(34, 'DIOP', 'Abdoulaye', 'abdoulaye.diop@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-002', 'Math-Info', 'L2', NULL, 26),
(35, 'TOURE', 'Khadija', 'khadija.toure@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-003', 'Math-Info', 'L2', NULL, 26),
(36, 'NIANE', 'Mamadou', 'mamadou.niane@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-004', 'Math-Info', 'L2', NULL, 27),
(37, 'CAMARA', 'Sokhna', 'sokhna.camara@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-005', 'Math-Info', 'L2', NULL, 27),
(38, 'FAYE', 'Modou', 'modou.faye@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L2-006', 'Math-Info', 'L2', NULL, 27),
(39, 'SYLLA', 'Binta', 'binta.sylla@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-001', 'Math-Info', 'L3', NULL, 28),
(40, 'MBOUP', 'Serigne', 'serigne.mboup@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-002', 'Math-Info', 'L3', NULL, 28),
(41, 'DEME', 'Ndéye', 'ndeye.deme@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-003', 'Math-Info', 'L3', NULL, 28),
(42, 'CISSE', 'Lamine', 'lamine.cisse@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-004', 'Math-Info', 'L3', NULL, 29),
(43, 'BADJI', 'Coumba', 'coumba.badji@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-005', 'Math-Info', 'L3', NULL, 29),
(44, 'SONKO', 'Aliou', 'aliou.sonko@univ.sn', 'etu123', 3, NULL, NULL, 'MI-L3-006', 'Math-Info', 'L3', NULL, 29),
(45, 'DIEDHIOU', 'Boubacar', 'boubacar.diedhiou@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-001', 'Génie Info', 'L1', NULL, 30),
(46, 'MENDY', 'Astou', 'astou.mendy@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-002', 'Génie Info', 'L1', NULL, 30),
(47, 'SANE', 'Ibou', 'ibou.sane@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-003', 'Génie Info', 'L1', NULL, 30),
(48, 'BASSENE', 'Marème', 'mareme.bassene@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-004', 'Génie Info', 'L1', NULL, 31),
(49, 'DIATTA', 'Lansana', 'lansana.diatta@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-005', 'Génie Info', 'L1', NULL, 31),
(50, 'TENDENG', 'Nafissatou', 'nafissatou.tendeng@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-006', 'Génie Info', 'L1', NULL, 31),
(51, 'GOUDIABY', 'Omar', 'omar.goudiaby@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-007', 'Génie Info', 'L1', NULL, 32),
(52, 'BADIANE', 'Rouba', 'rouba.badiane@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-008', 'Génie Info', 'L1', NULL, 32),
(53, 'MANGA', 'Abdou', 'abdou.manga@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-009', 'Génie Info', 'L1', NULL, 32),
(54, 'SAMBOU', 'Yande', 'yande.sambou@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-001', 'Génie Info', 'L2', NULL, 33),
(55, 'NDIAYE', 'Seydou', 'seydou.ndiaye@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-002', 'Génie Info', 'L2', NULL, 33),
(56, 'BALDE', 'Fatoumata', 'fatoumata.balde@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-003', 'Génie Info', 'L2', NULL, 33),
(57, 'DIALLO', 'Adama', 'adama.diallo@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-004', 'Génie Info', 'L2', NULL, 34),
(58, 'SOW', 'Maimouna', 'maimouna.sow@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-005', 'Génie Info', 'L2', NULL, 34),
(59, 'KOUYATE', 'Tidiane', 'tidiane.kouyate@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-006', 'Génie Info', 'L2', NULL, 34),
(60, 'FOFANA', 'Djiby', 'djiby.fofana@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-001', 'Génie Info', 'L3', NULL, 35),
(61, 'KEITA', 'Ndeye', 'ndeye.keita@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-002', 'Génie Info', 'L3', NULL, 35),
(62, 'BARRY', 'Elhadj', 'elhadj.barry@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-003', 'Génie Info', 'L3', NULL, 35),
(63, 'TOURE', 'Ibrahima', 'ibrahima.toure@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-004', 'Génie Info', 'L3', NULL, 36),
(64, 'DIALLO', 'Kadiatou', 'kadiatou.diallo@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-005', 'Génie Info', 'L3', NULL, 36),
(65, 'CAMARA', 'Mamadou', 'mamadou.camara@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-006', 'Génie Info', 'L3', NULL, 36),
(66, 'DIOUF', 'Awa', 'awa.diouf@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-001', 'Physique-Chimie', 'L1', NULL, 37),
(67, 'NDIAYE', 'Babacar', 'babacar.ndiaye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-002', 'Physique-Chimie', 'L1', NULL, 37),
(68, 'FALL', 'Binta', 'binta.fall@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-003', 'Physique-Chimie', 'L1', NULL, 37),
(69, 'MBAYE', 'Cheikh', 'cheikh.mbaye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-004', 'Physique-Chimie', 'L1', NULL, 38),
(70, 'GUEYE', 'Aminata', 'aminata.gueye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-005', 'Physique-Chimie', 'L1', NULL, 38),
(71, 'SECK', 'Pape', 'pape.seck@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-006', 'Physique-Chimie', 'L1', NULL, 38),
(72, 'DIOP', 'Rokhaya', 'rokhaya.diop@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-007', 'Physique-Chimie', 'L1', NULL, 39),
(73, 'SARR', 'Oumar', 'oumar.sarr@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-008', 'Physique-Chimie', 'L1', NULL, 39),
(74, 'WADE', 'Mame', 'mame.wade@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-009', 'Physique-Chimie', 'L1', NULL, 39),
(75, 'BADJI', 'Fatou', 'fatou.badji@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-001', 'Physique-Chimie', 'L2', NULL, 40),
(76, 'CISSE', 'Moussa', 'moussa.cisse@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-002', 'Physique-Chimie', 'L2', NULL, 40),
(77, 'SONKO', 'Mariama', 'mariama.sonko@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-003', 'Physique-Chimie', 'L2', NULL, 40),
(78, 'DIEME', 'Serigne', 'serigne.dieme@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-004', 'Physique-Chimie', 'L2', NULL, 41),
(79, 'FAYE', 'Ndéye', 'ndeye.faye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-005', 'Physique-Chimie', 'L2', NULL, 41),
(80, 'DIATTA', 'Lamine', 'lamine.diatta@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-006', 'Physique-Chimie', 'L2', NULL, 41),
(81, 'MENDY', 'Aissatou', 'aissatou.mendy@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-001', 'Physique-Chimie', 'L3', NULL, 42),
(82, 'SANE', 'Abdoulaye', 'abdoulaye.sane@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-002', 'Physique-Chimie', 'L3', NULL, 42),
(83, 'BADIANE', 'Coumba', 'coumba.badiane@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-003', 'Physique-Chimie', 'L3', NULL, 42),
(84, 'BASSENE', 'Ibou', 'ibou.bassene@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-004', 'Physique-Chimie', 'L3', NULL, 43),
(85, 'GOUDIABY', 'Awa', 'awa.goudiaby@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-005', 'Physique-Chimie', 'L3', NULL, 43),
(86, 'MANGA', 'Boubacar', 'boubacar.manga@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-006', 'Physique-Chimie', 'L3', NULL, 43),
(87, 'DIALLO', 'Seydou', 'seydou.diallo@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-001', 'Génie Info', 'L1', NULL, 30),
(88, 'NDIAYE', 'Khady', 'khady.ndiaye@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-002', 'Génie Info', 'L1', NULL, 30),
(89, 'FALL', 'Oumar', 'oumar.fall@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-003', 'Génie Info', 'L1', NULL, 30),
(90, 'MBAYE', 'Fatima', 'fatima.mbaye@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-004', 'Génie Info', 'L1', NULL, 31),
(91, 'GUEYE', 'Lamine', 'lamine.gueye@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-005', 'Génie Info', 'L1', NULL, 31),
(92, 'SECK', 'Mariama', 'mariama.seck@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-006', 'Génie Info', 'L1', NULL, 31),
(93, 'DIOP', 'Ibrahima', 'ibrahima.diop@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-007', 'Génie Info', 'L1', NULL, 32),
(94, 'SARR', 'Rokhaya', 'rokhaya.sarr@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-008', 'Génie Info', 'L1', NULL, 32),
(95, 'WADE', 'Boubacar', 'boubacar.wade@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L1-009', 'Génie Info', 'L1', NULL, 32),
(96, 'TOURE', 'Aissatou', 'aissatou.toure@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-001', 'Génie Info', 'L2', NULL, 33),
(97, 'CISSE', 'Modou', 'modou.cisse@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-002', 'Génie Info', 'L2', NULL, 33),
(98, 'BARRY', 'Ndéye', 'ndeye.barry@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-003', 'Génie Info', 'L2', NULL, 33),
(99, 'DIOUF', 'Cheikh', 'cheikh.diouf@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-004', 'Génie Info', 'L2', NULL, 34),
(100, 'NIANE', 'Fatou', 'fatou.niane@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-005', 'Génie Info', 'L2', NULL, 34),
(101, 'CAMARA', 'Aliou', 'aliou.camara@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L2-006', 'Génie Info', 'L2', NULL, 34),
(102, 'SYLLA', 'Mamadou', 'mamadou.sylla@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-001', 'Génie Info', 'L3', NULL, 35),
(103, 'MBOUP', 'Aminata', 'aminata.mboup@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-002', 'Génie Info', 'L3', NULL, 35),
(104, 'DEME', 'Serigne', 'serigne.deme@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-003', 'Génie Info', 'L3', NULL, 35),
(105, 'BADJI', 'Coumba', 'coumba.badji2@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-004', 'Génie Info', 'L3', NULL, 36),
(106, 'SONKO', 'Pape', 'pape.sonko@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-005', 'Génie Info', 'L3', NULL, 36),
(107, 'DIEDHIOU', 'Fatoumata', 'fatoumata.diedhiou@univ.sn', 'etu123', 3, NULL, NULL, 'GI-L3-006', 'Génie Info', 'L3', NULL, 36),
(108, 'MENDY', 'Abdou', 'abdou.mendy@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-010', 'Physique-Chimie', 'L1', NULL, 37),
(109, 'SANE', 'Marème', 'mareme.sane@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-011', 'Physique-Chimie', 'L1', NULL, 37),
(110, 'BASSENE', 'Omar', 'omar.bassene@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-012', 'Physique-Chimie', 'L1', NULL, 37),
(111, 'BADIANE', 'Ndéye', 'ndeye.badiane@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-013', 'Physique-Chimie', 'L1', NULL, 38),
(112, 'MANGA', 'Seydou', 'seydou.manga@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-014', 'Physique-Chimie', 'L1', NULL, 38),
(113, 'SAMBOU', 'Aissatou', 'aissatou.sambou@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-015', 'Physique-Chimie', 'L1', NULL, 38),
(114, 'BALDE', 'Moussa', 'moussa.balde@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-016', 'Physique-Chimie', 'L1', NULL, 39),
(115, 'DIATTA', 'Aminata', 'aminata.diatta@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-017', 'Physique-Chimie', 'L1', NULL, 39),
(116, 'TENDENG', 'Ibou', 'ibou.tendeng@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L1-018', 'Physique-Chimie', 'L1', NULL, 39),
(117, 'GOUDIABY', 'Fatou', 'fatou.goudiaby@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-007', 'Physique-Chimie', 'L2', NULL, 40),
(118, 'KOUYATE', 'Cheikh', 'cheikh.kouyate@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-008', 'Physique-Chimie', 'L2', NULL, 40),
(119, 'FOFANA', 'Mariama', 'mariama.fofana@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-009', 'Physique-Chimie', 'L2', NULL, 40),
(120, 'KEITA', 'Lamine', 'lamine.keita@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-010', 'Physique-Chimie', 'L2', NULL, 41),
(121, 'TOURE', 'Rokhaya', 'rokhaya.toure@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-011', 'Physique-Chimie', 'L2', NULL, 41),
(122, 'DIALLO', 'Boubacar', 'boubacar.diallo@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L2-012', 'Physique-Chimie', 'L2', NULL, 41),
(123, 'CAMARA', 'Ndéye', 'ndeye.camara@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-007', 'Physique-Chimie', 'L3', NULL, 42),
(124, 'NDIAYE', 'Aliou', 'aliou.ndiaye2@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-008', 'Physique-Chimie', 'L3', NULL, 42),
(125, 'FALL', 'Sokhna', 'sokhna.fall@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-009', 'Physique-Chimie', 'L3', NULL, 42),
(126, 'MBAYE', 'Ibrahima', 'ibrahima.mbaye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-010', 'Physique-Chimie', 'L3', NULL, 43),
(127, 'GUEYE', 'Fatima', 'fatima.gueye@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-011', 'Physique-Chimie', 'L3', NULL, 43),
(128, 'SECK', 'Oumar', 'oumar.seck@univ.sn', 'etu123', 3, NULL, NULL, 'PC-L3-012', 'Physique-Chimie', 'L3', NULL, 43),
(129, 'DIOP', 'Cheikh', 'cheikh.diop.m1@univ.sn', 'etu123', 3, NULL, NULL, 'M1-001', 'Math-Info', 'M1', NULL, 44),
(130, 'SARR', 'Fatou', 'fatou.sarr.m1@univ.sn', 'etu123', 3, NULL, NULL, 'M1-002', 'Math-Info', 'M1', NULL, 44),
(131, 'WADE', 'Mamadou', 'mamadou.wade.m1@univ.sn', 'etu123', 3, NULL, NULL, 'M1-003', 'Math-Info', 'M1', NULL, 44),
(132, 'NDIAYE', 'Coumba', 'coumba.ndiaye.m2@univ.sn', 'etu123', 3, NULL, NULL, 'M2-001', 'Math-Info', 'M2', NULL, 45),
(133, 'FALL', 'Serigne', 'serigne.fall.m2@univ.sn', 'etu123', 3, NULL, NULL, 'M2-002', 'Math-Info', 'M2', NULL, 45),
(134, 'DIALLO', 'Aissatou', 'aissatou.diallo.m2@univ.sn', 'etu123', 3, NULL, NULL, 'M2-003', 'Math-Info', 'M2', NULL, 45);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `annee`
--
ALTER TABLE `annee`
  ADD PRIMARY KEY (`idAnnee`);

--
-- Index pour la table `batiment`
--
ALTER TABLE `batiment`
  ADD PRIMARY KEY (`id_batiment`);

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id_cours`),
  ADD KEY `matiere_id` (`matiere_id`),
  ADD KEY `enseignant_id` (`enseignant_id`);

--
-- Index pour la table `creneau_horaire`
--
ALTER TABLE `creneau_horaire`
  ADD PRIMARY KEY (`id_creneau`);

--
-- Index pour la table `equipement`
--
ALTER TABLE `equipement`
  ADD PRIMARY KEY (`id_equipement`);

--
-- Index pour la table `groupe`
--
ALTER TABLE `groupe`
  ADD PRIMARY KEY (`idGroupe`),
  ADD KEY `annee_id` (`annee_id`);

--
-- Index pour la table `matiere`
--
ALTER TABLE `matiere`
  ADD PRIMARY KEY (`id_matiere`);

--
-- Index pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id_reservation`),
  ADD UNIQUE KEY `salle_id` (`salle_id`,`creneau_id`,`date_reservation`),
  ADD KEY `creneau_id` (`creneau_id`),
  ADD KEY `utilisateur_id` (`utilisateur_id`);

--
-- Index pour la table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `salle`
--
ALTER TABLE `salle`
  ADD PRIMARY KEY (`id_salle`),
  ADD KEY `batiment_id` (`batiment_id`);

--
-- Index pour la table `salle_equipement`
--
ALTER TABLE `salle_equipement`
  ADD PRIMARY KEY (`id_salle`,`id_equipement`),
  ADD KEY `id_equipement` (`id_equipement`);

--
-- Index pour la table `seance`
--
ALTER TABLE `seance`
  ADD PRIMARY KEY (`id_seance`),
  ADD KEY `seance_ibfk_1` (`cours_id`),
  ADD KEY `seance_ibfk_2` (`salle_id`),
  ADD KEY `seance_ibfk_3` (`creneau_id`),
  ADD KEY `seance_ibfk_groupe` (`groupe_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`),
  ADD KEY `utilisateur_ibfk_groupe` (`groupe_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `annee`
--
ALTER TABLE `annee`
  MODIFY `idAnnee` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `batiment`
--
ALTER TABLE `batiment`
  MODIFY `id_batiment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id_cours` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=122;

--
-- AUTO_INCREMENT pour la table `creneau_horaire`
--
ALTER TABLE `creneau_horaire`
  MODIFY `id_creneau` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `equipement`
--
ALTER TABLE `equipement`
  MODIFY `id_equipement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `groupe`
--
ALTER TABLE `groupe`
  MODIFY `idGroupe` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT pour la table `matiere`
--
ALTER TABLE `matiere`
  MODIFY `id_matiere` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT pour la table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id_reservation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `salle`
--
ALTER TABLE `salle`
  MODIFY `id_salle` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
  MODIFY `id_seance` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1393;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=135;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `cours_ibfk_1` FOREIGN KEY (`matiere_id`) REFERENCES `matiere` (`id_matiere`),
  ADD CONSTRAINT `cours_ibfk_2` FOREIGN KEY (`enseignant_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `groupe`
--
ALTER TABLE `groupe`
  ADD CONSTRAINT `groupe_ibfk_1` FOREIGN KEY (`annee_id`) REFERENCES `annee` (`idAnnee`);

--
-- Contraintes pour la table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`salle_id`) REFERENCES `salle` (`id_salle`),
  ADD CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`creneau_id`) REFERENCES `creneau_horaire` (`id_creneau`),
  ADD CONSTRAINT `reservation_ibfk_3` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `salle`
--
ALTER TABLE `salle`
  ADD CONSTRAINT `salle_ibfk_1` FOREIGN KEY (`batiment_id`) REFERENCES `batiment` (`id_batiment`);

--
-- Contraintes pour la table `salle_equipement`
--
ALTER TABLE `salle_equipement`
  ADD CONSTRAINT `salle_equipement_ibfk_1` FOREIGN KEY (`id_salle`) REFERENCES `salle` (`id_salle`),
  ADD CONSTRAINT `salle_equipement_ibfk_2` FOREIGN KEY (`id_equipement`) REFERENCES `equipement` (`id_equipement`);

--
-- Contraintes pour la table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `seance_ibfk_1` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id_cours`),
  ADD CONSTRAINT `seance_ibfk_2` FOREIGN KEY (`salle_id`) REFERENCES `salle` (`id_salle`),
  ADD CONSTRAINT `seance_ibfk_3` FOREIGN KEY (`creneau_id`) REFERENCES `creneau_horaire` (`id_creneau`),
  ADD CONSTRAINT `seance_ibfk_groupe` FOREIGN KEY (`groupe_id`) REFERENCES `groupe` (`idGroupe`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `utilisateur_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `utilisateur_ibfk_groupe` FOREIGN KEY (`groupe_id`) REFERENCES `groupe` (`idGroupe`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
