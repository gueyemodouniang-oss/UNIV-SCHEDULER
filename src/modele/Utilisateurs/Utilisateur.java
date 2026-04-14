package modele.Utilisateurs;
import modele.enums.Role;


public abstract class Utilisateur {
    private int id;
    private String prenom;
    private String nom;
    private String email;
    private String motDePasse;
    private Role role;

    //Constructeur
    public Utilisateur(int Id, String prenom, String nom, String email, String motDePasse, Role role) {
        this.id = Id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public abstract String getPermission();

    public boolean SeConnecter(String email, String mot_de_passe) {
        return this.email.equals(email) && this.motDePasse.equals(mot_de_passe);
    }

    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public Role getRole(){return role;};

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}

