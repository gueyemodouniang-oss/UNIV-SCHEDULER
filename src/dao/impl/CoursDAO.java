package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.ICoursDAO;
import modele.*;
import modele.Utilisateurs.Enseignant;
import modele.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO implements ICoursDAO {

    private static final String SELECT_COURS =
            "SELECT c.id_cours, " +
                    "m.id_matiere, m.nom_matiere, m.volume_horaire, " +
                    "u.id AS enseignant_id, u.nom, u.prenom, u.email, " +
                    "u.mot_de_passe, u.specialite, u.departement " +
                    "FROM cours c " +
                    "JOIN matiere m ON c.matiere_id = m.id_matiere " +
                    "JOIN utilisateur u ON c.enseignant_id = u.id ";

    private Cours creerCours(ResultSet rs) throws SQLException {
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

        return new Cours(
                rs.getInt("id_cours"),
                matiere,
                enseignant
        );
    }

    @Override
    public void ajouter(Cours cours) {
        String sql = "INSERT INTO cours(matiere_id, enseignant_id) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cours.getMatiere().getIdMatiere());
            stmt.setInt(2, cours.getEnseignant().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cours trouverParId(int id) {
        String sql = SELECT_COURS + "WHERE c.id_cours = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return creerCours(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cours> findAll() {
        List<Cours> coursList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_COURS)) {
            while (rs.next()) coursList.add(creerCours(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }

    @Override
    public void modifier(Cours cours) {
        String sql = "UPDATE cours SET matiere_id=?, enseignant_id=? WHERE id_cours=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cours.getMatiere().getIdMatiere());
            stmt.setInt(2, cours.getEnseignant().getId());
            stmt.setInt(3, cours.getIdCours());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement delSeance = conn.prepareStatement(
                    "DELETE FROM seance WHERE cours_id = ?");
            delSeance.setInt(1, id);
            delSeance.executeUpdate();
            PreparedStatement delCours = conn.prepareStatement(
                    "DELETE FROM cours WHERE id_cours = ?");
            delCours.setInt(1, id);
            delCours.executeUpdate();
            conn.commit();
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
    public List<Cours> trouverParEnseignant(int enseignantId) {
        List<Cours> coursList = new ArrayList<>();
        String sql = SELECT_COURS + "WHERE c.enseignant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) coursList.add(creerCours(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }

    @Override
    public List<Cours> trouverParGroupe(int groupeId) {
        // Groupe maintenant dans seance — recherche via seance
        List<Cours> coursList = new ArrayList<>();
        String sql = SELECT_COURS +
                "WHERE c.id_cours IN (" +
                "SELECT cours_id FROM seance WHERE groupe_id = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) coursList.add(creerCours(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }
}