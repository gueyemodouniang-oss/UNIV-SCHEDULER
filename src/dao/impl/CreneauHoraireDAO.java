package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.ICreneauHoraireDAO;
import modele.CreneauHoraire;
import modele.enums.Jour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CreneauHoraireDAO implements ICreneauHoraireDAO {

    private CreneauHoraire creerCreneau(ResultSet rs) throws SQLException {
        return new CreneauHoraire(
                rs.getInt("id_creneau"),
                Jour.valueOf(rs.getString("jour")),
                rs.getTime("heure_debut").toLocalTime(),
                rs.getTime("heure_fin").toLocalTime()
        );
    }

    @Override
    public void ajouter(CreneauHoraire creneau) {
        String sql = "INSERT INTO creneau_horaire(jour, heure_debut, heure_fin) VALUES(?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, creneau.getJour().name());
            stmt.setTime(2, Time.valueOf(creneau.getHeureDebut()));
            stmt.setTime(3, Time.valueOf(creneau.getHeureFin()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CreneauHoraire trouverParId(int id) {
        String sql = "SELECT * FROM creneau_horaire WHERE id_creneau=?";
        CreneauHoraire creneau = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) creneau = creerCreneau(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creneau;
    }

    @Override
    public List<CreneauHoraire> findAll() {
        List<CreneauHoraire> creneaux = new ArrayList<>();
        String sql = "SELECT * FROM creneau_horaire";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) creneaux.add(creerCreneau(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creneaux;
    }

    @Override
    public void modifier(CreneauHoraire creneau) {
        String sql = "UPDATE creneau_horaire SET jour=?, heure_debut=?, heure_fin=? WHERE id_creneau=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, creneau.getJour().name());
            stmt.setTime(2, Time.valueOf(creneau.getHeureDebut()));
            stmt.setTime(3, Time.valueOf(creneau.getHeureFin()));
            stmt.setInt(4, creneau.getIdCreneauHoraire());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM creneau_horaire WHERE id_creneau=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CreneauHoraire> trouverParJour(Jour jour) {
        List<CreneauHoraire> creneaux = new ArrayList<>();
        String sql = "SELECT * FROM creneau_horaire WHERE jour=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jour.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) creneaux.add(creerCreneau(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return creneaux;
    }
}