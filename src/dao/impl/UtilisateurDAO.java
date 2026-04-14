package dao.impl;

import dao.DatabaseConnection;
import dao.interfaces.IUtilisateurDAO;
import modele.Utilisateurs.Admin;
import modele.Utilisateurs.Enseignant;
import modele.Utilisateurs.Etudiant;
import modele.Utilisateurs.Gestionnaire;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements IUtilisateurDAO {

    // ✅ Méthode utilitaire centrale
    private Utilisateur creerUtilisateur(ResultSet rs) throws SQLException {
        Role role = Role.values()[rs.getInt("role_id") - 1]; // -1 car MySQL commence à 1
        int id = rs.getInt("id");
        String prenom = rs.getString("prenom");
        String nom = rs.getString("nom");
        String email = rs.getString("email");
        String mdp = rs.getString("mot_de_passe");

        if (role == Role.Admin)
            return new Admin(id, prenom, nom, email, mdp, Role.Admin);

        if (role == Role.Enseignant)
            return new Enseignant(id, prenom, nom, email, mdp, Role.Enseignant,
                    rs.getString("specialite"),
                    rs.getString("departement"));

        if (role == Role.Etudiant) {
            Etudiant etu = new Etudiant(id, prenom, nom, email, mdp, Role.Etudiant,
                    rs.getString("matricule"),
                    rs.getString("filiere"),
                    rs.getString("niveau"));
            etu.setGroupeId(rs.getInt("groupe_id")); // ← NOUVEAU
            return etu;
        }
        if (role == Role.Gestionnaire)
            return new Gestionnaire(id, prenom, nom, email, mdp, Role.Gestionnaire,
                    rs.getString("service"));

        return null;
    }

    @Override
    public void ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, role_id, " +
                "specialite, departement, matricule, filiere, niveau, service) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setInt(5, utilisateur.getRole().ordinal() + 1); // +1 car MySQL commence à 1

            // Champs spécifiques selon le rôle
            if (utilisateur instanceof Enseignant e) {
                stmt.setString(6, e.getSpecialite());
                stmt.setString(7, e.getDepartement());
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            } else if (utilisateur instanceof Etudiant et) {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setString(8, et.getMatricule());
                stmt.setString(9, et.getFiliere());
                stmt.setString(10, et.getNiveau());
                stmt.setNull(11, Types.VARCHAR);
            } else if (utilisateur instanceof Gestionnaire g) {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setString(11, g.getService());
            } else {
                // Admin — tous null
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilisateur trouverParId(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id=?";
        Utilisateur u = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) u = creerUtilisateur(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur u = creerUtilisateur(rs);
                if (u != null) utilisateurs.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateurs;
    }

    @Override
    public void modifier(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=?, " +
                "role_id=?, specialite=?, departement=?, matricule=?, filiere=?, " +
                "niveau=?, service=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setInt(5, utilisateur.getRole().ordinal() + 1);

            if (utilisateur instanceof Enseignant e) {
                stmt.setString(6, e.getSpecialite());
                stmt.setString(7, e.getDepartement());
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            } else if (utilisateur instanceof Etudiant et) {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setString(8, et.getMatricule());
                stmt.setString(9, et.getFiliere());
                stmt.setString(10, et.getNiveau());
                stmt.setNull(11, Types.VARCHAR);
            } else if (utilisateur instanceof Gestionnaire g) {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setString(11, g.getService());
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            }

            stmt.setInt(12, utilisateur.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM utilisateur WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilisateur login(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE email=? AND mot_de_passe=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return creerUtilisateur(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}