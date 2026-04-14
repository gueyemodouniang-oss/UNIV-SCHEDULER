package service;

import dao.impl.ReservationDAO;
import dao.impl.SeanceDAO;
import modele.Reservation;

public class ConflitService {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private SeanceDAO      seanceDAO      = new SeanceDAO();

    // ✅ Vérifier conflit entre deux réservations
    public boolean verifierConflitReservation(Reservation r1, Reservation r2) {
        return r1.getSalle().getIdSalle() == r2.getSalle().getIdSalle()
                && r1.getCreneau().getIdCreneauHoraire() == r2.getCreneau().getIdCreneauHoraire();
    }

    // ✅ Salle occupée — basé sur créneau uniquement (sans date)
    public boolean salleOccupee(int salleId, int creneauId) {
        String sql = "SELECT COUNT(*) FROM seance " +
                "WHERE salle_id = ? AND creneau_id = ?";
        try (java.sql.Connection conn = dao.DatabaseConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId);
            stmt.setInt(2, creneauId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Enseignant occupé — basé sur créneau uniquement (sans date)
    public boolean enseignantOccupe(int enseignantId, int creneauId) {
        String sql = "SELECT COUNT(*) FROM seance se " +
                "JOIN cours c ON se.cours_id = c.id_cours " +
                "WHERE c.enseignant_id = ? AND se.creneau_id = ?";
        try (java.sql.Connection conn = dao.DatabaseConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            stmt.setInt(2, creneauId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Ancien méthode gardée pour compatibilité avec ReservationService
    public boolean salleOccupee(int salleId, int creneauId, String date) {
        return salleOccupee(salleId, creneauId);
    }

    public boolean enseignantOccupe(int enseignantId, int creneauId, String date) {
        return enseignantOccupe(enseignantId, creneauId);
    }
}