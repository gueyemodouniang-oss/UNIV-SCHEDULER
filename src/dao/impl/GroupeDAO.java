package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IGroupeDAO;
import modele.Groupe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupeDAO implements IGroupeDAO {

    private Groupe creerGroupe(ResultSet rs) throws SQLException {
        return new Groupe(
                rs.getInt("idGroupe"),
                rs.getString("nomGroupe"),
                rs.getInt("effectif")
        );
    }

    @Override
    public void ajouter(Groupe groupe) {
        String sql = "INSERT INTO Groupe(nomGroupe, effectif, annee_id) VALUES(?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupe.getNomGroupe());
            stmt.setInt(2, groupe.getEffectif());
            stmt.setInt(3, groupe.getAnneeId()); // ✅ voir note ci-dessous
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Groupe trouverParId(int id) {
        String sql = "SELECT * FROM Groupe WHERE idGroupe=?";
        Groupe groupe = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) groupe = creerGroupe(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupe;
    }

    @Override
    public List<Groupe> findAll() {
        List<Groupe> groupes = new ArrayList<>();
        String sql = "SELECT * FROM Groupe";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) groupes.add(creerGroupe(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupes;
    }

    @Override
    public void modifier(Groupe groupe) {
        String sql = "UPDATE Groupe SET nomGroupe=?, effectif=?, annee_id=? WHERE idGroupe=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupe.getNomGroupe());
            stmt.setInt(2, groupe.getEffectif());
            stmt.setInt(3, groupe.getAnneeId());
            stmt.setInt(4, groupe.getIdGroupe());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM Groupe WHERE idGroupe=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Groupe> trouverParAnnee(int anneeId) {
        List<Groupe> groupes = new ArrayList<>();
        String sql = "SELECT * FROM Groupe WHERE annee_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, anneeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) groupes.add(creerGroupe(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupes;
    }
}