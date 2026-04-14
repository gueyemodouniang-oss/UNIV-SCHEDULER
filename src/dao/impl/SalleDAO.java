package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.ISalleDAO;
import modele.Batiment;
import modele.Equipement;
import modele.Salle;
import modele.enums.TypeSalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO implements ISalleDAO {

    private static final String SELECT_SALLE =
            "SELECT s.id_salle, s.numero, s.capacite, s.type, " +
                    "b.id_batiment AS bat_id, b.nom_batiment, b.localisation " +
                    "FROM salle s " +
                    "JOIN batiment b ON s.batiment_id = b.id_batiment ";


    private Salle creerSalle(ResultSet rs) throws SQLException {
        Batiment batiment = new Batiment(
                rs.getInt("bat_id"),          // ✅ alias
                rs.getString("nom_batiment"),
                rs.getString("localisation")
        );

        TypeSalle type = TypeSalle.valueOf(rs.getString("type"));

        return new Salle(
                rs.getInt("id_salle"),
                rs.getString("numero"),
                rs.getInt("capacite"),
                type,
                batiment
        );
    }

    // ✅ Méthode utilitaire : charger les équipements d'une salle
    private List<Equipement> chargerEquipements(int idSalle, Connection conn) throws SQLException {
        List<Equipement> equipements = new ArrayList<>();
        String sql = "SELECT e.* FROM equipement e " +
                "JOIN salle_equipement se ON e.id_equipement = se.id_equipement " +
                "WHERE se.id_salle = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSalle);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                equipements.add(new Equipement(
                        rs.getInt("id_equipement"),
                        rs.getString("nom_equipement"),
                        rs.getString("description")
                ));
            }
        }
        return equipements;
    }

    @Override
    public void ajouter(Salle salle) {
        String sql = "INSERT INTO salle(numero, capacite, type, batiment_id) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, salle.getNumeroSalle());
            stmt.setInt(2, salle.getCapacite());
            stmt.setString(3, salle.getType().name());
            stmt.setInt(4, salle.getBatiment().getIdBatiment());
            stmt.executeUpdate();

            // Récupérer l'id généré
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idSalle = generatedKeys.getInt(1);

                // Insérer les équipements dans salle_equipement
                for (Equipement eq : salle.getEquipements()) {
                    ajouterEquipement(idSalle, eq.getIdEquipement(), conn);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Ajouter lien salle-equipement
    private void ajouterEquipement(int idSalle, int idEquipement, Connection conn) throws SQLException {
        String sql = "INSERT INTO salle_equipement(id_salle, id_equipement) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSalle);
            stmt.setInt(2, idEquipement);
            stmt.executeUpdate();
        }
    }

    @Override
    public Salle trouverParId(int id) {
        String sql = "SELECT s.*, b.nom_batiment, b.localisation " +
                "FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                "WHERE s.id_salle = ?";
        Salle salle = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(id, conn)); // ✅
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salle;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT s.*, b.nom_batiment, b.localisation " +
                "FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SALLE)) {

            while (rs.next()) {
                Salle salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(salle.getIdSalle(), conn)); // ✅
                salles.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    @Override
    public void modifier(Salle salle) {
        String sql = "UPDATE salle SET numero=?, capacite=?, type=?, batiment_id=? WHERE id_salle=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, salle.getNumeroSalle());
            stmt.setInt(2, salle.getCapacite());
            stmt.setString(3, salle.getType().name());
            stmt.setInt(4, salle.getBatiment().getIdBatiment());
            stmt.setInt(5, salle.getIdSalle());
            stmt.executeUpdate();

            // ✅ Mettre à jour les équipements
            supprimerEquipements(salle.getIdSalle(), conn);
            for (Equipement eq : salle.getEquipements()) {
                ajouterEquipement(salle.getIdSalle(), eq.getIdEquipement(), conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Supprimer tous les équipements d'une salle
    private void supprimerEquipements(int idSalle, Connection conn) throws SQLException {
        String sql = "DELETE FROM salle_equipement WHERE id_salle=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idSalle);
            stmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Début transaction

            // 1. Supprimer les équipements liés
            supprimerEquipements(id, conn);

            // 2. Supprimer les réservations liées
            PreparedStatement delResa = conn.prepareStatement(
                    "DELETE FROM reservation WHERE salle_id = ?");
            delResa.setInt(1, id);
            delResa.executeUpdate();

            // 3. Supprimer les séances liées
            PreparedStatement delSeance = conn.prepareStatement(
                    "DELETE FROM seance WHERE salle_id = ?");
            delSeance.setInt(1, id);
            delSeance.executeUpdate();

            // 4. Supprimer la salle
            PreparedStatement delSalle = conn.prepareStatement(
                    "DELETE FROM salle WHERE id_salle = ?");
            delSalle.setInt(1, id);
            delSalle.executeUpdate();

            conn.commit(); // ✅ Tout s'est bien passé

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public List<Salle> trouverSallesDisponibles(int creneauId, String date) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT s.id_salle, s.numero, s.capacite, s.type, " +
                "b.id_batiment AS bat_id, b.nom_batiment, b.localisation " +  // ← alias bat_id ajouté
                "FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                "WHERE s.id_salle NOT IN (" +
                "  SELECT salle_id FROM seance " +
                "  WHERE creneau_id = ? AND date_seance = ?" +
                "  UNION " +
                "  SELECT salle_id FROM reservation " +
                "  WHERE creneau_id = ? AND date_reservation = ? " +
                "  AND statut != 'Annule')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, creneauId);
            stmt.setString(2, date);
            stmt.setInt(3, creneauId);
            stmt.setString(4, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(salle.getIdSalle(), conn));
                salles.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    @Override
    public List<Salle> trouverParType(TypeSalle type) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT s.*, b.nom_batiment, b.localisation FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                "WHERE s.type = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(salle.getIdSalle(), conn));
                salles.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    @Override
    public List<Salle> trouverParCapaciteMin(int capaciteMin) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT s.*, b.nom_batiment, b.localisation FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                "WHERE s.capacite >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, capaciteMin);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Salle salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(salle.getIdSalle(), conn));
                salles.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }
    // Nouvelle méthode sans date — ajouter dans SalleDAO
    public List<Salle> trouverSallesDisponibles(int creneauId) {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT s.id_salle, s.numero, s.capacite, s.type, " +
                "b.id_batiment AS bat_id, b.nom_batiment, b.localisation " +
                "FROM salle s " +
                "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                "WHERE s.id_salle NOT IN (" +
                "  SELECT salle_id FROM seance " +
                "  WHERE creneau_id = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, creneauId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Salle salle = creerSalle(rs);
                salle.setEquipements(chargerEquipements(salle.getIdSalle(), conn));
                salles.add(salle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }
}