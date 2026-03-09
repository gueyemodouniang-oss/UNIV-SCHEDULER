package modele.Utilisateurs;

import modele.enums.Role;

public class Admin extends Utilisateur {

    // Constructeur
    public Admin(int id, String prenom, String nom, String email, String motDePasse, Role role) {
        super(id, prenom, nom, email, motDePasse, role);
    }

    // Implémentation de la méthode abstraite
    @Override
    public String getPermission() {
        return "ADMIN : accès complet au système";
    }

    // Méthodes spécifiques à l'admin
    public void ajouterSalle() {
        System.out.println("Salle ajoutée par l'administrateur");
    }

    public void supprimerSalle() {
        System.out.println("Salle supprimée par l'administrateur");
    }

    public void gererUtilisateurs() {
        System.out.println("Gestion des utilisateurs");
    }
}