package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IEquipementDAO;
import modele.Equipement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementDAO implements IEquipementDAO {

    public List<Equipement> findAll() {
        List<Equipement> liste = new ArrayList<>();
        String sql = "SELECT * FROM equipement ORDER BY nom_equipement";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Equipement(
                        rs.getInt("id_equipement"),
                        rs.getString("nom_equipement"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public void ajouter(Equipement eq) {
        String sql = "INSERT INTO equipement(nom_equipement, description) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eq.getNomEquipement());
            stmt.setString(2, eq.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void modifier(Equipement eq) {
        String sql = "UPDATE equipement SET nom_equipement=?, description=? WHERE id_equipement=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eq.getNomEquipement());
            stmt.setString(2, eq.getDescription());
            stmt.setInt(3, eq.getIdEquipement());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void supprimer(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement delLien = conn.prepareStatement(
                    "DELETE FROM salle_equipement WHERE id_equipement=?");
            delLien.setInt(1, id); delLien.executeUpdate();
            PreparedStatement delEq = conn.prepareStatement(
                    "DELETE FROM equipement WHERE id_equipement=?");
            delEq.setInt(1, id); delEq.executeUpdate();
            conn.commit();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Equipement> trouverParSalle(int salleId) {
        List<Equipement> liste = new ArrayList<>();
        String sql = "SELECT e.* FROM equipement e " +
                "JOIN salle_equipement se ON e.id_equipement = se.id_equipement " +
                "WHERE se.id_salle = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                liste.add(new Equipement(
                        rs.getInt("id_equipement"),
                        rs.getString("nom_equipement"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public void assignerEquipement(int salleId, int equipementId) {
        String sql = "INSERT IGNORE INTO salle_equipement(id_salle, id_equipement) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId); stmt.setInt(2, equipementId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void retirerEquipement(int salleId, int equipementId) {
        String sql = "DELETE FROM salle_equipement WHERE id_salle=? AND id_equipement=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, salleId); stmt.setInt(2, equipementId);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}