package modele.Utilisateurs;

import modele.enums.Role;

public class Etudiant extends Utilisateur {

    private String matricule;
    private String filiere;
    private String niveau;

    // Constructeur
    public Etudiant(int id, String prenom, String nom, String email,
                    String motDePasse, Role role,
                    String matricule, String filiere, String niveau) {

        super(id, prenom, nom, email, motDePasse, role);
        this.matricule = matricule;
        this.filiere = filiere;
        this.niveau = niveau;
    }

    // Implémentation de la méthode abstraite
    @Override
    public String getPermission() {
        return "ETUDIANT : consulter l'emploi du temps";
    }

    // Getters
    public String getMatricule() {
        return matricule;
    }

    public String getFiliere() {
        return filiere;
    }

    public String getNiveau() {
        return niveau;
    }

    // Setter exemple
    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "matricule='" + matricule + '\'' +
                ", filiere='" + filiere + '\'' +
                ", niveau='" + niveau + '\'' +
                "} " + super.toString();
    }
}