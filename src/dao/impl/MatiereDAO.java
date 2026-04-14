package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IMatiereDAO;
import modele.Matiere;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDAO implements IMatiereDAO {

    private Matiere creerMatiere(ResultSet rs) throws SQLException {
        return new Matiere(
                rs.getInt("id_matiere"),
                rs.getString("nom_matiere"),
                rs.getInt("volume_horaire")
        );
    }

    @Override
    public void ajouter(Matiere matiere) {
        String sql = "INSERT INTO matiere(nom_matiere, volume_horaire) VALUES(?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matiere.getNomMatiere());
            stmt.setInt(2, matiere.getVolumeHoraire());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Matiere trouverParId(int id) {
        String sql = "SELECT * FROM matiere WHERE id_matiere=?";
        Matiere matiere = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) matiere = creerMatiere(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matiere;
    }

    @Override
    public List<Matiere> findAll() {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM matiere";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) matieres.add(creerMatiere(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matieres;
    }

    @Override
    public void modifier(Matiere matiere) {
        String sql = "UPDATE matiere SET nom_matiere=?, volume_horaire=? WHERE id_matiere=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matiere.getNomMatiere());
            stmt.setInt(2, matiere.getVolumeHoraire());
            stmt.setInt(3, matiere.getIdMatiere());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM matiere WHERE id_matiere=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}