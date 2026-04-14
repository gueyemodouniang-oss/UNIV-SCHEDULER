package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IBatimentDAO;
import modele.Batiment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatimentDAO implements IBatimentDAO {

    private Batiment creerBatiment(ResultSet rs) throws SQLException {
        return new Batiment(
                rs.getInt("id_batiment"),
                rs.getString("nom_batiment"),
                rs.getString("localisation")
        );
    }

    @Override
    public void ajouter(Batiment batiment) {
        String sql = "INSERT INTO batiment(nom_batiment, localisation) VALUES(?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batiment.getNomBatiment());
            stmt.setString(2, batiment.getLocalisation());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Batiment trouverParId(int id) {
        String sql = "SELECT * FROM batiment WHERE id_batiment=?";
        Batiment batiment = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) batiment = creerBatiment(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return batiment;
    }

    @Override
    public List<Batiment> findAll() {
        List<Batiment> batiments = new ArrayList<>();
        String sql = "SELECT * FROM batiment";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) batiments.add(creerBatiment(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return batiments;
    }

    @Override
    public void modifier(Batiment batiment) {
        String sql = "UPDATE batiment SET nom_batiment=?, localisation=? WHERE id_batiment=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batiment.getNomBatiment());
            stmt.setString(2, batiment.getLocalisation());
            stmt.setInt(3, batiment.getIdBatiment());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM batiment WHERE id_batiment=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}