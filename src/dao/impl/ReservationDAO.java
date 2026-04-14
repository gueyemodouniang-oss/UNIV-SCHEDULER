package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IReservationDAO;
import modele.*;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Jour;
import modele.enums.Role;
import modele.enums.StatutReservation;
import modele.enums.TypeSalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements IReservationDAO {

    private static final String SELECT_RESERVATION =
            "SELECT r.id_reservation, r.statut, r.date_reservation, " +
                    "s.id_salle, s.numero, s.capacite, s.type, " +
                    "b.id_batiment, b.nom_batiment, b.localisation, " +
                    "ch.id_creneau, ch.jour, ch.heure_debut, ch.heure_fin, " +
                    "u.id, u.nom, u.prenom, u.email, u.mot_de_passe, u.role_id " +
                    "FROM reservation r " +
                    "JOIN salle s ON r.salle_id = s.id_salle " +
                    "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                    "JOIN creneau_horaire ch ON r.creneau_id = ch.id_creneau " +
                    "JOIN utilisateur u ON r.utilisateur_id = u.id ";

    private Reservation creerReservation(ResultSet rs) throws SQLException {

        Batiment batiment = new Batiment(
                rs.getInt("id_batiment"),
                rs.getString("nom_batiment"),
                rs.getString("localisation")
        );

        Salle salle = new Salle(
                rs.getInt("id_salle"),
                rs.getString("numero"),
                rs.getInt("capacite"),
                TypeSalle.valueOf(rs.getString("type")),
                batiment
        );

        CreneauHoraire creneau = new CreneauHoraire(
                rs.getInt("id_creneau"),
                Jour.valueOf(rs.getString("jour")),
                rs.getTime("heure_debut").toLocalTime(),
                rs.getTime("heure_fin").toLocalTime()
        );

        // Créer l'utilisateur selon son rôle
        Role role = Role.values()[rs.getInt("role_id") - 1];
        Utilisateur utilisateur = creerUtilisateurSimple(rs, role);

        StatutReservation statut = StatutReservation.valueOf(rs.getString("statut"));

        return new Reservation(
                rs.getInt("id_reservation"),
                salle,
                creneau,
                utilisateur,
                statut,
                rs.getDate("date_reservation").toLocalDate()
        );
    }

    // ✅ Créer utilisateur simplifié sans champs spécifiques
    private Utilisateur creerUtilisateurSimple(ResultSet rs, Role role) throws SQLException {
        int id = rs.getInt("id");
        String prenom = rs.getString("prenom");
        String nom = rs.getString("nom");
        String email = rs.getString("email");
        String mdp = rs.getString("mot_de_passe");

        if (role == Role.Admin)
            return new modele.Utilisateurs.Admin(id, prenom, nom, email, mdp, Role.Admin);
        if (role == Role.Enseignant)
            return new modele.Utilisateurs.Enseignant(id, prenom, nom, email, mdp, Role.Enseignant, null, null);
        if (role == Role.Etudiant)
            return new modele.Utilisateurs.Etudiant(id, prenom, nom, email, mdp, Role.Etudiant, null, null, null);
        if (role == Role.Gestionnaire)
            return new modele.Utilisateurs.Gestionnaire(id, prenom, nom, email, mdp, Role.Gestionnaire, null);
        return null;
    }

    @Override
    public void ajouter(Reservation reservation) {
        String sql = "INSERT INTO reservation(salle_id, creneau_id, utilisateur_id, statut, date_reservation) " +
                "VALUES(?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getSalle().getIdSalle());
            stmt.setInt(2, reservation.getCreneau().getIdCreneauHoraire());
            stmt.setInt(3, reservation.getUtilisateur().getId());
            stmt.setString(4, reservation.getStatut().name());
            stmt.setDate(5, Date.valueOf(reservation.getDateReservation()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation trouverParId(int id) {
        String sql = SELECT_RESERVATION + "WHERE r.id_reservation = ?";
        Reservation reservation = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) reservation = creerReservation(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_RESERVATION)) {

            while (rs.next()) reservations.add(creerReservation(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public void modifier(Reservation reservation) {
        String sql = "UPDATE reservation SET salle_id=?, creneau_id=?, statut=?, date_reservation=? " +
                "WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getSalle().getIdSalle());
            stmt.setInt(2, reservation.getCreneau().getIdCreneauHoraire());
            stmt.setString(3, reservation.getStatut().name());
            stmt.setDate(4, Date.valueOf(reservation.getDateReservation()));
            stmt.setInt(5, reservation.getIdReservation());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM reservation WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservation> trouverParUtilisateur(int utilisateurId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = SELECT_RESERVATION + "WHERE r.utilisateur_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateurId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) reservations.add(creerReservation(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public List<Reservation> trouverParSalle(int salleId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = SELECT_RESERVATION + "WHERE r.salle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, salleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) reservations.add(creerReservation(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public List<Reservation> trouverParStatut(StatutReservation statut) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = SELECT_RESERVATION + "WHERE r.statut = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) reservations.add(creerReservation(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    @Override
    public boolean verifierConflit(int salleId, int creneauId, String date) {
        String sql = "SELECT COUNT(*) FROM reservation " +
                "WHERE salle_id=? AND creneau_id=? AND date_reservation=? " +
                "AND statut != 'Annule'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, salleId);
            stmt.setInt(2, creneauId);
            stmt.setString(3, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void changerStatut(int idReservation, StatutReservation statut) {
        String sql = "UPDATE reservation SET statut=? WHERE id_reservation=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut.name());
            stmt.setInt(2, idReservation);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}