package modele.Utilisateurs;

import modele.enums.Role;

public class Gestionnaire extends Utilisateur {

    private String service;
    private Role role;

    // Constructeur
    public Gestionnaire(int id, String prenom, String nom, String email,
                        String motDePasse, Role role, String service) {

        super(id, prenom, nom, email, motDePasse, role);
        this.service = service;
    }

    // Implémentation de la méthode abstraite
    @Override
    public String getPermission() {
        return "GESTIONNAIRE : gérer les salles et les emplois du temps";
    }

    // Getter
    public String getService() {
        return service;
    }

    // Setter
    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Gestionnaire{" +
                "service='" + service + '\'' +
                "} " + super.toString();
    }
}