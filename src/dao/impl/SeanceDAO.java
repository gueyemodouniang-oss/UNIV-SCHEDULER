package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.ISeanceDAO;
import modele.*;
import modele.Utilisateurs.Enseignant;
import modele.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO implements ISeanceDAO {

    private static final String SELECT_SEANCE =
            "SELECT se.id_seance, " +
                    "c.id_cours, " +
                    "m.id_matiere, m.nom_matiere, m.volume_horaire, " +
                    "u.id AS enseignant_id, u.nom, u.prenom, u.email, " +
                    "u.mot_de_passe, u.specialite, u.departement, " +
                    "s.id_salle, s.numero, s.capacite, s.type, " +
                    "b.id_batiment AS bat_id, b.nom_batiment, b.localisation, " +
                    "cr.id_creneau, cr.jour, cr.heure_debut, cr.heure_fin, " +
                    "g.idGroupe, g.nomGroupe, g.effectif " +
                    "FROM seance se " +
                    "JOIN cours c ON se.cours_id = c.id_cours " +
                    "JOIN matiere m ON c.matiere_id = m.id_matiere " +
                    "JOIN utilisateur u ON c.enseignant_id = u.id " +
                    "JOIN salle s ON se.salle_id = s.id_salle " +
                    "JOIN batiment b ON s.batiment_id = b.id_batiment " +
                    "JOIN creneau_horaire cr ON se.creneau_id = cr.id_creneau " +
                    "JOIN Groupe g ON se.groupe_id = g.idGroupe ";

    private Seance creerSeance(ResultSet rs) throws SQLException {
        Matiere matiere = new Matiere(
                rs.getInt("id_matiere"),
                rs.getString("nom_matiere"),
                rs.getInt("volume_horaire")
        );

        Enseignant enseignant = new Enseignant(
                rs.getInt("enseignant_id"),
                rs.getString("prenom"),
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("mot_de_passe"),
                Role.Enseignant,
                rs.getString("specialite"),
                rs.getString("departement")
        );

        Cours cours = new Cours(
                rs.getInt("id_cours"),
                matiere,
                enseignant
        );

        Batiment batiment = new Batiment(
                rs.getInt("bat_id"),
                rs.getString("nom_batiment"),
                rs.getString("localisation")
        );

        Salle salle = new Salle(
                rs.getInt("id_salle"),
                rs.getString("numero"),
                rs.getInt("capacite"),
                modele.enums.TypeSalle.valueOf(rs.getString("type")),
                batiment
        );

        CreneauHoraire creneau = new CreneauHoraire(
                rs.getInt("id_creneau"),
                modele.enums.Jour.valueOf(rs.getString("jour")),
                rs.getTime("heure_debut").toLocalTime(),
                rs.getTime("heure_fin").toLocalTime()
        );

        Groupe groupe = new Groupe(
                rs.getInt("idGroupe"),
                rs.getString("nomGroupe"),
                rs.getInt("effectif")
        );

        return new Seance(
                rs.getInt("id_seance"),
                cours, salle, creneau, groupe
        );
    }

    @Override
    public void ajouter(Seance seance) {
        String sql = "INSERT INTO seance(cours_id, salle_id, creneau_id, groupe_id) " +
                "VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seance.getCours().getIdCours());
            stmt.setInt(2, seance.getSalle().getIdSalle());
            stmt.setInt(3, seance.getCreneau().getIdCreneauHoraire());
            stmt.setInt(4, seance.getGroupe().getIdGroupe());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM seance WHERE id_seance=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Seance trouverParId(int id) {
        String sql = SELECT_SEANCE + "WHERE se.id_seance = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return creerSeance(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Seance> findAll() {
        List<Seance> seances = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SEANCE)) {
            while (rs.next()) seances.add(creerSeance(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }

    @Override
    public void modifier(Seance seance) {
        String sql = "UPDATE seance SET cours_id=?, salle_id=?, " +
                "creneau_id=?, groupe_id=? WHERE id_seance=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seance.getCours().getIdCours());
            stmt.setInt(2, seance.getSalle().getIdSalle());
            stmt.setInt(3, seance.getCreneau().getIdCreneauHoraire());
            stmt.setInt(4, seance.getGroupe().getIdGroupe());
            stmt.setInt(5, seance.getIdSeance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Seance> trouverParCours(int coursId) {
        List<Seance> seances = new ArrayList<>();
        String sql = SELECT_SEANCE + "WHERE se.cours_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) seances.add(creerSeance(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }

    @Override
    public List<Seance> trouverParSalle(int salleId) {
        List<Seance> seances = new ArrayList<>();
        String sql = SELECT_SEANCE + "WHERE se.salle_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) seances.add(creerSeance(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }

    @Override
    public boolean verifierConflit(int salleId, int creneauId) {
        // Date supprimée — vérifier uniquement salle + créneau
        String sql = "SELECT COUNT(*) FROM seance " +
                "WHERE salle_id = ? AND creneau_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId);
            stmt.setInt(2, creneauId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}