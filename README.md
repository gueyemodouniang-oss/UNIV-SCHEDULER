# 🎓 UNIV-SCHEDULER

Application de gestion des salles et emplois du temps universitaires.
Développée dans le cadre du projet L2 Informatique.

---

## 📋 Table des matières

- [Description](#description)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Utilisation](#utilisation)
- [Structure du projet](#structure-du-projet)
- [Comptes de test](#comptes-de-test)
- [Base de données](#base-de-données)

---

## 📖 Description

**UNIV-SCHEDULER** est une application de bureau développée en **Java + JavaFX**
permettant de gérer les emplois du temps, les salles de cours et les réservations
d'une université. Elle supporte 4 rôles distincts : Admin, Gestionnaire,
Enseignant et Étudiant.

---

## ✅ Fonctionnalités

### 👑 Administrateur
- Gestion des utilisateurs (CRUD + profil)
- Gestion des salles et bâtiments
- Gestion des équipements (assignation aux salles)
- Statistiques globales (graphiques)

### 📋 Gestionnaire
- Gestion des cours (matière + enseignant)
- Gestion des séances (cours + groupe + salle + créneau)
- Gestion des réservations (confirmation, annulation)
- Détection des conflits (salle et enseignant)
- Planning hebdomadaire visuel

### 👨‍🏫 Enseignant
- Consultation de son emploi du temps
- Réservation ponctuelle de salles
- Gestion de ses réservations
- Recherche de salles disponibles

### 🎓 Étudiant
- Consultation de l'EDT de sa classe
- Consultation de l'EDT par groupe
- Recherche de salles disponibles
- Export PDF de l'emploi du temps

---

## 🛠 Technologies utilisées

| Technologie | Version | Usage |
|---|---|---|
| Java | 17+ | Langage principal |
| JavaFX | 25.0.2 | Interface graphique |
| MySQL | 8.0+ | Base de données |
| MySQL Connector/J | 9.6.0 | Connexion Java-MySQL |
| iText | 5.5.13.3 | Export PDF |
| IntelliJ IDEA | 2024+ | IDE de développement |

---

## 📦 Prérequis

Avant d'installer l'application, assurez-vous d'avoir :

- ✅ **Java JDK 17** ou supérieur
- ✅ **JavaFX SDK 25.0.2**
- ✅ **MySQL Server 8.0** ou supérieur
- ✅ **phpMyAdmin** (optionnel, pour gérer la base)
- ✅ **IntelliJ IDEA** (pour le code source)

---

## 🚀 Installation

### Étape 1 — Cloner le dépôt

```bash
git clone https://github.com/TON_USERNAME/UNIV-SCHEDULER.git
cd UNIV-SCHEDULER
```

### Étape 2 — Créer la base de données

1. Ouvrez **phpMyAdmin** ou **MySQL Workbench**
2. Importez le fichier `univ_scheduler.sql` :
```sql
source univ_scheduler.sql;
```
Ou via phpMyAdmin : **Importer → Choisir le fichier → univ_scheduler.sql → Exécuter**

### Étape 3 — Configurer la connexion

Ouvrez le fichier `src/dao/DatabaseConnection.java` et modifiez :

```java
private static final String URL      = "jdbc:mysql://localhost:3306/univ_scheduler";
private static final String USER     = "root";      // ← votre utilisateur MySQL
private static final String PASSWORD = "";          // ← votre mot de passe MySQL
```

### Étape 4 — Configurer IntelliJ IDEA

1. Ouvrez le projet dans IntelliJ IDEA
2. `File → Project Structure → Libraries → +` :
    - Ajoutez **JavaFX** : `C:\Program Files\javafx-sdk-25.0.2\lib`
    - Ajoutez **MySQL Connector** : `mysql-connector-j-9.6.0.jar`
    - Ajoutez **iText** : `itextpdf-5.5.13.3.jar`
3. Configurez les VM Options :
    - `Run → Edit Configurations → VM Options` :
   
      --module-path "C:\Program Files\javafx-sdk-25.0.2\lib"
      --add-modules javafx.controls,javafx.fxml,javafx.graphics
      --enable-native-access=javafx.graphics,javafx.media,javafx.web

4. Main class : `vue.commun.MainVue`

### Étape 5 — Lancer l'application

```bash
# Depuis IntelliJ
Run → Run 'MainVue'

# Depuis le JAR
java --module-path "C:\Program Files\javafx-sdk-25.0.2\lib" \
     --add-modules javafx.controls,javafx.graphics \
     -jar UNIV-SCHEDULER.jar
```

---

## ⚙️ Configuration

### Structure de la base de données

| Table | Description |
|---|---|
| `utilisateur` | Tous les comptes (admin, ens, etu, gest) |
| `batiment` | Bâtiments de l'université |
| `salle` | Salles (TD, TP, AMPHI) |
| `equipement` | Types d'équipements |
| `salle_equipement` | Équipements par salle |
| `creneau_horaire` | Plages horaires |
| `cours` | Cours (matière + enseignant) |
| `seance` | Séances planifiées |
| `reservation` | Réservations ponctuelles |
| `Groupe` | Groupes d'étudiants |
| `matiere` | Matières enseignées |
| `Annee` | Années académiques |
| `role` | Rôles utilisateurs |

### Organisation universitaire
Université
├── Math-Info (Bâtiment A)
│   ├── L1 : G1, G2, G3
│   ├── L2 : G1, G2
│   └── L3 : G1, G2
├── Génie Info (Bâtiment B)
│   ├── L1 : G1, G2, G3
│   ├── L2 : G1, G2
│   └── L3 : G1, G2
├── Physique-Chimie (Bâtiment C)
│   ├── L1 : G1, G2, G3
│   ├── L2 : G1, G2
│   └── L3 : G1, G2
└── Master (Bâtiment D)
├── M1
└── M2
---

## 💻 Utilisation

### Connexion

Lancez l'application et connectez-vous avec un compte de test.

### Navigation

Chaque rôle dispose d'un menu latéral avec ses fonctionnalités :

| Rôle | Menu disponible |
|---|---|
| Admin | Accueil, Utilisateurs, Salles, Bâtiments, Équipements, Statistiques |
| Gestionnaire | Accueil, Cours, Séances, Salles Disponibles, Réservations, Conflits, Planning |
| Enseignant | Accueil, Mon EDT, Réserver une Salle, Mes Réservations, Salles Disponibles |
| Étudiant | Accueil, Mon EDT, EDT par Groupe, Salles Disponibles, Rechercher une Salle |

---

## 🔑 Comptes de test

| Rôle | Email | Mot de passe |
|---|---|---|
| Admin | `admin@univ.sn` | `admin123` |
| Gestionnaire | `gestionnaire@univ.sn` | `gest123` |
| Enseignant | `diop@univ.sn` | `ens123` |
| Étudiant L2-MI | `aissatou.sarr@univ.sn` | `etu123` |
| Étudiant L1-GI | `seydou.diallo@univ.sn` | `etu123` |
| Étudiant L2-PC | `fatou.goudiaby@univ.sn` | `etu123` |
| Étudiant M1 | `cheikh.diop.m1@univ.sn` | `etu123` |

---

## 📁 Structure du projet
## 🔑 Comptes de test

| Rôle | Email | Mot de passe |
|---|---|---|
| Admin | `admin@univ.sn` | `admin123` |
| Gestionnaire | `gestionnaire@univ.sn` | `gest123` |
| Enseignant | `diop@univ.sn` | `ens123` |
| Étudiant L2-MI | `aissatou.sarr@univ.sn` | `etu123` |
| Étudiant L1-GI | `seydou.diallo@univ.sn` | `etu123` |
| Étudiant L2-PC | `fatou.goudiaby@univ.sn` | `etu123` |
| Étudiant M1 | `cheikh.diop.m1@univ.sn` | `etu123` |

---

## 📁 Structure du projet

UNIV-SCHEDULER/
├── src/
│   ├── dao/
│   │   ├── DatabaseConnection.java
│   │   ├── interfaces/
│   │   │   ├── IUtilisateurDAO.java
│   │   │   ├── ISalleDAO.java
│   │   │   ├── ICoursDAO.java
│   │   │   ├── ISeanceDAO.java
│   │   │   ├── IReservationDAO.java
│   │   │   ├── IBatimentDAO.java
│   │   │   ├── ICreneauHoraireDAO.java
│   │   │   ├── IGroupeDAO.java
│   │   │   ├── IMatiereDAO.java
│   │   │   └── IEquipementDAO.java
│   │   └── impl/
│   │       ├── UtilisateurDAO.java
│   │       ├── SalleDAO.java
│   │       ├── CoursDAO.java
│   │       ├── SeanceDAO.java
│   │       ├── ReservationDAO.java
│   │       ├── BatimentDAO.java
│   │       ├── CreneauHoraireDAO.java
│   │       ├── GroupeDAO.java
│   │       ├── MatiereDAO.java
│   │       └── EquipementDAO.java
│   ├── modele/
│   │   ├── Utilisateurs/
│   │   │   ├── Utilisateur.java
│   │   │   ├── Admin.java
│   │   │   ├── Enseignant.java
│   │   │   ├── Etudiant.java
│   │   │   └── Gestionnaire.java
│   │   ├── enums/
│   │   │   ├── Role.java
│   │   │   ├── TypeSalle.java
│   │   │   ├── Jour.java
│   │   │   └── StatutReservation.java
│   │   ├── Batiment.java
│   │   ├── Salle.java
│   │   ├── Equipement.java
│   │   ├── Cours.java
│   │   ├── Seance.java
│   │   ├── Reservation.java
│   │   ├── CreneauHoraire.java
│   │   ├── Groupe.java
│   │   └── Matiere.java
│   ├── service/
│   │   ├── ConflitService.java
│   │   ├── ReservationService.java
│   │   └── Planning.java
│   └── vue/
│       ├── auth/
│       │   └── LoginVue.java
│       ├── admin/
│       │   └── DashboardAdminVue.java
│       ├── gestionnaire/
│       │   └── DashboardGestionnaireVue.java
│       ├── enseignant/
│       │   └── DashboardEnseignantVue.java
│       ├── etudiant/
│       │   └── DashboardEtudiantVue.java
│       └── commun/
│           └── MainVue.java
├── univ_scheduler.sql
├── README.md
└── UNIV-SCHEDULER.jar
---

## 🗄️ Base de données

### Diagramme simplifié
utilisateur ──── cours ──── seance ──── salle ──── batiment
│                          │                      │
role                      groupe              equipement
creneau_horaire
### Statistiques

| Élément | Nombre |
|---|---|
| Étudiants | 115 |
| Enseignants | 9 |
| Salles | 47 |
| Séances | 153 |
| Créneaux | 24 |
| Groupes | 23 |

---

## 👨‍💻 Auteur

**Modou Niang et Modou Wadji** — Étudiant L2 Informatique

---

## 📄 Licence

Projet académique — Tous droits réservés © 2024-2025