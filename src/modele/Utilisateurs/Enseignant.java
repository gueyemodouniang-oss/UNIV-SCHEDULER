package modele.Utilisateurs;

import modele.enums.Role;

public class Enseignant extends Utilisateur {

    private String specialite;
    private String departement;

    // Constructeur
    public Enseignant(int id, String prenom, String nom, String email,
                      String motDePasse, Role role,
                      String specialite, String departement) {

        super(id, prenom, nom, email, motDePasse, role);
        this.specialite = specialite;
        this.departement = departement;
    }

    // Implémentation de la méthode abstraite
    @Override
    public String getPermission() {
        return "ENSEIGNANT : consulter ses cours et l'emploi du temps";
    }

    // Getters
    public String getSpecialite() {
        return specialite;
    }

    public String getDepartement() {
        return departement;
    }

    // Setters
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "specialite='" + specialite + '\'' +
                ", departement='" + departement + '\'' +
                "} " + super.toString();
    }
}